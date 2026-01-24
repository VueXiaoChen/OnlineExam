package com.example.onlineexam.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.example.onlineexam.util.CurrentUser;
import com.example.onlineexam.util.OssUtil;
import com.example.onlineexam.util.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VideoService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(VideoService.class);

    @Value("${directory.cover}")
    private String COVER_DIRECTORY;   // 投稿封面存储目录
    @Value("${directory.video}")
    private String VIDEO_DIRECTORY;   // 投稿视频存储目录
    @Value("${directory.chunk}")
    private String CHUNK_DIRECTORY;   // 分片存储目录

    @Autowired
    private CurrentUser currentUser;

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
        //根据用户ID
        if (!ObjectUtils.isEmpty(videoReq.getUid())) {
            criteria.andUidEqualTo(videoReq.getUid());
        }
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

    public PageResp<Map<String, Object>> getRandomVideos(VideoReq videoReq) {
        VideoExample example = new VideoExample();
        VideoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(3);
        example.setOrderByClause("RAND()");

        PageHelper.startPage(videoReq.getPage(), videoReq.getSize());
        List<Video> videoList = videoMapper.selectByExample(example);
        PageInfo<Video> pageInfo = new PageInfo<>(videoList);

        if (videoList.isEmpty()) {
            PageResp<Map<String, Object>> pageResp = new PageResp<>();
            pageResp.setTotal(0);
            pageResp.setList(Collections.emptyList());
            return pageResp;
        }

        List<Map<String, Object>> mapList = videoList.stream()
                .map(video -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("video", video);
                    map.put("user", userService.getUserById(video.getUid()));
                    map.put("stats", videoStatsService.getVideoStatsById(video.getVid()));
                    map.put("category", categoryService.getCategoryById(video.getMcId(), video.getScId()));
                    return map;
                })
                .collect(Collectors.toList());

        PageResp<Map<String, Object>> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(mapList);
        return pageResp;
    }





    /**
     * 获取视频下一个还没上传的分片序号
     * @param hash 视频的hash值
     * @return CustomResponse对象
     */
    public CommonResp askCurrentChunk(String hash) {
        CommonResp customResponse = new CommonResp();
        // 查询本地
        // 获取分片文件的存储目录
        File chunkDir = new File(CHUNK_DIRECTORY);
        // 获取存储在目录中的所有分片文件
        File[] chunkFiles = chunkDir.listFiles((dir, name) -> name.startsWith(hash + "-"));
        // 返回还没上传的分片序号
        if (chunkFiles == null) {
            customResponse.setData(0);
        } else {
            customResponse.setData(chunkFiles.length);
        }
        // 查询OSS当前存在的分片数量，即前端要上传的分片序号，建议分布式系统才使用OSS存储分片，单体系统本地存储分片效率更高
//        int counts = ossUploadUtil.countFiles("chunk/", hash + "-");
//        customResponse.setData(counts);
        return customResponse;
    }
    /**
     * 上传单个视频分片，当前上传到阿里云对象存储
     * @param chunk 分片文件
     * @param hash  视频的hash值
     * @param index 当前分片的序号
     * @return  CustomResponse对象
     * @throws IOException
     */
    public CommonResp uploadChunk(MultipartFile chunk, String hash, Integer index) throws IOException {
        CommonResp customResponse = new CommonResp();
        // 构建分片文件名
        String chunkFileName = hash + "-" + index;

        // 存储到本地
        // 构建分片文件的完整路径
        String chunkFilePath = Paths.get(CHUNK_DIRECTORY, chunkFileName).toString();
        // 检查是否已经存在相同的分片文件
        File chunkFile = new File(chunkFilePath);
        if (chunkFile.exists()) {
            LOG.info("分片{}已存在",chunkFilePath);
            customResponse.setCode(500);
            customResponse.setMessage("已存在分片文件");
            return customResponse;
        }
        // 保存分片文件到指定目录
        chunk.transferTo(chunkFile);

        // 存储到OSS，建议分布式系统才使用OSS存储分片，单体系统本地存储分片效率更高
//        try {
//            boolean flag = ossUploadUtil.uploadChunk(chunk, chunkFileName);
//            if (!flag) {
//                log.warn("分片 " + chunkFileName + " 已存在");
//                customResponse.setCode(500);
//                customResponse.setMessage("已存在分片文件");
//            }
//        } catch (IOException ioe) {
//            log.error("读取分片文件数据流时出错了");
//        }

        // 返回成功响应
        return customResponse;
    }



    /**
     * 取消上传并且删除该视频的分片文件
     * @param hash 视频的hash值
     * @return CustomResponse对象
     */
    public CommonResp cancelUpload(String hash) {

        // 删除本地分片文件
        // 获取分片文件的存储目录
        File chunkDir = new File(CHUNK_DIRECTORY);
        // 获取存储在目录中的所有分片文件
        File[] chunkFiles = chunkDir.listFiles((dir, name) -> name.startsWith(hash + "-"));
//        System.out.println("检索到要删除的文件数: " + chunkFiles.length);
        // 删除全部分片文件
        if (chunkFiles != null && chunkFiles.length > 0) {
            for (File chunkFile : chunkFiles) {
                chunkFile.delete(); // 删除分片文件
            }
        }
//        System.out.println("删除分片完成");

        // 删除OSS上的分片文件，建议分布式系统才使用OSS存储分片，单体系统本地存储分片效率更高
//        ossUploadUtil.deleteFiles("chunk/", hash + "-");

        // 不管删没删成功 返回成功响应
        return new CommonResp();
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


    /**
     * 查询对应状态的视频数量
     * @param status 状态 0审核中 1通过审核 2打回整改（指投稿信息不符） 3视频违规删除（视频内容违规）
     * @return 包含视频数量的CustomResponse对象
     */

    public CommonResp getTotalByStatus(Integer status) {
        CommonResp customResponse = new CommonResp();
        if (!currentUser.isAdmin()) {
            customResponse.setCode(403);
            customResponse.setMessage("您不是管理员，无权访问");
            return customResponse;
        }
        Long total = redisUtils.scard("video_status:" + status);
        customResponse.setData(total);
        return customResponse;
    }

    /**
     * 获取分页对应状态的视频
     * @return CustomResponse对象，包含符合条件的视频列表
     */

    public CommonResp getVideosByPage(Integer status, Integer page, Integer quantity) {
        CommonResp customResponse = new CommonResp();
        if (!currentUser.isAdmin()) {
            customResponse.setCode(403);
            customResponse.setMessage("您不是管理员，无权访问");
            return customResponse;
        }
        // 从 redis 获取待审核的视频id集合，为了提升效率就不遍历数据库了，前提得保证 Redis 没崩，数据一致性采用定时同步或者中间件来保证
        Set<Object> set = redisUtils.getMembers("video_status:" + status);
        if (set != null && set.size() != 0) {
            // 如果集合不为空，则在数据库主键查询，并且返回没有被删除的视频
            List<Map<String, Object>> mapList = getVideosWithDataByIds(set, page, quantity);
            customResponse.setData(mapList);
        }
        return customResponse;
    }
    /**
     * 更新视频状态，包括过审、不通过、删除，其中审核相关需要管理员权限，删除可以是管理员或者投稿用户
     * @param vid   视频ID
     * @param status 要修改的状态，1通过 2不通过 3删除
     * @return 无data返回，仅返回响应信息
     */

    @Transactional
    public CommonResp updateVideoStatus(Integer vid, Integer status) throws IOException {
        CommonResp customResponse = new CommonResp();
        Object userId = currentUser.getUserId();
        if (status == 1 || status == 2) {
            // Admin check for status 1/2
            if (!currentUser.isAdmin()) {
                customResponse.setCode(403);
                customResponse.setMessage("您不是管理员，无权访问");
                return customResponse;
            }

            // Query video (exclude status=3)
            VideoExample example = new VideoExample();
            example.createCriteria()
                    .andVidEqualTo(vid)
                    .andStatusNotEqualTo(3); // Exclude deleted videos
            List<Video> videolist = videoMapper.selectByExample(example);
            Video video =videolist.get(0);
            if (video == null) {
                customResponse.setCode(404);
                customResponse.setMessage("视频不见了QAQ");
                return customResponse;
            }

            Integer lastStatus = video.getStatus();
            video.setStatus(status); // Set new status (1 or 2)

            // Update video status
            VideoExample updateExample = new VideoExample();
            updateExample.createCriteria().andVidEqualTo(vid);

            Video updatedVideo = new Video();
            updatedVideo.setStatus(status);
            if (status == 1) {
                updatedVideo.setUploadDate(new Date()); // Set upload date for approved videos
            }

            int flag = videoMapper.updateByExampleSelective(updatedVideo, updateExample);

            if (flag > 0) {
                // Sync Redis
                redisUtils.delMember("video_status:" + lastStatus, vid);
                redisUtils.addMember("video_status:" + status, vid);
                redisUtils.delValue("video:" + vid);

                if (status == 1) {
                    redisUtils.zset("user_video_upload:" + video.getUid(), video.getVid());
                } else {
                    redisUtils.zsetDelMember("user_video_upload:" + video.getUid(), video.getVid());
                }
                return customResponse;
            } else {
                customResponse.setCode(500);
                customResponse.setMessage("更新状态失败");
                return customResponse;
            }
        } else if (status == 3) {
            // Query video (exclude status=3)
            VideoExample example = new VideoExample();
            example.createCriteria()
                    .andVidEqualTo(vid)
                    .andStatusNotEqualTo(3);

            List<Video> videolist = videoMapper.selectByExample(example);
            Video video =videolist.get(0);

            if (video == null) {
                customResponse.setCode(404);
                customResponse.setMessage("视频不见了QAQ");
                return customResponse;
            }

            // Permission check: owner or admin
            if (!Objects.equals(userId, video.getUid()) && !currentUser.isAdmin()) {
                customResponse.setCode(403);
                customResponse.setMessage("您没有权限删除视频");
                return customResponse;
            }

            // Update video status to 3 (deleted)
            VideoExample updateExample = new VideoExample();
            updateExample.createCriteria().andVidEqualTo(vid);

            Video updatedVideo = new Video();
            updatedVideo.setStatus(3);
            updatedVideo.setDeleteDate(new Date());

            int flag = videoMapper.updateByExampleSelective(updatedVideo, updateExample);

            if (flag > 0) {
                // Cleanup Redis
                redisUtils.delMember("video_status:" + video.getStatus(), vid);
                redisUtils.delValue("video:" + vid);
                redisUtils.delValue("danmu_idset:" + vid);
                redisUtils.zsetDelMember("user_video_upload:" + video.getUid(), video.getVid());

                // Async OSS cleanup
                String videoPrefix = video.getVideoUrl().split("aliyuncs.com/")[1];
                String coverPrefix = video.getCoverUrl().split("aliyuncs.com/")[1];
                CompletableFuture.runAsync(() -> ossUtil.deleteFiles(videoPrefix), taskExecutor);
                CompletableFuture.runAsync(() -> ossUtil.deleteFiles(coverPrefix), taskExecutor);

                // Async comment cache cleanup
                CompletableFuture.runAsync(() -> {
                    Set<Object> commentIds = redisUtils.zReverange("comment_video:" + vid, 0, -1);
                    List<String> keysToDelete = new ArrayList<>();
                    commentIds.forEach(id -> keysToDelete.add("comment_reply:" + id));
                    keysToDelete.add("comment_video:" + vid);
                    redisUtils.delValues(keysToDelete);
                }, taskExecutor);
                return customResponse;
            } else {
                customResponse.setCode(500);
                customResponse.setMessage("更新状态失败");
                return customResponse;
            }
        }
        // Fallback for invalid status
        customResponse.setCode(500);
        customResponse.setMessage("更新状态失败");
        return customResponse;
    }
}
