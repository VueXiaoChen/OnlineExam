package com.example.onlineexam.service;


import cn.hutool.core.bean.BeanUtil;
import com.example.onlineexam.domain.User;
import com.example.onlineexam.domain.UserExample;
import com.example.onlineexam.exception.BusinessException;
import com.example.onlineexam.exception.BusinessExceptionCode;
import com.example.onlineexam.mapper.UserMapper;
import com.example.onlineexam.req.UserReq;
import com.example.onlineexam.req.UsersLoadingReq;
import com.example.onlineexam.resp.UserResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.resp.UsersLoadingResp;
import com.example.onlineexam.util.CopyUtil;
import com.example.onlineexam.util.JwtUtil;
import com.example.onlineexam.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(UserService.class);

    @Resource
    public UserMapper userMapper;
    @Resource
    private SnowFlake snowFlake;

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
        //判断修改的用户名是否重复
        User usernickdto = selectByName(userReq.getUsername(),userReq.getNickname());
        //增加数据需要判断传过来的用户名是否被用过
        if(ObjectUtils.isEmpty(usernickdto)){
            if(ObjectUtils.isEmpty(userReq.getUid())){
                user.setCreateDate(new Date());
                user.setVip(0);
                user.setState(0);
                user.setRole(0);
                user.setAuth(0);
                userMapper.insertSelective(user);
            }else{
                userMapper.updateByPrimaryKeySelective(user);
            }
        }else{
            throw new BusinessException(BusinessExceptionCode.USER_USERNAME_ERROR);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        userMapper.deleteByPrimaryKey(id);
    }

    //判断名称重复的方法
    public User selectByName(String username,String nickname){
        //固定写法
        UserExample example = new UserExample();
        //固定写法
        UserExample.Criteria criteria = example.createCriteria();
        //固定写法
        UserExample.Criteria criteria2 = example.createCriteria();
        //查询用户名
        criteria.andUsernameEqualTo(username);
        criteria2.andNicknameEqualTo(nickname);
        example.or(criteria2);
        //返回查询的实体类
        List<User> userList = userMapper.selectByExample(example);
        //判断是否有数据
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }else{
            return userList.get(0);
        }
    }
    //登录
    public UsersLoadingResp loading(UsersLoadingReq usersLoadingReq) {
        User user = selectByName(usersLoadingReq.getUsername(),"");
        if(!ObjectUtils.isEmpty(user)){
            //判断用户名是否一样
            if(user.getPassword().equals(usersLoadingReq.getPassword())){
                //登录成功
                UsersLoadingResp usersLoadingResp = CopyUtil.copy(user,UsersLoadingResp.class);
                Map<String,Object> map =BeanUtil.beanToMap(usersLoadingResp);
                usersLoadingResp.setUid(Long.valueOf(user.getUid()));
                usersLoadingResp.setUsername(user.getUsername());
                String token = JwtUtil.createToken(usersLoadingResp.getUid(),usersLoadingResp.getUsername());
                usersLoadingResp.setToken(token);
                LOG.info("登录成功");
                return usersLoadingResp;
            }else{
                //账号或者密码错误
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }else{
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }
    }

    /**
     * 根据uid查询用户信息
     * @param uid 用户ID
     * @return 用户可见信息实体类 UserDTO
     */
    public UserResp getUserById(Integer uid){
      User user = userMapper.selectByPrimaryKey(uid);
      //判断数据是否为孔或者null
      if(ObjectUtils.isEmpty(user)){
          throw new BusinessException(BusinessExceptionCode.USER_INFO_ERROR);
      }else{
          UserResp userResp = CopyUtil.copy(user,UserResp.class);
          return userResp;
      }
    }

    /**
     * 更新用户头像
     * @param uid
     * @param file
     * @return
     */
    public void updateUserAvatar(Integer uid, MultipartFile file){
        //TODO 没有oss暂时不做该功能
    }



}
