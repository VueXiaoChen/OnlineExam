package com.example.onlineexam.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlineexam.domain.Comment;
import com.example.onlineexam.domain.CommentExample;
import com.example.onlineexam.domain.CommentTree;
import com.example.onlineexam.mapper.CommentMapper;
import com.example.onlineexam.req.CommentReq;
import com.example.onlineexam.resp.CommentResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.example.onlineexam.util.RedisUtil;
import com.example.onlineexam.util.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CommentService.class);

    @Resource
    public CommentMapper commentMapper;
    @Autowired
    private RedisUtils redisUtils;


    public PageResp<CommentResp> list(CommentReq commentReq) {
        //固定写法
        CommentExample example = new CommentExample();
        //固定写法
        CommentExample.Criteria criteria = example.createCriteria();
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

        //tree.setUser(userService.getUserById(comment.getUid()));
        //tree.setToUser(userService.getUserById(comment.getToUserId()));

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
        List<Integer> rootIds = rootIdsSet.stream()
                .map(obj -> Integer.valueOf(obj.toString()))
                .collect(Collectors.toList());

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


}
