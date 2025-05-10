package com.example.onlineexam.util;


import com.example.onlineexam.domain.User;
import com.example.onlineexam.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CurrentUser {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取当前登录用户的uid，也是JWT认证的一环
     * @return 当前登录用户的uid
     */
    public Set<Object> getUserId() {
          // 这里的user是登录时存的security:user，因为是静态数据，可能会跟实际的有区别，所以只能用作获取uid用
        Set<Object> set = redisUtils.zRange("usersid",0,-1L);
        return set;
    }

    /**
     * 判断当前用户是否管理员
     * @return  是否管理员 true/false
     */
//    public Boolean isAdmin() {
//        Long uid = getUserId();
//        User user = redisUtils.getObject("user:" + uid, User.class);
//        if (user == null) {
//            user = userMapper.selectByPrimaryKey(Math.toIntExact(uid));
//            redisUtils.setExObjectValue("user:" + user.getUid(), user);
//        }
//        return (user.getRole() == 1 || user.getRole() == 2);
//    }
}
