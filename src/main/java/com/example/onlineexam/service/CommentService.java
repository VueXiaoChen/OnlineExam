package com.example.onlineexam.service;


import com.example.onlineexam.domain.Comment;
import com.example.onlineexam.domain.CommentExample;
import com.example.onlineexam.mapper.CommentMapper;
import com.example.onlineexam.req.CommentReq;
import com.example.onlineexam.resp.CommentResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CommentService.class);

    @Resource
    public CommentMapper commentMapper;

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

}
