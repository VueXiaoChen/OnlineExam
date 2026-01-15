package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.MsgUnread;
import com.example.onlineexam.domain.MsgUnreadExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface MsgUnreadMapper {
    long countByExample(MsgUnreadExample example);

    int deleteByExample(MsgUnreadExample example);

    int deleteByPrimaryKey(Integer uid);

    int insert(MsgUnread row);

    int insertSelective(MsgUnread row);

    List<MsgUnread> selectByExample(MsgUnreadExample example);

    MsgUnread selectByPrimaryKey(Integer uid);

    int updateByExampleSelective(@Param("row") MsgUnread row, @Param("example") MsgUnreadExample example);

    int updateByExample(@Param("row") MsgUnread row, @Param("example") MsgUnreadExample example);

    int updateByPrimaryKeySelective(MsgUnread row);

    int updateByPrimaryKey(MsgUnread row);
}