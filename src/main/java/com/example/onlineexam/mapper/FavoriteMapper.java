package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.Favorite;
import com.example.onlineexam.domain.FavoriteExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface FavoriteMapper {
    long countByExample(FavoriteExample example);

    int deleteByExample(FavoriteExample example);

    int deleteByPrimaryKey(Integer fid);

    int insert(Favorite row);

    int insertSelective(Favorite row);

    List<Favorite> selectByExample(FavoriteExample example);

    Favorite selectByPrimaryKey(Integer fid);

    int updateByExampleSelective(@Param("row") Favorite row, @Param("example") FavoriteExample example);

    int updateByExample(@Param("row") Favorite row, @Param("example") FavoriteExample example);

    int updateByPrimaryKeySelective(Favorite row);

    int updateByPrimaryKey(Favorite row);
}