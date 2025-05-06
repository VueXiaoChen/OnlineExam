package com.example.onlineexam.service;


import com.example.onlineexam.domain.Video;
import com.example.onlineexam.domain.VideoExample;
import com.example.onlineexam.mapper.VideoMapper;
import com.example.onlineexam.req.VideoReq;
import com.example.onlineexam.resp.VideoResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class VideoService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(VideoService.class);

    @Resource
    public VideoMapper videoMapper;

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

}
