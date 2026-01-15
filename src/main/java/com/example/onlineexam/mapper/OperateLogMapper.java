package com.example.onlineexam.mapper;

import com.example.onlineexam.domain.OperateLog;
import com.example.onlineexam.domain.OperateLogExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OperateLogMapper {
    long countByExample(OperateLogExample example);

    int deleteByExample(OperateLogExample example);

    int deleteByPrimaryKey(Long logId);

    int insert(OperateLog row);

    int insertSelective(OperateLog row);

    List<OperateLog> selectByExampleWithBLOBs(OperateLogExample example);

    List<OperateLog> selectByExample(OperateLogExample example);

    OperateLog selectByPrimaryKey(Long logId);

    int updateByExampleSelective(@Param("row") OperateLog row, @Param("example") OperateLogExample example);

    int updateByExampleWithBLOBs(@Param("row") OperateLog row, @Param("example") OperateLogExample example);

    int updateByExample(@Param("row") OperateLog row, @Param("example") OperateLogExample example);

    int updateByPrimaryKeySelective(OperateLog row);

    int updateByPrimaryKeyWithBLOBs(OperateLog row);

    int updateByPrimaryKey(OperateLog row);
}