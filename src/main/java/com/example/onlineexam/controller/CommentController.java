package com.example.onlineexam.controller;


import com.example.onlineexam.domain.CommentTree;
import com.example.onlineexam.mapper.CommentMapper;
import com.example.onlineexam.req.CommentReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.CommentResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.onlineexam.util.RedisUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RedisUtils redisUtils;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated CommentReq commentReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<CommentResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<CommentResp> data = commentService.list(commentReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody CommentReq commentReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        commentService.save(commentReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(commentReq.getId())) {
            resp.setMessage("保存成功");
        } else {

            resp.setMessage("修改成功");
        }
        //将信息添加到返回信息里
        return resp;
    }

    /**
     * 发表评论
     * @param vid   视频id
     * @param rootId    根评论id
     * @param parentId  被回复评论id
     * @param toUserId  被回复者uid
     * @param content   评论内容
     * @return  响应对象
     */
    @PostMapping("/add")
    public CommonResp addComment(@RequestBody CommentReq commentReq) {
        CommonResp commonResp = new CommonResp();
        CommentTree commentTree = commentService.sendComment(commentReq);
        if (commentTree == null) {
            commonResp.setMessage("发送失败");
        }
        commonResp.setData(commentTree);
        return commonResp;
    }
    //单个删除
    @GetMapping("/delete/{id}")
    //@PathVariable与{blogId}是绑定的
    public CommonResp delete(@PathVariable Integer id) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //删除数据
        commentService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }
    @GetMapping("/getcomment/{vid}/{offset}/{type}")
    //@PathVariable与{blogId}是绑定的
    public CommonResp getCommentTreeByVid(@PathVariable Integer vid, @PathVariable Long offset, @PathVariable Integer type) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        long count = redisUtils.zCard("comment_video:" + vid);
        Map<String, Object> map = new HashMap<>();
        if (offset >= count) {
            // 表示前端已经获取到全部根评论了，没必要继续
            map.put("more", false);
            map.put("comments", Collections.emptyList());
        } else if (offset + 10 >= count){
            // 表示这次查询会查完全部根评论
            map.put("more", false);
            map.put("comments", commentService.getCommentTreeByVid(vid, offset, type));
        } else {
            // 表示这次查的只是冰山一角，还有很多评论没查到
            map.put("more", true);
            map.put("comments", commentService.getCommentTreeByVid(vid, offset, type));
        }
        resp.setData(map);
        //将信息添加到返回信息里
        resp.setMessage("获取评论成功");

        return resp;
    }
}
