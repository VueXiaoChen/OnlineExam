package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.FavoriteVideo;
import com.example.onlineexam.domain.FavoriteVideoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FavoriteVideoMapper {
    long countByExample(FavoriteVideoExample example);

    int deleteByExample(FavoriteVideoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FavoriteVideo row);

    int insertSelective(FavoriteVideo row);

    List<FavoriteVideo> selectByExample(FavoriteVideoExample example);

    FavoriteVideo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") FavoriteVideo row, @Param("example") FavoriteVideoExample example);

    int updateByExample(@Param("row") FavoriteVideo row, @Param("example") FavoriteVideoExample example);

    int updateByPrimaryKeySelective(FavoriteVideo row);

    int updateByPrimaryKey(FavoriteVideo row);
}