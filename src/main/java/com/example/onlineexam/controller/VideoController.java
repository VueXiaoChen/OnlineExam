package com.example.onlineexam.controller;



import com.example.onlineexam.domain.Video;
import com.example.onlineexam.domain.VideoStats;
import com.example.onlineexam.domain.VideoUploadInfoDTO;
import com.example.onlineexam.mapper.VideoMapper;
import com.example.onlineexam.mapper.VideoStatsMapper;
import com.example.onlineexam.req.VideoReq;
import com.example.onlineexam.req.VideoUploadInfoReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.VideoResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.VideoService;
import com.example.onlineexam.util.RedisUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@RestController
@RequestMapping("/video")
public class VideoController {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(VideoController.class);

    @Resource
    private VideoService videoService;


    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

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
    /**
     * 添加视频投稿
     * @param cover 封面文件
     * @param hash  视频的hash值
     * @param title 投稿标题
     * @param type  视频类型 1自制 2转载
     * @param auth  作者声明 0不声明 1未经允许禁止转载
     * @param duration 视频总时长
     * @param mcid  主分区ID
     * @param scid  子分区ID
     * @param tags  标签
     * @param descr 简介
     * @return  响应对象
     */
    @PostMapping("/add")
    public CommonResp addVideo(@RequestParam("cover") MultipartFile cover,
                               @RequestParam("hash") String hash,
                               @RequestParam("title") String title,
                               @RequestParam("type") Integer type,
                               @RequestParam("auth") Integer auth,
                               @RequestParam("duration") Double duration,
                               @RequestParam("mcid") String mcid,
                               @RequestParam("scid") String scid,
                               @RequestParam("tags") String tags,
                               @RequestParam("descr") String descr) {
        VideoUploadInfoDTO videoUploadInfoDTO = new VideoUploadInfoDTO(null, hash, title, type, auth, duration, mcid, scid, tags, descr, null);
        try {
            return videoService.addVideo(cover, videoUploadInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            CommonResp commonResp = new CommonResp();
            commonResp.setCode(500);
            commonResp.setMessage("封面上传失败");
            commonResp.setData(null);
            return commonResp;
        }
    }

}
