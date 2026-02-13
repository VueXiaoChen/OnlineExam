package com.example.onlineexam.service;

import com.example.onlineexam.domain.Comment;
import com.example.onlineexam.domain.CommentExample;
import com.example.onlineexam.domain.CommentTree;
import com.example.onlineexam.domain.Video;
import com.example.onlineexam.mapper.CommentMapper;
import com.example.onlineexam.mapper.VideoMapper;
import com.example.onlineexam.req.CommentReq;
import com.example.onlineexam.resp.CommentResp;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.example.onlineexam.util.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


@Service
public class CommentService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CommentService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    public CommentMapper commentMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoStatsService videoStatsService;

    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;


    public PageResp<CommentResp> list(CommentReq commentReq) {
        //固定写法
        CommentExample example = new CommentExample();
        //固定写法
        CommentExample.Criteria criteria = example.createCriteria();
        if (!ObjectUtils.isEmpty(commentReq.getVid())) {
            criteria.andVidEqualTo(commentReq.getVid());
        }
        //分页(获取从页面传来的数据)
        PageHelper.startPage(commentReq.getPage(), commentReq.getSize());
        //类接收返回的数据
        List<Comment> sortsList = commentMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<CommentResp> data = CopyUtil.copyList(sortsList, CommentResp.class);
        //定义分页获取总数
        PageInfo<Comment> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<CommentResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(CommentReq commentReq) {
        Comment comment = CopyUtil.copy(commentReq, Comment.class);
        //固定写法
        CommentExample example = new CommentExample();
        //固定写法
        CommentExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(commentReq.getId())) {
            commentMapper.insertSelective(comment);
        } else {
            //更新数据
            commentMapper.updateByPrimaryKeySelective(comment);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        commentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取评论树列表
     * @param vid   对应视频ID
     * @param offset 分页偏移量（已经获取到的评论树的数量）
     * @param type  排序类型 1 按热度排序 2 按时间排序
     * @return  评论树列表
     */
    public List<CommentTree> getCommentTreeByVid(Integer vid, Long offset, Integer type) {
        // 查询父级评论
        List<Comment> rootComments = getRootCommentsByVid(vid, offset, type);

        // 并行执行每个根级评论的子评论查询任务
        List<CommentTree> commentTreeList = rootComments.stream().parallel()
                .map(rootComment ->buildCommentTree(rootComment, 0L, 2L))
                .collect(Collectors.toList());
        return commentTreeList;
    }

    /**
     * 构建评论树
     * @param comment 根评论
     * @param start 子评论开始偏移量
     * @param stop  子评论结束偏移量
     * @return  单棵评论树
     */
    private CommentTree buildCommentTree(Comment comment, Long start, Long stop) {
        CommentTree tree = new CommentTree();
        tree.setId(comment.getId());
        tree.setVid(comment.getVid());
        tree.setRootId(comment.getRootId());
        tree.setParentId(comment.getParentId());
        tree.setContent(comment.getContent());
        tree.setCreateTime(comment.getCreateTime());
        tree.setLove(comment.getLove());
        tree.setBad(comment.getBad());

        tree.setUser(userService.getUserById(comment.getUid()));
        tree.setToUser(userService.getUserById(comment.getToUserId()));

        // 递归查询构建子评论树
        // 这里如果是根节点的评论，则查出他的子评论； 如果不是根节点评论，则不查，只填写 User 信息。
        if (comment.getRootId() == 0) {
            long count = redisUtils.zCard("comment_reply:" + comment.getId());
            tree.setCount(count);

            List<Comment> childComments = getChildCommentsByRootId(comment.getId(), comment.getVid(), start, stop);

            List<CommentTree> childTreeList = childComments.stream().parallel()
                    .map(childComment -> buildCommentTree(childComment, start, stop))
                    .collect(Collectors.toList());

            tree.setReplies(childTreeList);
        }

        return tree;
    }

    /**
     * @param rootId 根级节点的评论 id, 即楼层 id
     * @param vid 视频的 vid
     * @return 1. 根据 redis 查找出回复该评论的子评论 id 列表
     * 2. 根据 id 多线程查询出所有评论的详细信息
     */
    public List<Comment> getChildCommentsByRootId(Integer rootId, Integer vid, Long start, Long stop) {
        Set<Object> replyIds = redisUtils.zRange("comment_reply:" + rootId, start, stop);
        // 2. 缓存未命中 → 从数据库加载全部 ID，并初始化 ZSet
        if (replyIds == null || replyIds.isEmpty()) {
            // 2.1 加锁，防止并发下多次查询数据库（单机版简单同步）
            synchronized (this) {
                // 双重检查：再次尝试从缓存获取
                replyIds = redisUtils.zRange("comment_reply:" + rootId, start, stop);
                if (replyIds == null || replyIds.isEmpty()) {
                    // 2.2 从数据库查询当前根评论下的所有子评论（按创建时间升序，假设越早越靠前）
                    List<Comment> allReplies = commentMapper.selectByExample(
                            new CommentExample() {{
                                createCriteria().andRootIdEqualTo(rootId)
                                        .andIsDeletedNotEqualTo(1);
                                setOrderByClause("create_time ASC");
                            }}
                    );

                    // 2.3 构建 ZSet 批量添加的数据：member = 评论ID, score = 创建时间戳
                    List<RedisUtils.ZObjTime> zObjTimes = allReplies.stream()
                            .map(c -> new RedisUtils.ZObjTime(c.getId(), c.getCreateTime()))
                            .collect(Collectors.toList());

                    // 2.4 批量写入 Redis ZSet
                    if (!zObjTimes.isEmpty()) {
                        redisUtils.zsetOfCollectionByTime("comment_reply:" + rootId, zObjTimes);
                        // 设置过期时间（例如1小时），避免冷数据常驻
                        redisUtils.setExpire("comment_reply:" + rootId, 3600);
                    }

                    // 2.5 重新获取当前页的 ID 列表
                    replyIds = redisUtils.zRange("comment_reply:" + rootId, start, stop);
                }
            }
        }
        // 3. 如果仍然为空，说明数据库中也确实没有数据
        if (replyIds == null || replyIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 转换为List<Integer>以便在Criteria中使用
        List<Integer> replyIdList = replyIds.stream()
                .map(obj -> Integer.valueOf(obj.toString()))
                .collect(Collectors.toList());
        // 使用CommentExample构建查询条件
        CommentExample example = new CommentExample();
        CommentExample.Criteria criteria = example.createCriteria();

        // 设置查询条件：ID在replyIdList中且未删除
        criteria.andIdIn(replyIdList)
                .andIsDeletedNotEqualTo(1);
        // 执行查询
        LOG.info("replyIdList数据:[{}]", replyIdList);
        return commentMapper.selectByExample(example);
    }

    /**
     * 根据视频 vid 获取根评论列表，一次查 10 条
     * @param vid 视频 id
     * @param offset 偏移量，已经获取到的根评论数量
     * @param type 1:按热度排序 2:按时间排序
     * @return List<Comment>
     */

    /**
     * 直接查询数据库（不依赖Redis）
     */
    private List<Comment> queryFromDatabaseDirectly(Integer vid, Long offset, Integer type) {
        CommentExample example = new CommentExample();
        CommentExample.Criteria criteria = example.createCriteria();

        // 查询条件
        criteria.andVidEqualTo(vid)
                .andIsDeletedNotEqualTo(1)
                .andRootIdEqualTo(0);// 根评论

        // 设置排序
        if (type == 1) {
            // 热度排序
            example.setOrderByClause("(love - bad) DESC");
        } else {
            // 时间排序
            example.setOrderByClause("create_time DESC");
        }

        // 设置分页
        int pageNum = (offset.intValue() / 10) + 1;  // 计算页码
        int pageSize = 10;
        PageHelper.startPage(pageNum, pageSize);

        // 执行查询
        List<Comment> result = commentMapper.selectByExample(example);
        LOG.info("直接数据库查询，vid={}, offset={}, 结果数量={}",
                vid, offset, result.size());

        return result;
    }

    public List<Comment> getRootCommentsByVid(Integer vid, Long offset, Integer type) {
        Set<Object> rootIdsSet;
        if (type == 1) {
            // 按热度排序需查询全部数据，后续在MySQL筛选
            rootIdsSet = redisUtils.zReverange("comment_video:" + vid, 0L, -1L);
        } else {
            // 按时间排序查询分页数据
            rootIdsSet = redisUtils.zReverange("comment_video:" + vid, offset, offset + 9L);
        }
        LOG.info("rootIdsSet的数据，rootIdsSet={}", rootIdsSet);
        if (rootIdsSet == null || rootIdsSet.isEmpty()) {
            LOG.info("Redis中没有数据，直接查询数据库，vid={}", vid);
            return queryFromDatabaseDirectly(vid, offset, type);
            //return Collections.emptyList();
        }

        // 转换为List<Integer>便于后续查询
//        List<Integer> rootIds = rootIdsSet.stream()
//                .filter(Objects::nonNull)  // 过滤null
//                .flatMap(obj -> {
//                    try {
//                        // 1. 将对象转为JSON字符串
//                        String objJson = JSON.toJSONString(obj);
//                        // 2. 解析JSON为Comment列表（兼容单个对象和数组）
//                        List<Comment> comments;
//                        if (objJson.trim().startsWith("[")) {
//                            comments = JSON.parseArray(objJson, Comment.class);
//                        } else {
//                            Comment comment = JSON.parseObject(objJson, Comment.class);
//                            comments = Collections.singletonList(comment);
//                        }
//                        // 3. 提取所有有效id
//                        return comments.stream()
//                                .filter(c -> c != null && c.getId() != null)
//                                .map(Comment::getId);
//
//                    } catch (Exception e) {
//                        return Stream.empty();  // 解析失败时返回空流
//                    }
//                })
//                .collect(Collectors.toList());

        List<Integer> rootIds = rootIdsSet.stream()
                .filter(Objects::nonNull)
                .map(obj -> {
                    try {
                        return Integer.parseInt(obj.toString());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        LOG.info("rootIds:{}",rootIds);
        // 使用CommentExample构建查询条件
        CommentExample example = new CommentExample();
        CommentExample.Criteria criteria = example.createCriteria();

        // 基础条件：ID在rootIds中且未删除
        criteria.andIdIn(rootIds).andIsDeletedNotEqualTo(1);

        // 根据类型设置排序
        if (type == 1) {
            // 热度排序：(love - bad)降序 + 分页
            example.setOrderByClause("(love - bad) DESC LIMIT 10 OFFSET " + offset);
        } else {
            // 时间排序：create_time降序
            example.setOrderByClause("create_time DESC");
        }
        // ========== 关键：执行数据库查询 ==========
        List<Comment> result = commentMapper.selectByExample(example);
        LOG.info("数据库查询结果数量: {}", result.size());

        return result;
    }
    /**
     * 获取更多回复评论
     * @param id 根评论id
     * @return  包含全部回复评论的评论树
     */
    public CommentTree getMoreCommentsById(Integer id) {
        Comment comment = commentMapper.selectById(id);
        return buildCommentTree(comment, 0L, -1L);
    }

    public CommentTree sendComment(CommentReq commentReq) {
        if (commentReq.getContent() == null || commentReq.getContent().length() == 0 || commentReq.getContent().length() > 2000) return null;
        Comment comment = CopyUtil.copy(commentReq, Comment.class);
        comment.setCreateTime(new Date());
        commentMapper.insertSelective(comment);
        CommentTree commentTree = buildCommentTree(comment, 0L, -1L);
        try {
            CompletableFuture.runAsync(() -> {
                // 如果不是根级评论，则加入 redis 对应的 zset 中
                if (!commentReq.getRootId().equals(0)) {
                    redisUtils.zset("comment_reply:" + commentReq.getRootId(), comment.getId());
                } else {
                    redisUtils.zset("comment_video:"+ commentReq.getVid(), comment.getId());
                }
                // 表示被回复的用户收到的回复评论的 id 有序集合
                // 如果不是回复自己
                if(!Objects.equals(comment.getToUserId(), comment.getUid())) {
                    redisUtils.zset("reply_zset:" + comment.getToUserId(), comment.getId());
                }
            }, taskExecutor);
        } catch (Exception e) {
            LOG.info("发送评论过程中出现一点差错");
            e.printStackTrace();
        }
        return commentTree;
    }
    public CommonResp deleteComment(Integer id, Integer uid, boolean isAdmin) {
        CommonResp customResponse = new CommonResp();
        // 使用Example查询评论是否存在
        CommentExample example = new CommentExample();
        CommentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id).andIsDeletedNotEqualTo(1); // is_deleted != 1
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments == null || comments.isEmpty()) {
            customResponse.setSuccess(false);
            customResponse.setMessage("评论不存在");
            return customResponse;
        }
        Comment comment = comments.get(0);
        // 权限校验
        Video video = videoMapper.selectByPrimaryKey(comment.getVid());
        if (Objects.equals(comment.getUid(), uid) || isAdmin || Objects.equals(video.getUid(), uid)) {
            // 更新is_deleted状态
            Comment updateComment = new Comment();
            updateComment.setIsDeleted(1); // 标记为删除
            CommentExample updateExample = new CommentExample();
            updateExample.createCriteria()
                    .andIdEqualTo(comment.getId());

            commentMapper.updateByExampleSelective(updateComment, updateExample);
            /* 处理关联数据 */
            if (Objects.equals(comment.getRootId(), 0)) {
                // 根评论处理
                int count = Math.toIntExact(redisUtils.zCard("comment_reply:" + comment.getId()));
                videoStatsService.updateStats(comment.getVid(), "comment", false, count + 1);
                redisUtils.zsetDelMember("comment_video:" + comment.getVid(), comment.getId());
                redisUtils.delValue("comment_reply:" + comment.getId());
            } else {
                // 子评论处理
                videoStatsService.updateStats(comment.getVid(), "comment", false, 1);
                redisUtils.zsetDelMember("comment_reply:" + comment.getRootId(), comment.getId());
            }

            customResponse.setSuccess(true);
            customResponse.setMessage("删除成功!");
        } else {
            customResponse.setSuccess(false);
            customResponse.setMessage("你无权删除该条评论");
        }

        return customResponse;
    }

    /**
     * 同时相对更新点赞和点踩
     * @param id    评论id
     * @param addLike   true 点赞 false 点踩
     */
    public void updateLikeAndDisLike(Integer id, boolean addLike) {
        CommentExample example = new CommentExample();
        CommentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);  // 设置查询条件
        // 创建更新对象
        Comment comment = new Comment();
        if (addLike) {
            comment.setLove(comment.getLove() + 1);
            if (comment.getBad() > 0) {
                comment.setBad(comment.getBad() - 1);
            }
        } else {
            comment.setBad(comment.getBad() + 1);
            if (comment.getLove() > 0) {
                comment.setLove(comment.getLove() - 1);
            }
        }
        // 执行更新
        commentMapper.updateByExampleSelective(comment, example);
    }


    public void updateComment(Integer id, String column, boolean increase, Integer count) {
        // 1. 先查询当前值
        CommentExample example = new CommentExample();
        CommentExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);

        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.isEmpty()) {
            throw new RuntimeException("评论不存在，id: " + id);
        }

        Comment comment = comments.get(0);

        // 2. 创建更新对象
        Comment updateComment = new Comment();
        updateComment.setId(id);

        // 3. 根据列名和操作类型计算新值
        try {
            Field field = Comment.class.getDeclaredField(column);
            field.setAccessible(true);

            Integer currentValue = (Integer) field.get(comment);
            Integer newValue;

            if (increase) {
                newValue = currentValue + count;
            } else {
                newValue = Math.max(0, currentValue - count);
            }

            field.set(updateComment, newValue);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("字段不存在或访问失败: " + column, e);
        }

        // 4. 执行更新
        CommentExample updateExample = new CommentExample();
        CommentExample.Criteria updateCriteria = updateExample.createCriteria();
        updateCriteria.andIdEqualTo(id);

        int rows = commentMapper.updateByExampleSelective(updateComment, updateExample);
        if (rows == 0) {
            throw new RuntimeException("更新失败，id: " + id);
        }
    }

    /**
     * 获取用户点赞和点踩的评论集合
     *
     * @param uid 当前用户
     * @return 点赞和点踩的评论集合
     */

    public Map<String, Set<Object>> getUserLikeAndDislike(Integer uid) {
        // 使用 ConcurrentHashMap 保证线程安全
        Map<String, Set<Object>> result = new ConcurrentHashMap<>();

        CompletableFuture<Void> likeFuture = CompletableFuture.runAsync(() -> {
            Set<Object> likeSet = redisUtils.getMembers("user_like_comment:" + uid);
            // getMembers 永远不会返回 null，但为保险仍使用 null-safe 方式
            result.put("userLike", likeSet != null ? likeSet : Collections.emptySet());
        }, taskExecutor);

        CompletableFuture<Void> dislikeFuture = CompletableFuture.runAsync(() -> {
            Set<Object> dislikeSet = redisUtils.getMembers("user_dislike_comment:" + uid);
            result.put("userDislike", dislikeSet != null ? dislikeSet : Collections.emptySet());
        }, taskExecutor);

        // 等待两个任务全部完成
        CompletableFuture.allOf(likeFuture, dislikeFuture).join();

        return result;
    }

    /**
     * 点赞或点踩某条评论
     * @param uid   当前用户id
     * @param id    评论id
     * @param isLike true 赞 false 踩
     * @param isSet true 点 false 取消
     */

    public void userSetLikeOrUnlike(Integer uid, Integer id, boolean isLike, boolean isSet) {
        Boolean likeExist = redisUtils.isMember("user_like_comment:" + uid, id);
        Boolean dislikeExist = redisUtils.isMember("user_dislike_comment:" + uid, id);

        // 点赞
        if (isLike && isSet) {
            // 原本就点了赞
            if (likeExist) {
                return;
            }
            // 原本点了踩，就要取消踩
            if (dislikeExist) {
                // 1.redis中删除点踩记录
                CompletableFuture.runAsync(() -> {
                    redisUtils.delMember("user_dislike_comment:" + uid, id);
                }, taskExecutor);
                // 2. 数据库中更改评论的点赞点踩数
                CompletableFuture.runAsync(() -> {
                    commentService.updateLikeAndDisLike(id, true);
                }, taskExecutor);

            } else {
                // 原来没点踩，只需要点赞, 这里只更新评论的点赞数，下面添加点赞记录
                CompletableFuture.runAsync(() -> {
                    commentService.updateComment(id, "love", true, 1);
                }, taskExecutor);
            }
            // 添加点赞记录
            redisUtils.addMember("user_like_comment:" + uid, id);
        } else if (isLike) {
            // 取消点赞
            if (!likeExist) {
                // 原本就没有点赞，直接返回
                return;
            }
            CompletableFuture.runAsync(() -> {
                // 移除点赞记录
                redisUtils.delMember("user_like_comment:" + uid, id);
            }, taskExecutor);
            // 更新评论点赞数
            CompletableFuture.runAsync(() -> {
                commentService.updateComment(id, "love", false, 1);
            }, taskExecutor);
        } else if (isSet) {
            // 点踩
            if (dislikeExist) {
                // 原本就点了踩，直接返回
                return;
            }

            if (likeExist) {
                // 原本点了赞，要取消赞
                CompletableFuture.runAsync(() -> {
                    redisUtils.delMember("user_like_comment:" + uid, id);
                }, taskExecutor);
                // 更新评论点赞点踩的记录
                CompletableFuture.runAsync(() -> {
                    commentService.updateLikeAndDisLike(id, false);
                }, taskExecutor);

            } else {
                // 原本没有点赞，直接点踩，更新评论点踩数量
                CompletableFuture.runAsync(() -> {
                    commentService.updateComment(id, "bad", true, 1);
                }, taskExecutor);
            }
            // 更新用户点踩记录
            redisUtils.addMember("user_dislike_comment:" + uid, id);
        } else {
            // 取消点踩
            if (!dislikeExist) {
                // 原本就没有点踩直接返回
                return;
            }

            CompletableFuture.runAsync(() -> {
                // 取消用户点踩记录
                redisUtils.delMember("user_dislike_comment:" + uid, id);
            }, taskExecutor);

            CompletableFuture.runAsync(() -> {
                // 更新评论点踩数量
                commentService.updateComment(id, "bad", false, 1);
            }, taskExecutor);
        }
    }

}