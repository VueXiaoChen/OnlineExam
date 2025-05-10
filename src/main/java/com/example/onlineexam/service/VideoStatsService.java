package com.example.onlineexam.service;


import com.example.onlineexam.domain.VideoStats;
import com.example.onlineexam.domain.VideoStatsExample;
import com.example.onlineexam.mapper.VideoStatsMapper;
import com.example.onlineexam.req.videoStatsReq;
import com.example.onlineexam.resp.videoStatsResp;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
public class VideoStatsService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(VideoStatsService.class);

    @Resource
    public VideoStatsMapper videoStatsMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    public PageResp<videoStatsResp> list(videoStatsReq videoStatsReq) {
        //固定写法
        VideoStatsExample example = new VideoStatsExample();
        //固定写法
        VideoStatsExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(videoStatsReq.getPage(), videoStatsReq.getSize());
        //类接收返回的数据
        List<VideoStats> sortsList = videoStatsMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<videoStatsResp> data = CopyUtil.copyList(sortsList, videoStatsResp.class);
        //定义分页获取总数
        PageInfo<VideoStats> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<videoStatsResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(videoStatsReq videoStatsReq) {
        VideoStats videoStats = CopyUtil.copy(videoStatsReq, VideoStats.class);
        //固定写法
        VideoStatsExample example = new VideoStatsExample();
        //固定写法
        VideoStatsExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(videoStatsReq.getVid())) {
            videoStatsMapper.insertSelective(videoStats);
        } else {
            //更新数据
            videoStatsMapper.updateByPrimaryKeySelective(videoStats);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        videoStatsMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新指定字段
     * @param vid   视频ID
     * @param column    对应数据库的列名
     * @param increase  是否增加，true则增加 false则减少
     * @param count 增减数量 一般是1，只有投币可以加2
     */

    public void updateStats(Integer vid, String column, boolean increase, Integer count) {
        videoStatsMapper.updateStatsDynamic(vid, column, count, increase);
        redisUtils.delValue("VideoStats:" + vid);
    }

    /**
     * 根据视频ID查询视频常变数据
     * @param vid 视频ID
     * @return 视频数据统计
     */
    public VideoStats getVideoStatsById(Integer vid) {
        // 1. 先尝试从Redis获取
        VideoStats videoStats = redisUtils.getObject("videoStats:" + vid, VideoStats.class);
        if (videoStats != null) {
            return videoStats;  // 缓存命中直接返回
        }
        // 2. 缓存未命中则查数据库
        videoStats = videoStatsMapper.selectByPrimaryKey(vid);
        if (videoStats == null) {
            return null;  // 数据库也不存在
        }
        // 3. 同步更新缓存（不再异步）
        redisUtils.setExObjectValue("videoStats:" + vid, videoStats, 3600, TimeUnit.SECONDS);  // 建议添加过期时间
        return videoStats;
    }

}
