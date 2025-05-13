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
import com.example.onlineexam.util.CurrentUser;
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
    private CurrentUser currentUser;


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
     * 查询当前视频准备要上传的分片序号
     * @param hash 视频的hash值
     * @return
     */
    @GetMapping("/ask-chunk")
    public CommonResp askChunk(@RequestParam("hash") String hash) {
        return videoService.askCurrentChunk(hash);
    }

    /**
     * 上传分片
     * @param chunk 分片的blob文件
     * @param hash  视频的hash值
     * @param index 当前分片的序号
     * @return
     * @throws IOException
     */
    @PostMapping("/upload-chunk")
    public CommonResp uploadChunk(@RequestParam("chunk") MultipartFile chunk,
                                      @RequestParam("hash") String hash,
                                      @RequestParam("index") Integer index) throws IOException {
        try {
            return videoService.uploadChunk(chunk, hash, index);
        } catch (Exception e) {
            e.printStackTrace();
            CommonResp commonResp =new CommonResp();
            commonResp.setCode(500);
            commonResp.setMessage("分片上传失败");
            commonResp.setData(null);
            return commonResp;
        }

    }

    /**
     * 取消上传
     * @param hash 视频的hash值
     * @return
     */
    @GetMapping("/cancel-upload")
    public CommonResp cancelUpload(@RequestParam("hash") String hash) {
        return videoService.cancelUpload(hash);
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
        CommonResp commonResp = new CommonResp();
        try {
            commonResp.setCode(200);
            commonResp.setMessage("成功上传");
            commonResp.setData(videoService.addVideo(cover, videoUploadInfoDTO));
            return commonResp;
        } catch (Exception e) {
            e.printStackTrace();
            commonResp.setCode(500);
            commonResp.setMessage("封面上传失败");
            commonResp.setData(null);
            return commonResp;
        }
    }

    /**
     * 审核 查询对应状态的视频数量
     * @param status 状态 0待审核 1通过 2未通过
     * @return
     */
    @GetMapping("/review/total")
    public CommonResp getTotal(@RequestParam("vstatus") Integer status) {
        CommonResp commonResp = new CommonResp();
        commonResp.setCode(200);
        commonResp.setMessage("查询成功");
        commonResp.setData(videoService.getTotalByStatus(status));
        return commonResp;
    }

    /**
     * 审核 分页查询对应状态视频
     * @param status 状态 0待审核 1通过 2未通过
     * @param page  当前页
     * @param quantity  每页的数量
     * @return
     */
    @GetMapping("/review/getpage")
    public CommonResp getreviewVideos(@RequestParam("vstatus") Integer status,
                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "quantity", defaultValue = "10") Integer quantity) {

        CommonResp commonResp = new CommonResp();
        commonResp.setCode(200);
        commonResp.setMessage("查询成功");
        commonResp.setData(videoService.getVideosByPage(status, page, quantity));
        return commonResp;
    }

    /**
     * 审核 查询单个视频详情
     * @param vid 视频id
     * @return
     */
    @GetMapping("/review/getone")
    public CommonResp getOnereviewVideo(@RequestParam("vid") Integer vid) {
        CommonResp customResponse = new CommonResp();
        if (!currentUser.isAdmin()) {
            customResponse.setCode(403);
            customResponse.setMessage("您不是管理员，无权访问");
            return customResponse;
        }
        Map<String, Object> map = videoService.getVideoWithDataById(vid);
        customResponse.setData(map);    // 如果是是空照样返回，前端自动处理
        return customResponse;
    }

    /**
     * 更新视频状态，包括过审、不通过、删除，其中审核相关需要管理员权限，删除可以是管理员或者投稿用户
     * @param vid 视频ID
     * @param status 要修改的状态，1通过 2不通过 3删除
     * @return 无data返回 仅返回响应
     */
    @PostMapping("/change/status")
    public CommonResp updateStatus(@RequestParam("vid") Integer vid,
                                   @RequestParam("status") Integer status) {
        CommonResp commonResp = new CommonResp();
        try {
            commonResp.setCode(200);
            commonResp.setMessage("修改成功");
            commonResp.setData(videoService.updateVideoStatus(vid, status));
            return commonResp;
        } catch (Exception e) {
            e.printStackTrace();
            commonResp.setCode(500);
            commonResp.setMessage("操作失败");
            commonResp.setData(null);
            return commonResp;
        }
    }
}
