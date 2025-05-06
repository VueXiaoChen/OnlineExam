package com.example.onlineexam.service;


import com.example.onlineexam.domain.User;
import com.example.onlineexam.domain.UserExample;
import com.example.onlineexam.mapper.UserMapper;
import com.example.onlineexam.req.UserReq;
import com.example.onlineexam.resp.UserResp;
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
public class UserService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(UserService.class);

    @Resource
    public UserMapper userMapper;

    public PageResp<UserResp> list(UserReq userReq) {
        //固定写法
        UserExample example = new UserExample();
        //固定写法
        UserExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(userReq.getPage(), userReq.getSize());
        //类接收返回的数据
        List<User> sortsList = userMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<UserResp> data = CopyUtil.copyList(sortsList, UserResp.class);
        //定义分页获取总数
        PageInfo<User> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<UserResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(UserReq userReq) {
        User user = CopyUtil.copy(userReq, User.class);
        //固定写法
        UserExample example = new UserExample();
        //固定写法
        UserExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(userReq.getUid())) {
            userMapper.insertSelective(user);
        } else {
            //更新数据
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        userMapper.deleteByPrimaryKey(id);
    }

}
