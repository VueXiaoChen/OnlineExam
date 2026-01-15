package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.Danmu;
import com.example.onlineexam.domain.DanmuExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DanmuMapper {
    long countByExample(DanmuExample example);

    int deleteByExample(DanmuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Danmu row);

    int insertSelective(Danmu row);

    List<Danmu> selectByExample(DanmuExample example);

    Danmu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Danmu row, @Param("example") DanmuExample example);

    int updateByExample(@Param("row") Danmu row, @Param("example") DanmuExample example);

    int updateByPrimaryKeySelective(Danmu row);

    int updateByPrimaryKey(Danmu row);
}