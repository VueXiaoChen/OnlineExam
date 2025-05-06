package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.ChatDetailed;
import com.example.onlineexam.domain.ChatDetailedExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChatDetailedMapper {
    long countByExample(ChatDetailedExample example);

    int deleteByExample(ChatDetailedExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ChatDetailed row);

    int insertSelective(ChatDetailed row);

    List<ChatDetailed> selectByExample(ChatDetailedExample example);

    ChatDetailed selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") ChatDetailed row, @Param("example") ChatDetailedExample example);

    int updateByExample(@Param("row") ChatDetailed row, @Param("example") ChatDetailedExample example);

    int updateByPrimaryKeySelective(ChatDetailed row);

    int updateByPrimaryKey(ChatDetailed row);
}