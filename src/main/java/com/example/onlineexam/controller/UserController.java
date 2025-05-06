package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.UserMapper;
import com.example.onlineexam.req.UserReq;
import com.example.onlineexam.req.UsersLoadingReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.UserResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.resp.UsersLoadingResp;
import com.example.onlineexam.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated UserReq userReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<UserResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<UserResp> data = userService.list(userReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody UserReq userReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        userService.save(userReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(userReq.getUid())) {

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
        userService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }


    //单个删除
    @PostMapping("/loading")
    //@PathVariable与{blogId}是绑定的
    public CommonResp loading(@Validated @RequestBody UsersLoadingReq usersLoadingReq) {
        //返回信息里面定义返回的类型
        CommonResp<UsersLoadingResp> resp = new CommonResp<>();
        resp.setData(userService.loading(usersLoadingReq));
        //将信息添加到返回信息里
        resp.setMessage("登录成功");
        return resp;
    }
}
