package com.example.onlineexam.service;


import com.example.onlineexam.domain.UserVideo;
import com.example.onlineexam.domain.UserVideoExample;
import com.example.onlineexam.mapper.UserVideoMapper;
import com.example.onlineexam.req.UserVideoReq;
import com.example.onlineexam.resp.UserVideoResp;
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
public class UserVideoService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(UserVideoService.class);

    @Resource
    public UserVideoMapper userVideoMapper;

    public PageResp<UserVideoResp> list(UserVideoReq userVideoReq) {
        //固定写法
        UserVideoExample example = new UserVideoExample();
        //固定写法
        UserVideoExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(userVideoReq.getPage(), userVideoReq.getSize());
        //类接收返回的数据
        List<UserVideo> sortsList = userVideoMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<UserVideoResp> data = CopyUtil.copyList(sortsList, UserVideoResp.class);
        //定义分页获取总数
        PageInfo<UserVideo> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<UserVideoResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(UserVideoReq userVideoReq) {
        UserVideo userVideo = CopyUtil.copy(userVideoReq, UserVideo.class);
        //固定写法
        UserVideoExample example = new UserVideoExample();
        //固定写法
        UserVideoExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(userVideoReq.getId())) {
            userVideoMapper.insertSelective(userVideo);
        } else {
            //更新数据
            userVideoMapper.updateByPrimaryKeySelective(userVideo);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        userVideoMapper.deleteByPrimaryKey(id);
    }



}
