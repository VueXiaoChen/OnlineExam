package com.example.onlineexam.service;


import cn.hutool.core.bean.BeanUtil;
import com.example.onlineexam.domain.User;
import com.example.onlineexam.domain.UserDTO;
import com.example.onlineexam.domain.UserExample;
import com.example.onlineexam.domain.VideoStats;
import com.example.onlineexam.exception.BusinessException;
import com.example.onlineexam.exception.BusinessExceptionCode;
import com.example.onlineexam.mapper.UserMapper;
import com.example.onlineexam.req.UserReq;
import com.example.onlineexam.req.UsersLoadingReq;
import com.example.onlineexam.req.VideoReq;
import com.example.onlineexam.resp.UserResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.resp.UsersLoadingResp;
import com.example.onlineexam.resp.VideoResp;
import com.example.onlineexam.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(UserService.class);

    @Resource
    public UserMapper userMapper;
    @Resource
    private SnowFlake snowFlake;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private VideoStatsService videoStatsService;

    @Autowired
    private VideoService videoService;
    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

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
                //TODO 此处的redis存取userid还需要改进 先用着
                LOG.info("登录成功");
                //CurrentUser currentUser = new CurrentUser();
                //usersLoadingResp.setUserDTO(getUserById(user.getUid()));
                redisUtils.setExObjectValue("user:" + user.getUid(), user);  // 默认存活1小时
                try {
                    // 把完整的用户信息存入redis，时间跟token一样，注意单位
                    // 这里缓存的user信息建议只供读取uid用，其中的状态等非静态数据可能不准，所以 redis另外存值
                    redisUtils.setExObjectValue("security:user:" + user.getUid(), user, 60L * 60 * 24 * 2, TimeUnit.SECONDS);
                    // 将该用户放到redis中在线集合
//            redisUtil.addMember("login_member", user.getUid());
                } catch (Exception e) {
                    LOG.error("存储redis数据失败");
                    throw e;
                }
                UserDTO userDTO = new UserDTO();
                userDTO.setUid(user.getUid());
                userDTO.setNickname(user.getNickname());
                userDTO.setAvatar_url(user.getAvatar());
                userDTO.setBg_url(user.getBackground());
                userDTO.setGender(user.getGender());
                userDTO.setDescription(user.getDescription());
                userDTO.setExp(user.getExp());
                userDTO.setCoin(Double.valueOf(user.getCoin()));
                userDTO.setVip(user.getVip());
                userDTO.setState(user.getState());
                userDTO.setAuth(user.getAuth());
                userDTO.setAuthMsg(user.getAuthMsg());
                userDTO.setFollowsCount(110);
                userDTO.setFansCount(110);
                usersLoadingResp.setToken(token);
                VideoReq videoReq = new VideoReq();
                videoReq.setUid(user.getUid());
                PageResp<VideoResp> videoRespPageResp = videoService.list(videoReq);
                Set<Object> set =new HashSet<>();
                for (int i = 0; i < videoRespPageResp.getTotal(); i++) {
                    set.add(videoRespPageResp.getList().get(i).getVid());
                }
                if (set == null || set.size() == 0) {
                    userDTO.setVideoCount(0);
                    userDTO.setLoveCount(0);
                    userDTO.setPlayCount(0);
                }
                LOG.info("数据:{}",set);
                // 并发执行每个视频数据统计的查询任务
                List<VideoStats> list = set.stream().parallel()
                        .map(vid -> videoStatsService.getVideoStatsById((Integer) vid))
                        .collect(Collectors.toList());

                int video = list.size(), love = 0, play = 0;
                for (VideoStats VideoStats : list) {
                    love = love + VideoStats.getGood();
                    play = play + VideoStats.getPlay();
                }
                userDTO.setVideoCount(video);
                userDTO.setLoveCount(love);
                userDTO.setPlayCount(play);
                usersLoadingResp.setUserDTO(userDTO);
                redisUtils.setValue("usersid",user.getUid());

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
     * @param id 用户ID
     * @return 用户可见信息实体类 UserDTO
     */
    public UserDTO getUserById(Integer id) {
        // 从redis中获取最新数据
        User user = redisUtils.getObject("user:" + id, User.class);
        // 如果redis中没有user数据，就从mysql中获取并更新到redis
        if (user == null) {
            user = userMapper.selectByPrimaryKey(id);
            if (user == null) {
                return null;    // 如果uid不存在则返回空
            }
            User finalUser = user;
            CompletableFuture.runAsync(() -> {
                redisUtils.setExObjectValue("user:" + finalUser.getUid(), finalUser);  // 默认存活1小时
            }, taskExecutor);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(user.getUid());
        userDTO.setState(user.getState());
        if (user.getState() == 2) {
            userDTO.setNickname("账号已注销");
            userDTO.setAvatar_url("https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png");
            userDTO.setBg_url("https://tinypic.host/images/2023/11/15/69PB2Q5W9D2U7L.png");
            userDTO.setGender(2);
            userDTO.setDescription("-");
            userDTO.setExp(0);
            userDTO.setCoin((double) 0);
            userDTO.setVip(0);
            userDTO.setAuth(0);
            userDTO.setVideoCount(0);
            userDTO.setFollowsCount(0);
            userDTO.setFansCount(0);
            userDTO.setLoveCount(0);
            userDTO.setPlayCount(0);
            return userDTO;
        }
        userDTO.setNickname(user.getNickname());
        userDTO.setAvatar_url(user.getAvatar());
        userDTO.setBg_url(user.getBackground());
        userDTO.setGender(user.getGender());
        userDTO.setDescription(user.getDescription());
        userDTO.setExp(user.getExp());
        userDTO.setCoin(Double.valueOf(user.getCoin()));
        userDTO.setVip(user.getVip());
        userDTO.setAuth(user.getAuth());
        userDTO.setAuthMsg(user.getAuthMsg());
        userDTO.setFollowsCount(0);
        userDTO.setFansCount(0);
        Set<Object> set= redisUtils.zReverange("user_video_upload:" + user.getUid(), 0L, -1L);

        if (set == null || set.size() == 0) {
            userDTO.setVideoCount(0);
            userDTO.setLoveCount(0);
            userDTO.setPlayCount(0);
            return userDTO;
        }
        // 并发执行每个视频数据统计的查询任务
        List<VideoStats> list = set.stream().parallel()
                .map(vid -> videoStatsService.getVideoStatsById((Integer) vid))
                .collect(Collectors.toList());

        int video = list.size(), love = 0, play = 0;
        for (VideoStats VideoStats : list) {
            love = love + VideoStats.getGood();
            play = play + VideoStats.getPlay();
        }
        userDTO.setVideoCount(video);
        userDTO.setLoveCount(love);
        userDTO.setPlayCount(play);

        return userDTO;
    }

//    /**
//     * 根据uid查询用户信息
//     * @param uid 用户ID
//     * @return 用户可见信息实体类 UserDTO
//     */
//    public UserResp getUserById(Integer uid){
//      User user = userMapper.selectByPrimaryKey(uid);
//      //判断数据是否为孔或者null
//      if(ObjectUtils.isEmpty(user)){
//          throw new BusinessException(BusinessExceptionCode.USER_INFO_ERROR);
//      }else{
//          UserResp userResp = CopyUtil.copy(user,UserResp.class);
//          return userResp;
//      }
//    }

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
