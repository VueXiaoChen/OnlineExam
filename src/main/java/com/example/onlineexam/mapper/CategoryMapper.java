package com.example.onlineexam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlineexam.domain.Category;
import com.example.onlineexam.domain.CategoryExample;
import java.util.List;

import com.example.onlineexam.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface CategoryMapper extends BaseMapper<Comment>{
    long countByExample(CategoryExample example);

    int deleteByExample(CategoryExample example);

    int deleteByPrimaryKey(@Param("mcId") String mcId, @Param("scId") String scId);

    int insert(Category row);

    int insertSelective(Category row);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(@Param("mcId") String mcId, @Param("scId") String scId);

    int updateByExampleSelective(@Param("row") Category row, @Param("example") CategoryExample example);

    int updateByExample(@Param("row") Category row, @Param("example") CategoryExample example);

    int updateByPrimaryKeySelective(Category row);

    int updateByPrimaryKey(Category row);
}