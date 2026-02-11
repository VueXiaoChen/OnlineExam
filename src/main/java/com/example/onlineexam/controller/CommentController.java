package com.example.onlineexam.controller;


import com.example.onlineexam.RedisMessageReceive.RedisReceiver;
import com.example.onlineexam.domain.Comment;
import com.example.onlineexam.domain.CommentTree;
import com.example.onlineexam.mapper.CommentMapper;
import com.example.onlineexam.req.CommentReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.CommentResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.CommentService;
import com.example.onlineexam.util.CurrentUser;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.onlineexam.util.RedisUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/comment")
public class CommentController {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CommentController.class);
    @Resource
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CurrentUser currentUser;
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
        LOG.info("count数据，count={}",redisUtils.zCard("comment_video:" + vid));
        if (count == 0) {
            // 调用 Service 层方法获取数据库中该视频的根评论总数（需自行实现）
            List<CommentTree> comments = commentService.getCommentTreeByVid(vid, offset, type);
            count = comments.size();
            // 可选：将数量缓存到 Redis（使用 String 结构，避免下次再次穿透）
            // 2.2 批量存入 Redis ZSet，以创建时间戳作为 score
            if (!comments.isEmpty()) {
                List<RedisUtils.ZObjTime> zObjTimes = comments.stream()
                        .map(c -> new RedisUtils.ZObjTime(c.getId(), c.getCreateTime()))
                        .collect(Collectors.toList());
                redisUtils.zsetOfCollectionByTime("comment_video:" + vid, zObjTimes);

                // 2.3 设置 ZSet 过期时间（例如 1 小时，避免冷数据常驻）
                redisUtils.setExpire("comment_video:" + vid, 3600);
            }
        }
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
        resp.setCode(200);
        return resp;
    }
    /**
     * 删除评论
     * @param id 评论id
     * @return  响应对象
     */
    @GetMapping("/deletes/{id}")
    public CommonResp delComment(@PathVariable("id") Integer id) {
        Integer loginUid = 25;
        return commentService.deleteComment(id, loginUid, false);
    }

    /**
     * 展开更多回复评论
     * @param id 根评论id
     * @return 完整的一棵包含全部评论的评论树
     */
    @GetMapping("/reply/getmore/{id}")
    public CommonResp getMoreCommentById(@PathVariable("id") Integer id) {
        CommonResp<CommentTree> commonResp = new CommonResp<>();
        commonResp.setMessage("获取成功");
        commonResp.setData(commentService.getMoreCommentsById(id));
        return commonResp;
    }

    /**
     * 获取用户点赞点踩评论集合
     */
    @GetMapping("/comment/get-like-and-dislike")
    public CommonResp getLikeAndDislike() {
        Integer uid = (Integer) currentUser.getUserId();

        CommonResp commonResp = new CommonResp();
        commonResp.setCode(200);
        commonResp.setData(commentService.getUserLikeAndDislike(uid));

        return commonResp;
    }

    /**
     * 点赞或点踩某条评论
     * @param id    评论id
     * @param isLike true 赞 false 踩
     * @param isSet  true 点 false 取消
     */
    @PostMapping("/comment/love-or-not")
    public CommonResp loveOrNot(@RequestParam("id") Integer id,
                                    @RequestParam("isLike") boolean isLike,
                                    @RequestParam("isSet") boolean isSet) {
        Integer uid = (Integer) currentUser.getUserId();
        commentService.userSetLikeOrUnlike(uid, id, isLike, isSet);
        return new CommonResp();
    }

    /**
     * 获取UP主觉得很淦的评论
     * @param uid   UP主uid
     * @return  点赞的评论id列表
     */
    @GetMapping("/comment/get-up-like")
    public CommonResp getUpLike(@RequestParam("uid") Integer uid) {
        CommonResp commonResp = new CommonResp();
        Map<String, Object> map = commentService.getUserLikeAndDislike(uid);
        commonResp.setData(map.get("userLike"));
        return commonResp;
    }

}
