package com.example.onlineexam.service;


import com.example.onlineexam.domain.OperateLog;
import com.example.onlineexam.domain.OperateLogExample;
import com.example.onlineexam.mapper.OperateLogMapper;
import com.example.onlineexam.req.OperateLogReq;
import com.example.onlineexam.resp.OperateLogResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class OperateLogService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(OperateLogService.class);

    @Resource
    public OperateLogMapper operateLogMapper;

    public PageResp<OperateLogResp> list(OperateLogReq operateLogReq) {
        //固定写法
        OperateLogExample example = new OperateLogExample();
        //固定写法
        OperateLogExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(operateLogReq.getPage(), operateLogReq.getSize());
        //类接收返回的数据
        List<OperateLog> sortsList = operateLogMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<OperateLogResp> data = CopyUtil.copyList(sortsList, OperateLogResp.class);
        //定义分页获取总数
        PageInfo<OperateLog> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<OperateLogResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(OperateLogReq operateLogReq) {
        OperateLog operateLog = CopyUtil.copy(operateLogReq, OperateLog.class);
        //固定写法
        OperateLogExample example = new OperateLogExample();
        //固定写法
        OperateLogExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(operateLogReq.getLogId())) {
            operateLogMapper.insertSelective(operateLog);
        } else {
            //更新数据
            operateLogMapper.updateByPrimaryKeySelective(operateLog);
        }
    }

    //删除数据
    public void delete(Long id) {
        //删除数据
        operateLogMapper.deleteByPrimaryKey(id);
    }

}
