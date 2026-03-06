package com.example.onlineexam.service;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.onlineexam.domain.Favorite;
import com.example.onlineexam.domain.FavoriteExample;
import com.example.onlineexam.domain.Video;
import com.example.onlineexam.exception.BusinessException;
import com.example.onlineexam.exception.BusinessExceptionCode;
import com.example.onlineexam.mapper.FavoriteMapper;
import com.example.onlineexam.mapper.VideoMapper;
import com.example.onlineexam.req.FavoriteReq;
import com.example.onlineexam.resp.FavoriteResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.example.onlineexam.util.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.*;

@Service
public class FavoriteService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(FavoriteService.class);

    @Resource
    public FavoriteMapper favoriteMapper;

    @Resource
    public VideoMapper videoMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

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
    //查询收藏夹
    public List<Favorite> getFavorites(Integer uid, boolean isOwner) {
        String key = "favorites:" + uid;   // uid用户的收藏夹列表
        String string = redisUtils.getObjectString(key);
        List<Favorite> list = JSONArray.parseArray(string, Favorite.class);
        if (list != null) {
            if (!isOwner) {
                List<Favorite> list1 = new ArrayList<>();
                for (Favorite favorite : list) {
                    if (favorite.getVisible() == 1) {
                        list1.add(favorite);
                    }
                }
                return list1;
            }
            return list;
        }

        // 使用 FavoriteExample 构建查询条件
        FavoriteExample example = new FavoriteExample();
        example.createCriteria()
                .andUidEqualTo(uid)
                .andIsDeleteNotEqualTo(1);  // 排除已删除的收藏夹
        example.setOrderByClause("fid desc");

        list = favoriteMapper.selectByExample(example);
        if (list != null && !list.isEmpty()) {
            // 使用事务批量操作 减少连接sql的开销
            try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
                // 设置收藏夹封面
                list.stream().parallel().forEach(favorite -> {
                    if (favorite.getCover() == null) {
                        Set<Object> set = redisUtils.zReverange("favorite_video:" + favorite.getFid(), 0, 0);    // 找到最近一个收藏的视频
                        if (set != null && set.size() > 0) {
                            Integer vid = (Integer) set.iterator().next();
                            Video video = videoMapper.selectById(vid);
                            favorite.setCover(video.getCoverUrl());
                        }
                    }
                });
                sqlSession.commit();
            }
            List<Favorite> finalList = list;
            CompletableFuture.runAsync(() -> {
                redisUtils.setExObjectValue(key, finalList);
            }, taskExecutor);
            if (!isOwner) {
                List<Favorite> list1 = new ArrayList<>();
                for (Favorite favorite : list) {
                    if (favorite.getVisible() == 1) {
                        list1.add(favorite);
                    }
                }
                return list1;
            }
            return list;
        }
        return Collections.emptyList();
    }

    public Favorite addFavorite(Integer uid, String title, String desc, Integer visible) {
        // 懒得做字数等的合法判断了，前端做吧
        Favorite favorite = new Favorite();
        favorite.setUid(uid);
        favorite.setVisible(visible);
        favorite.setTitle(title);
        favorite.setDescription(desc);
        favorite.setType(2);
        favorite.setCount(0);
        favoriteMapper.insert(favorite);
        redisUtils.delValue("favorites:" + uid);
        return favorite;
    }

    public Favorite updateFavorite(Integer fid, Integer uid, String title, String desc, Integer visible) {
        // 1. 根据主键查询原收藏记录（假设 Mapper 提供 selectByPrimaryKey 方法）
        Favorite favorite = favoriteMapper.selectByPrimaryKey(fid);
        if (favorite == null || !Objects.equals(favorite.getUid(), uid)) {
            return null;
        }

        // 2. 创建要更新的实体对象（只设置需要修改的字段）
        Favorite updateRecord = new Favorite();
        updateRecord.setTitle(title);
        updateRecord.setDescription(desc);
        updateRecord.setVisible(visible);

        // 3. 创建 Example 并添加条件（指定 fid）
        FavoriteExample example = new FavoriteExample();
        FavoriteExample.Criteria criteria = example.createCriteria();
        criteria.andFidEqualTo(fid); // 假设生成的 Example 包含 andFidEqualTo 方法

        // 4. 执行更新（使用选择性更新，只更新 record 中非 null 的字段）
        favoriteMapper.updateByExampleSelective(updateRecord, example);

        // 5. 删除缓存并返回原对象
        redisUtils.delValue("favorites:" + uid);
            return favorite;
    }

    /**
     * 删除收藏夹（软删除）
     * @param fid 收藏夹ID
     * @param currentUid 当前操作用户ID（用于权限校验）
     * @return 是否删除成功
     */
    public boolean deleteFavorite(Integer fid, Integer currentUid) {
        // 1. 查询收藏夹是否存在且未删除
        Favorite favorite = favoriteMapper.selectByPrimaryKey(fid);
        if (favorite == null || favorite.getIsDelete() == 1) {
            // 收藏夹不存在或已删除，可视为删除成功或抛出异常，根据业务决定
            return false;
        }

        // 2. 权限校验：只有所有者可以删除
        if (!favorite.getUid().equals(currentUid)) {
            throw new BusinessException(BusinessExceptionCode.USER_DEL_FAVORITE);
        }

        // 3. 执行软删除（使用 Example 更新）
        FavoriteExample example = new FavoriteExample();
        example.createCriteria().andFidEqualTo(fid);
        Favorite updateFavorite = new Favorite();
        updateFavorite.setIsDelete(1);   // 标记为删除
        int updated = favoriteMapper.updateByExampleSelective(updateFavorite, example);

        if (updated > 0) {
            // 4. 清理缓存
            // 4.1 删除该用户的收藏夹列表缓存
            String userFavoritesKey = "favorites:" + favorite.getUid();
            redisUtils.delValue(userFavoritesKey);

            // 4.2 可选：删除该收藏夹下的视频列表缓存（如果业务上不再需要）
            String favoriteVideosKey = "favorite_video:" + fid;
            redisUtils.delValue(favoriteVideosKey);

            return true;
        }
        return false;
    }

}
