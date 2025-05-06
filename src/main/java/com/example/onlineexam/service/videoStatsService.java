package com.example.onlineexam.service;


import com.example.onlineexam.domain.videoStats;
import com.example.onlineexam.domain.videoStatsExample;
import com.example.onlineexam.mapper.videoStatsMapper;
import com.example.onlineexam.req.videoStatsReq;
import com.example.onlineexam.resp.videoStatsResp;
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
public class videoStatsService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(videoStatsService.class);

    @Resource
    public videoStatsMapper videoStatsMapper;

    public PageResp<videoStatsResp> list(videoStatsReq videoStatsReq) {
        //固定写法
        videoStatsExample example = new videoStatsExample();
        //固定写法
        videoStatsExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(videoStatsReq.getPage(), videoStatsReq.getSize());
        //类接收返回的数据
        List<videoStats> sortsList = videoStatsMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<videoStatsResp> data = CopyUtil.copyList(sortsList, videoStatsResp.class);
        //定义分页获取总数
        PageInfo<videoStats> pageInfo = new PageInfo<>(sortsList);
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
        videoStats videoStats = CopyUtil.copy(videoStatsReq, videoStats.class);
        //固定写法
        videoStatsExample example = new videoStatsExample();
        //固定写法
        videoStatsExample.Criteria criteria = example.createCriteria();
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

}
