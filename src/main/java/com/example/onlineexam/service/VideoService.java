package com.example.onlineexam.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlineexam.domain.Video;
import com.example.onlineexam.domain.VideoExample;
import com.example.onlineexam.domain.VideoStats;
import com.example.onlineexam.domain.VideoUploadInfoDTO;
import com.example.onlineexam.mapper.VideoMapper;
import com.example.onlineexam.mapper.VideoStatsMapper;
import com.example.onlineexam.req.VideoReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.VideoResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.example.onlineexam.util.OssUtil;
import com.example.onlineexam.util.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VideoService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(VideoService.class);

    @Resource
    public VideoMapper videoMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private VideoStatsService videoStatsService;

    @Autowired
    private RedisUtils redisUtils;


    @Autowired
    private OssUtil ossUtil;


    @Resource
    public VideoStatsMapper videoStatsMapper;

    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    public PageResp<VideoResp> list(VideoReq videoReq) {
        //固定写法
        VideoExample example = new VideoExample();
        //固定写法
        VideoExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(videoReq.getPage(), videoReq.getSize());
        //类接收返回的数据
        List<Video> sortsList = videoMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<VideoResp> data = CopyUtil.copyList(sortsList, VideoResp.class);
        //定义分页获取总数
        PageInfo<Video> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<VideoResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(VideoReq videoReq) {
        Video video = CopyUtil.copy(videoReq, Video.class);
        //固定写法
        VideoExample example = new VideoExample();
        //固定写法
        VideoExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(videoReq.getVid())) {
            videoMapper.insertSelective(video);
        } else {
            //更新数据
            videoMapper.updateByPrimaryKeySelective(video);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        videoMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id分页获取视频信息，包括用户和分区信息
     * @param set   要查询的视频id集合
     * @param index 分页页码 为空默认是1
     * @param quantity  每一页查询的数量 为空默认是10
     * @return  包含用户信息、分区信息、视频信息的map列表
     */
    public List<Map<String, Object>> getVideosWithDataByIds(Set<Object> set,Integer index, Integer quantity) {
        if (index == null) {
            index = 1;
        }
        if (quantity == null) {
            quantity = 10;
        }
        int startIndex = (index - 1) * quantity;
        int endIndex = startIndex + quantity;

        // 检查数据是否足够满足分页查询
        if (startIndex > set.size()) {
            return Collections.emptyList();
        }
        // 转换ID集合
        // 类型安全转换
        List<Integer> idList = set.stream()
                .filter(obj -> obj instanceof Integer)
                .map(obj -> (Integer) obj)
                .collect(Collectors.toList());
        endIndex = Math.min(endIndex, idList.size());
        List<Integer> sublist = idList.subList(startIndex, endIndex);

        // 使用Example进行查询
        VideoExample example = new VideoExample();
        VideoExample.Criteria criteria = example.createCriteria();
        criteria.andVidIn(sublist)  // 相当于QueryWrapper的in
                .andStatusNotEqualTo(3);  // 相当于ne

        List<Video> videoList = videoMapper.selectByExample(example);
        if (videoList.isEmpty()) {
            return Collections.emptyList();
        }

        // 并行处理
        List<Map<String, Object>> mapList = videoList.parallelStream()
                .map(video -> {
                    Map<String, Object> map = new ConcurrentHashMap<>();  // 线程安全的Map
                    map.put("video", video);

                    CompletableFuture<Void> userFuture = CompletableFuture.runAsync(() -> {
                        map.put("user", userService.getUserById(video.getUid()));
                        map.put("stats", videoStatsService.getVideoStatsById(video.getVid()));
                    }, taskExecutor);

                    CompletableFuture<Void> categoryFuture = CompletableFuture.runAsync(() -> {
                        map.put("category", categoryService.getCategoryById(video.getMcId(), video.getScId()));
                    }, taskExecutor);

                    // 等待所有任务完成
                    CompletableFuture.allOf(userFuture, categoryFuture).join();

                    return map;
                })
                .collect(Collectors.toList());
        return mapList;
    }


    /**
     * 根据vid查询单个视频信息，包含用户信息和分区信息
     * @param vid 视频ID
     * @return 包含用户信息、分区信息、视频信息的map
     */
    public Map<String, Object> getVideoWithDataById(Integer vid) {
        try {
            Map<String, Object> map = new ConcurrentHashMap<>(); // 改用线程安全的Map
            // 1. 先查询Redis
            Video video = redisUtils.getObject("video:" + vid, Video.class);
            LOG.info("video:{}",video);
            // 2. Redis未命中则查数据库
            if (video == null) {
                VideoExample example = new VideoExample();
                example.createCriteria()
                        .andVidEqualTo(vid)
                        .andStatusNotEqualTo(3); // status != 3

                List<Video> videos = videoMapper.selectByExample(example);
                video = videos.isEmpty() ? null : videos.get(0);

                if (video != null) {
                    // 异步更新Redis
                    Video finalVideo = video;
                    CompletableFuture.runAsync(() -> {
                        redisUtils.setExObjectValue("video:" + vid, finalVideo, 3600, TimeUnit.SECONDS); // 默认1小时
                    }, taskExecutor);
                } else {
                    return null;
                }
            }
            // 3. 并行查询用户信息和分区信息
            Video finalVideo = video;
            map.put("video", finalVideo);
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(() -> {
                        try {
                            map.put("user", userService.getUserById(finalVideo.getUid()));
                            map.put("stats", videoStatsService.getVideoStatsById(finalVideo.getVid()));
                            LOG.info("执行到此处了么：{}", map);
                        } catch (Exception e) {
                            LOG.info("查询用户或统计信息失败", e);
                        }
                    }, taskExecutor),
                    CompletableFuture.runAsync(() -> {
                        try {
                            map.put("category", categoryService.getCategoryById(
                                    finalVideo.getMcId(),
                                    finalVideo.getScId()
                            ));
                        } catch (Exception e) {
                            LOG.info("查询分类信息失败", e);
                        }
                    }, taskExecutor)
            ).join();
            return map;
        } catch (Exception e) {
            LOG.info("获取视频详情失败", e);
            return null;
        }
    }
    /**
     * 接收前端提供的视频信息，包括封面文件和稿件的其他信息，保存完封面后将信息发送到消息队列，并返回投稿成功响应
     * @param cover 封面图片文件
     * @param videoUploadInfoDTO 存放投稿信息的 VideoUploadInfo 对象
     * @return  CustomResponse对象
     * @throws IOException
     */
    public CommonResp addVideo(MultipartFile cover, VideoUploadInfoDTO videoUploadInfoDTO) throws IOException {
        //TODO 此处的redis存取userid还需要改进 先用着
        Object uid =  redisUtils.getValue("usersid");
        //Integer loginUserId = currentUser.getUserId();
        // 值的判定 虽然前端会判 防止非法请求 不过数据库也写不进去 但会影响封面保存
        if (videoUploadInfoDTO.getTitle().trim().length() == 0) {
            CommonResp commonResp = new CommonResp();
            commonResp.setMessage("标题不能为空");
            commonResp.setCode(500);
            commonResp.setData("");
        }
        if (videoUploadInfoDTO.getTitle().length() > 80) {
            CommonResp commonResp = new CommonResp();
            commonResp.setMessage("标题不能超过80字");
            commonResp.setCode(500);
            commonResp.setData("");
        }
        if (videoUploadInfoDTO.getDescr().length() > 2000) {
            CommonResp commonResp = new CommonResp();
            commonResp.setMessage("简介太长啦");
            commonResp.setCode(500);
            commonResp.setData("");
        }
        // 保存封面到本地
//        try {
//            // 获取当前时间戳
//            long timestamp = System.currentTimeMillis();
//            String fileName = timestamp + videoUploadInfo.getHash() + ".jpg";
//            String path = Paths.get(COVER_DIRECTORY, fileName).toString();
//            File file = new File(path);
////            System.out.println(file.getAbsolutePath());
//            cover.transferTo(file);
//            videoUploadInfo.setCoverUrl(file.getAbsolutePath());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        // 保存封面到OSS，返回URL
        String coverUrl = ossUtil.uploadImage(cover, "cover");
        // 将投稿信息封装
        videoUploadInfoDTO.setCoverUrl(coverUrl);
        videoUploadInfoDTO.setUid((Integer) uid);
//        mergeChunks(videoUploadInfo);   // 这里是串行操作，严重影响性能
        // 发送到消息队列等待监听写库
        // 序列化 videoUploadInfo 对象为 String， 往 rabbitmq 中发送投稿信息，也可以使用多线程异步
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonPayload = objectMapper.writeValueAsString(videoUploadInfo);
//        rabbitTemplate.convertAndSend("direct_upload_exchange", "videoUpload", jsonPayload);

        // 使用异步线程最佳，因为监听rabbitmq的始终是单线程，高峰期会堆积阻塞
        CommonResp commonResp = new CommonResp();
        CompletableFuture.runAsync(() -> {
            try {
                mergeChunks(videoUploadInfoDTO);
                commonResp.setMessage("上传成功");
                commonResp.setCode(200);
                commonResp.setData("");
            } catch (IOException e) {
                LOG.info("合并视频写库时出错了");
                e.printStackTrace();
            }
        }, taskExecutor);
        return commonResp;
    }
    public void mergeChunks(VideoUploadInfoDTO vui) throws IOException {
        String url; // 视频最终的URL
        // 合并到本地
//        // 获取分片文件的存储目录
//        File chunkDir = new File(CHUNK_DIRECTORY);
//        // 获取当前时间戳
//        long timestamp = System.currentTimeMillis();
//        // 构建最终文件名，将时间戳加到文件名开头
//        String finalFileName = timestamp + vui.getHash() + ".mp4";
//        // 构建最终文件的完整路径
//        String finalFilePath = Paths.get(VIDEO_DIRECTORY, finalFileName).toString();
//        // 创建最终文件
//        File finalFile = new File(finalFilePath);
//        // 获取所有对应分片文件
//        File[] chunkFiles = chunkDir.listFiles((dir, name) -> name.startsWith(vui.getHash() + "-"));
//        if (chunkFiles != null && chunkFiles.length > 0) {
//            // 使用流操作对文件名进行排序，防止出现先合并 10 再合并 2
//            List<File> sortedChunkFiles = Arrays.stream(chunkFiles)
//                    .sorted(Comparator.comparingInt(file -> Integer.parseInt(file.getName().split("-")[1])))
//                    .collect(Collectors.toList());
//            try {
////                System.out.println("正在合并视频");
//                // 合并分片文件
//                for (File chunkFile : sortedChunkFiles) {
//                    byte[] chunkBytes = FileUtils.readFileToByteArray(chunkFile);
//                    FileUtils.writeByteArrayToFile(finalFile, chunkBytes, true);
//                    chunkFile.delete(); // 删除已合并的分片文件
//                }
////                System.out.println("合并完成!");
//                // 获取绝对路径，仅限本地服务器
//                url = finalFile.getAbsolutePath();
////                System.out.println(url);
//            } catch (IOException e) {
//                // 处理合并失败的情况 重新入队等
//                log.error("合并视频失败");
//                throw e;
//            }
//        } else {
//            // 没有找到分片文件 发通知用户投稿失败
//            log.error("未找到分片文件 " + vui.getHash());
//            return;
//        }
        //获取文件的后缀
        String filename = vui.getCoverUrl();
        String extension = FilenameUtils.getExtension(filename);
        if(extension.equals("txt")){
            url = filename;
            LOG.info("传过来的是txt文件");
        }else if(extension.equals("jpg")){
            url = filename;
            LOG.info("传过来的是是jpg文件");
        }else if(extension.equals("png")){
            url = filename;
            LOG.info("传过来的是是png文件");
        }else{
            url = ossUtil.appendUploadVideo(vui.getHash());
            if (url == null) {
                return;
            }
        }
        // 合并到OSS，并返回URL地址

        // 存入数据库
        Date now = new Date();
        Video video = new Video();
        video.setUid(vui.getUid());
        video.setTitle(vui.getTitle());
        video.setType(vui.getType());
        video.setAuth(vui.getAuth());
        video.setDuration(vui.getDuration());
        video.setMcId(vui.getMcId());
        video.setScId(vui.getScId());
        video.setTags(vui.getTags());
        video.setDescr(vui.getDescr());
        video.setCoverUrl(vui.getCoverUrl());
        video.setVideoUrl(url);
        video.setStatus(0);
        video.setUploadDate(now);
        video.setDeleteDate(null);
        videoMapper.insertSelective(video);
        VideoStats videoStats = new VideoStats(video.getVid(),0,0,0,0,0,0,0,0);
        videoStatsMapper.insertSelective(videoStats);
        LOG.info("用来测试的:{}",videoStats);
        //esUtil.addVideo(video);
        CompletableFuture.runAsync(() -> redisUtils.setExObjectValue("video:" + video.getVid(), video), taskExecutor);
        CompletableFuture.runAsync(() -> redisUtils.addMember("video_status:0", video.getVid()), taskExecutor);
        CompletableFuture.runAsync(() -> redisUtils.setExObjectValue("videoStats:" + video.getVid(), videoStats), taskExecutor);

        // 其他逻辑 （发送消息通知写库成功）
    }
}
