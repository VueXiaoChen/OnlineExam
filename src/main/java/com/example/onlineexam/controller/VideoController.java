package com.example.onlineexam.controller;


import com.example.onlineexam.domain.Video;
import com.example.onlineexam.mapper.VideoMapper;
import com.example.onlineexam.req.VideoReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.VideoResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.VideoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/video")
public class VideoController {
    @Resource
    private VideoService videoService;
    @Autowired
    private VideoMapper videoMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated VideoReq videoReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<VideoResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<VideoResp> data = videoService.list(videoReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody VideoReq videoReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        videoService.save(videoReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(videoReq.getVid())) {

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
        videoService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }
    /**
     * 获取单条视频的信息
     * @param vid   视频vid
     * @return  视频信息
     */
    @GetMapping("/getone/{vid}")
    public CommonResp getOneVideo(@PathVariable("vid") Integer vid) {
        CommonResp commonResp = new CommonResp();
        Map<String, Object> map = videoService.getVideoWithDataById(vid);
        if (map == null) {
            commonResp.setCode(404);
            commonResp.setMessage("没找到个视频QAQ");
            return commonResp;
        }
        Video video = (Video) map.get("video");
        if (video.getStatus() != 1) {
            commonResp.setCode(404);
            commonResp.setMessage("没找到个视频QAQ");
            return commonResp;
        }
        commonResp.setData(map);
        return commonResp;
    }
}
