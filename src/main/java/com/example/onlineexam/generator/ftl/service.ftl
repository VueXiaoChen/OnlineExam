package com.example.onlineexam.service;


import com.example.onlineexam.domain.${Domain};
import com.example.onlineexam.domain.${Domain}Example;
import com.example.onlineexam.mapper.${Domain}Mapper;
import com.example.onlineexam.req.${Domain}Req;
import com.example.onlineexam.resp.${Domain}Resp;
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
public class ${Domain}Service {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(${Domain}Service.class);

    @Resource
    public ${Domain}Mapper ${domain}Mapper;

    public PageResp<${Domain}Resp> list(${Domain}Req ${domain}Req) {
        //固定写法
        ${Domain}Example example = new ${Domain}Example();
        //固定写法
        ${Domain}Example.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(${domain}Req.getPage(), ${domain}Req.getSize());
        //类接收返回的数据
        List<${Domain}> sortsList = ${domain}Mapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<${Domain}Resp> data = CopyUtil.copyList(sortsList, ${Domain}Resp.class);
        //定义分页获取总数
        PageInfo<${Domain}> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<${Domain}Resp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(${Domain}Req ${domain}Req) {
        ${Domain} ${domain} = CopyUtil.copy(${domain}Req, ${Domain}.class);
        //固定写法
        ${Domain}Example example = new ${Domain}Example();
        //固定写法
        ${Domain}Example.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(${domain}Req.)) {
            ${domain}Mapper.insertSelective(${domain});
        } else {
            //更新数据
            ${domain}Mapper.updateByPrimaryKeySelective(${domain});
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        ${domain}Mapper.deleteByPrimaryKey(id);
    }

}
