package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.ChatMapper;
import com.example.onlineexam.req.ChatReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.ChatResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat")
public class ChatController {
    @Resource
    private ChatService chatService;
    @Autowired
    private ChatMapper chatMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated ChatReq chatReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<ChatResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<ChatResp> data = chatService.list(chatReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody ChatReq chatReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        chatService.save(chatReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(chatReq.getId())) {

            resp.setMessage("保存成功");
        } else {

            resp.setMessage("修改成功");
        }
        //将信息添加到返回信息里
        return resp;
    }

    //单个删除
    @GetMapping("/delete/{id}")
    //@PathVariable与{blogId}是绑定的
    public CommonResp delete(@PathVariable Integer id) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //删除数据
        chatService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }
}
