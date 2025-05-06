package com.example.onlineexam.service;


import com.example.onlineexam.domain.Category;
import com.example.onlineexam.domain.CategoryExample;
import com.example.onlineexam.mapper.CategoryMapper;
import com.example.onlineexam.req.CategoryReq;
import com.example.onlineexam.resp.CategoryResp;
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
public class CategoryService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CategoryService.class);

    @Resource
    public CategoryMapper categoryMapper;

    public PageResp<CategoryResp> list(CategoryReq categoryReq) {
        //固定写法
        CategoryExample example = new CategoryExample();
        //固定写法
        CategoryExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(categoryReq.getPage(), categoryReq.getSize());
        //类接收返回的数据
        List<Category> sortsList = categoryMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<CategoryResp> data = CopyUtil.copyList(sortsList, CategoryResp.class);
        //定义分页获取总数
        PageInfo<Category> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<CategoryResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
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
    public void delete(Integer id) {
        //删除数据
        categoryMapper.deleteByPrimaryKey(id);
    }

}
