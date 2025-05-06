package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.MsgUnreadMapper;
import com.example.onlineexam.req.MsgUnreadReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.MsgUnreadResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.MsgUnreadService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/msgUnread")
public class MsgUnreadController {
    @Resource
    private MsgUnreadService msgUnreadService;
    @Autowired
    private MsgUnreadMapper msgUnreadMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated MsgUnreadReq msgUnreadReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<MsgUnreadResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<MsgUnreadResp> data = msgUnreadService.list(msgUnreadReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody MsgUnreadReq msgUnreadReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        msgUnreadService.save(msgUnreadReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(msgUnreadReq.getUid())) {

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
        msgUnreadService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }
}
