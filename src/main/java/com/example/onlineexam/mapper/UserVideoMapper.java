package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.UserVideo;
import com.example.onlineexam.domain.UserVideoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserVideoMapper {
    long countByExample(UserVideoExample example);

    int deleteByExample(UserVideoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserVideo row);

    int insertSelective(UserVideo row);

    List<UserVideo> selectByExample(UserVideoExample example);

    UserVideo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") UserVideo row, @Param("example") UserVideoExample example);

    int updateByExample(@Param("row") UserVideo row, @Param("example") UserVideoExample example);

    int updateByPrimaryKeySelective(UserVideo row);

    int updateByPrimaryKey(UserVideo row);
}