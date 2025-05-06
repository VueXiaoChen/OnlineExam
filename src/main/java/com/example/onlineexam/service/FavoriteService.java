package com.example.onlineexam.service;


import com.example.onlineexam.domain.Favorite;
import com.example.onlineexam.domain.FavoriteExample;
import com.example.onlineexam.mapper.FavoriteMapper;
import com.example.onlineexam.req.FavoriteReq;
import com.example.onlineexam.resp.FavoriteResp;
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
public class FavoriteService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(FavoriteService.class);

    @Resource
    public FavoriteMapper favoriteMapper;

    public PageResp<FavoriteResp> list(FavoriteReq favoriteReq) {
        //固定写法
        FavoriteExample example = new FavoriteExample();
        //固定写法
        FavoriteExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(favoriteReq.getPage(), favoriteReq.getSize());
        //类接收返回的数据
        List<Favorite> sortsList = favoriteMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<FavoriteResp> data = CopyUtil.copyList(sortsList, FavoriteResp.class);
        //定义分页获取总数
        PageInfo<Favorite> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<FavoriteResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(FavoriteReq favoriteReq) {
        Favorite favorite = CopyUtil.copy(favoriteReq, Favorite.class);
        //固定写法
        FavoriteExample example = new FavoriteExample();
        //固定写法
        FavoriteExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(favoriteReq.getFid())) {
            favoriteMapper.insertSelective(favorite);
        } else {
            //更新数据
            favoriteMapper.updateByPrimaryKeySelective(favorite);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        favoriteMapper.deleteByPrimaryKey(id);
    }

}
