package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.FavoriteMapper;
import com.example.onlineexam.req.FavoriteReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.FavoriteResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.FavoriteService;
import com.example.onlineexam.util.CurrentUser;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Resource
    private FavoriteService favoriteService;
    @Autowired
    private FavoriteMapper favoriteMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated FavoriteReq favoriteReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<FavoriteResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<FavoriteResp> data = favoriteService.list(favoriteReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody FavoriteReq favoriteReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        favoriteService.save(favoriteReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(favoriteReq.getFid())) {

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
        favoriteService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }

    @Autowired
    private CurrentUser currentUser;

    /**
     * 站内用户请求某个用户的收藏夹列表（需要jwt鉴权）
     * @param uid   被查看的用户ID
     * @return  包含收藏夹列表的响应对象
     */
    @GetMapping("/get-all/user")
    public CommonResp getAllFavoritiesForUser(@RequestParam("uid") Integer uid) {
        Integer loginUid = (Integer) currentUser.getUserId();
        CommonResp commonResp = new CommonResp();
        if (Objects.equals(loginUid, uid)) {
            commonResp.setData(favoriteService.getFavorites(uid, true));
        } else {
            commonResp.setData(favoriteService.getFavorites(uid, false));
        }
        return commonResp;
    }

    /**
     * 游客请求某个用户的收藏夹列表（不需要jwt鉴权）
     * @param uid   被查看的用户ID
     * @return  包含收藏夹列表的响应对象
     */
    @GetMapping("/get-all/visitor")
    public CommonResp getAllFavoritiesForVisitor(@RequestParam("uid") Integer uid) {
        CommonResp customResponse = new CommonResp();
        customResponse.setData(favoriteService.getFavorites(uid, false));
        return customResponse;
    }
}
