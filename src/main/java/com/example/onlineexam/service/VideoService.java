package com.example.onlineexam.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlineexam.domain.Video;
import com.example.onlineexam.domain.VideoExample;
import com.example.onlineexam.mapper.VideoMapper;
import com.example.onlineexam.req.VideoReq;
import com.example.onlineexam.resp.VideoResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.example.onlineexam.util.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
}
