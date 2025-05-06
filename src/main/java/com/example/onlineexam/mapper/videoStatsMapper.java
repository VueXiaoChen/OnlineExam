package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.videoStats;
import com.example.onlineexam.domain.videoStatsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface videoStatsMapper {
    long countByExample(videoStatsExample example);

    int deleteByExample(videoStatsExample example);

    int deleteByPrimaryKey(Integer vid);

    int insert(videoStats row);

    int insertSelective(videoStats row);

    List<videoStats> selectByExample(videoStatsExample example);

    videoStats selectByPrimaryKey(Integer vid);

    int updateByExampleSelective(@Param("row") videoStats row, @Param("example") videoStatsExample example);

    int updateByExample(@Param("row") videoStats row, @Param("example") videoStatsExample example);

    int updateByPrimaryKeySelective(videoStats row);

    int updateByPrimaryKey(videoStats row);
}