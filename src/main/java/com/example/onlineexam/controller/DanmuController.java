package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.DanmuMapper;
import com.example.onlineexam.req.DanmuReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.DanmuResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.DanmuService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/danmu")
public class DanmuController {
    @Resource
    private DanmuService danmuService;
    @Autowired
    private DanmuMapper danmuMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated DanmuReq danmuReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<DanmuResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<DanmuResp> data = danmuService.list(danmuReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody DanmuReq danmuReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        danmuService.save(danmuReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(danmuReq.getId())) {

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
        danmuService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }
}
