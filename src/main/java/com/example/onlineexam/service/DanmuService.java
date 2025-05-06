package com.example.onlineexam.service;


import com.example.onlineexam.domain.Danmu;
import com.example.onlineexam.domain.DanmuExample;
import com.example.onlineexam.mapper.DanmuMapper;
import com.example.onlineexam.req.DanmuReq;
import com.example.onlineexam.resp.DanmuResp;
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
public class DanmuService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(DanmuService.class);

    @Resource
    public DanmuMapper danmuMapper;

    public PageResp<DanmuResp> list(DanmuReq danmuReq) {
        //固定写法
        DanmuExample example = new DanmuExample();
        //固定写法
        DanmuExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(danmuReq.getPage(), danmuReq.getSize());
        //类接收返回的数据
        List<Danmu> sortsList = danmuMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<DanmuResp> data = CopyUtil.copyList(sortsList, DanmuResp.class);
        //定义分页获取总数
        PageInfo<Danmu> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<DanmuResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(DanmuReq danmuReq) {
        Danmu danmu = CopyUtil.copy(danmuReq, Danmu.class);
        //固定写法
        DanmuExample example = new DanmuExample();
        //固定写法
        DanmuExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(danmuReq.getId())) {
            danmuMapper.insertSelective(danmu);
        } else {
            //更新数据
            danmuMapper.updateByPrimaryKeySelective(danmu);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        danmuMapper.deleteByPrimaryKey(id);
    }

}
