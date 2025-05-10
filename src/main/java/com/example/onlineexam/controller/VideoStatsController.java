package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.VideoStatsMapper;
import com.example.onlineexam.req.videoStatsReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.videoStatsResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.VideoStatsService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/videoStats")
public class VideoStatsController {
    @Resource
    private VideoStatsService videoStatsService;
    @Autowired
    private VideoStatsMapper videoStatsMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated videoStatsReq videoStatsReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<videoStatsResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<videoStatsResp> data = videoStatsService.list(videoStatsReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody videoStatsReq videoStatsReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        videoStatsService.save(videoStatsReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(videoStatsReq.getVid())) {

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
        videoStatsService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }
}
