package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.Video;
import com.example.onlineexam.domain.VideoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VideoMapper {
    long countByExample(VideoExample example);

    int deleteByExample(VideoExample example);

    int deleteByPrimaryKey(Integer vid);

    int insert(Video row);

    int insertSelective(Video row);

    List<Video> selectByExample(VideoExample example);

    Video selectByPrimaryKey(Integer vid);

    int updateByExampleSelective(@Param("row") Video row, @Param("example") VideoExample example);

    int updateByExample(@Param("row") Video row, @Param("example") VideoExample example);

    int updateByPrimaryKeySelective(Video row);

    int updateByPrimaryKey(Video row);
}