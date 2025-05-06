package com.example.onlineexam.service;


import com.example.onlineexam.domain.FavoriteVideo;
import com.example.onlineexam.domain.FavoriteVideoExample;
import com.example.onlineexam.mapper.FavoriteVideoMapper;
import com.example.onlineexam.req.FavoriteVideoReq;
import com.example.onlineexam.resp.FavoriteVideoResp;
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
public class FavoriteVideoService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(FavoriteVideoService.class);

    @Resource
    public FavoriteVideoMapper favoriteVideoMapper;

    public PageResp<FavoriteVideoResp> list(FavoriteVideoReq favoriteVideoReq) {
        //固定写法
        FavoriteVideoExample example = new FavoriteVideoExample();
        //固定写法
        FavoriteVideoExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(favoriteVideoReq.getPage(), favoriteVideoReq.getSize());
        //类接收返回的数据
        List<FavoriteVideo> sortsList = favoriteVideoMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<FavoriteVideoResp> data = CopyUtil.copyList(sortsList, FavoriteVideoResp.class);
        //定义分页获取总数
        PageInfo<FavoriteVideo> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<FavoriteVideoResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(FavoriteVideoReq favoriteVideoReq) {
        FavoriteVideo favoriteVideo = CopyUtil.copy(favoriteVideoReq, FavoriteVideo.class);
        //固定写法
        FavoriteVideoExample example = new FavoriteVideoExample();
        //固定写法
        FavoriteVideoExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(favoriteVideoReq.getId())) {
            favoriteVideoMapper.insertSelective(favoriteVideo);
        } else {
            //更新数据
            favoriteVideoMapper.updateByPrimaryKeySelective(favoriteVideo);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        favoriteVideoMapper.deleteByPrimaryKey(id);
    }

}
