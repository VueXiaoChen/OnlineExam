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

import java.util.*;
import java.util.concurrent.CompletableFuture;
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
        return commentMapper.selectByExample(example);
    }

    /**
     * 根据视频 vid 获取根评论列表，一次查 10 条
     * @param vid 视频 id
     * @param offset 偏移量，已经获取到的根评论数量
     * @param type 1:按热度排序 2:按时间排序
     * @return List<Comment>
     */

    public List<Comment> getRootCommentsByVid(Integer vid, Long offset, Integer type) {
        Set<Object> rootIdsSet;
        if (type == 1) {
            // 按热度排序需查询全部数据，后续在MySQL筛选
            rootIdsSet = redisUtils.zReverange("comment_video:" + vid, 0L, -1L);
        } else {
            // 按时间排序查询分页数据
            rootIdsSet = redisUtils.zReverange("comment_video:" + vid, offset, offset + 9L);
        }
        if (rootIdsSet == null || rootIdsSet.isEmpty()) {
            return Collections.emptyList();
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

        return commentMapper.selectByExample(example);
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
}