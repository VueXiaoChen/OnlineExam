package com.example.onlineexam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlineexam.domain.Comment;
import com.example.onlineexam.domain.VideoStats;
import com.example.onlineexam.domain.VideoStatsExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface VideoStatsMapper extends BaseMapper<VideoStats> {
    long countByExample(VideoStatsExample example);

    int deleteByExample(VideoStatsExample example);

    int deleteByPrimaryKey(Integer vid);

    int insert(VideoStats row);

    int insertSelective(VideoStats row);

    List<VideoStats> selectByExample(VideoStatsExample example);

    VideoStats selectByPrimaryKey(Integer vid);

    int updateByExampleSelective(@Param("row") VideoStats row, @Param("example") VideoStatsExample example);

    int updateByExample(@Param("row") VideoStats row, @Param("example") VideoStatsExample example);

    int updateByPrimaryKeySelective(VideoStats row);

    int updateByPrimaryKey(VideoStats row);

    int updateStatsDynamic(
            @Param("vid") int vid,
            @Param("column") String column,
            @Param("count") int count,
            @Param("increase") boolean increase
    );
    int updateStats(
            @Param("vid") int vid,
            @Param("addGood") boolean addGood
    );
}