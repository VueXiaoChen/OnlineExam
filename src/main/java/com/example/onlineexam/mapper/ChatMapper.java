package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.Chat;
import com.example.onlineexam.domain.ChatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChatMapper {
    long countByExample(ChatExample example);

    int deleteByExample(ChatExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Chat row);

    int insertSelective(Chat row);

    List<Chat> selectByExample(ChatExample example);

    Chat selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Chat row, @Param("example") ChatExample example);

    int updateByExample(@Param("row") Chat row, @Param("example") ChatExample example);

    int updateByPrimaryKeySelective(Chat row);

    int updateByPrimaryKey(Chat row);
}