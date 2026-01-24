package com.example.onlineexam.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onlineexam.domain.Category;
import com.example.onlineexam.domain.CategoryDTO;
import com.example.onlineexam.domain.CategoryExample;
import com.example.onlineexam.mapper.CategoryMapper;
import com.example.onlineexam.req.CategoryReq;
import com.example.onlineexam.resp.CategoryResp;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.example.onlineexam.util.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CategoryService.class);

    @Resource
    public CategoryMapper categoryMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    public PageResp<CategoryResp> list(CategoryReq categoryReq) {
        //固定写法
        CategoryExample example = new CategoryExample();
        //固定写法
        CategoryExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(categoryReq.getPage(), categoryReq.getSize());
        //类接收返回的数据
        List<Category> sortsList = categoryMapper.selectByExample(example);
        //定义分页获取总数
        PageInfo<Category> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<CategoryResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将返回的数据进行封装,某些信息是不需要返回的
        List<CategoryResp> data = CopyUtil.copyList(sortsList, CategoryResp.class);
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(CategoryReq categoryReq) {
        Category category = CopyUtil.copy(categoryReq, Category.class);
        //固定写法
        CategoryExample example = new CategoryExample();
        //固定写法
        CategoryExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(categoryReq.getMcId())) {
            categoryMapper.insertSelective(category);
        } else {
            //更新数据
            categoryMapper.updateByPrimaryKeySelective(category);
        }
    }

    //删除数据
    public void delete(String mcId, String scId) {
        //删除数据
        categoryMapper.deleteByPrimaryKey(mcId,scId);
    }
    /**
     * 获取全部分区数据
     * @return CustomResponse对象
     */
    public CommonResp getAll() {
        CommonResp commonResp = new CommonResp();
        List<CategoryDTO> sortedCategories = new ArrayList<>();

        // 尝试从redis中获取数据
        try {
            sortedCategories = redisUtils.getAllList("categoryList", CategoryDTO.class);
            if (sortedCategories.size() != 0) {
                commonResp.setMessage("获取成功");
                commonResp.setData(sortedCategories);
                return commonResp;
            }
            LOG.info("redis中获取不到分区数据");
        } catch (Exception e) {
            LOG.info("获取redis分区数据失败");
        }

        //固定写法
        CategoryExample example = new CategoryExample();
        //固定写法
        CategoryExample.Criteria criteria = example.createCriteria();
        //类接收返回的数据
        List<Category> sortsList = categoryMapper.selectByExample(example);

        // 开一个临时整合map
        Map<String, CategoryDTO> categoryDTOMap = new HashMap<>();

        for (Category category : sortsList) {
            String mcId = category.getMcId();
            String scId = category.getScId();
            String mcName = category.getMcName();
            String scName = category.getScName();
            String descr = category.getDescr();
            List<String> rcmTag = new ArrayList<>();
            if (category.getRcmTag() != null) {
                String[] strings = category.getRcmTag().split("\n");    // 将每个标签切出来组成列表封装
                rcmTag = Arrays.asList(strings);
            }

            // 先将主分类和空的子分类列表整合到map中
            if (!categoryDTOMap.containsKey(mcId)) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setMcId(mcId);
                categoryDTO.setMcName(mcName);
                categoryDTO.setScList(new ArrayList<>());
                categoryDTOMap.put(mcId, categoryDTO);
            }

            // 把子分类整合到map的子分类列表里
            Map<String, Object> scMap = new HashMap<>();
            scMap.put("mcId", mcId);
            scMap.put("scId", scId);
            scMap.put("scName", scName);
            scMap.put("descr", descr);
            scMap.put("rcmTag", rcmTag);
            categoryDTOMap.get(mcId).getScList().add(scMap);

        }

        // 按指定序列排序
        List<String> sortOrder = Arrays.asList("anime", "guochuang", "douga", "game", "kichiku",
                "music", "dance", "cinephile", "ent", "knowledge",
                "tech", "information", "food", "life", "car",
                "fashion", "sports", "animal", "virtual");

        for (String mcId : sortOrder) {
            if (categoryDTOMap.containsKey(mcId)) {
                sortedCategories.add(categoryDTOMap.get(mcId));
            }
        }
        // 将分类添加到redis缓存中
        try {
            redisUtils.delValue("categoryList");
            redisUtils.setAllList("categoryList", sortedCategories);
        } catch (Exception e) {
            LOG.info("存储redis分类列表失败");
        }
        commonResp.setMessage("获取成功");
        commonResp.setData(sortedCategories);
        return commonResp;
    }

    /**
     * 根据id查询对应分区信息
     * @param mcId 主分区ID
     * @param scId 子分区ID
     * @return Category类信息
     */
    public Category getCategoryById(String mcId, String scId) {
        // 从redis中获取最新数据
        Category category = redisUtils.getObject("category:" + mcId + ":" + scId, Category.class);
        // 如果redis中没有数据，从mysql中获取并更新到redis
        if (category == null) {
            // 使用Example查询
            CategoryExample example = new CategoryExample();
            example.createCriteria()
                    .andMcIdEqualTo(mcId)
                    .andScIdEqualTo(scId);

            List<Category> categories = categoryMapper.selectByExample(example);
            category = categories.isEmpty() ? new Category() : categories.get(0);

            // 异步更新缓存（仅当查询到有效数据时）
            if (!categories.isEmpty()) {
                Category finalCategory = category;
                CompletableFuture.runAsync(() -> {
                    redisUtils.setExObjectValue("category:" + mcId + ":" + scId, finalCategory,3600, TimeUnit.SECONDS);
                }, taskExecutor);
            }
        }
        return category;
    }
}
