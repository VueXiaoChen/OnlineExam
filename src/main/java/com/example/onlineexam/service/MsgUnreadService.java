package com.example.onlineexam.service;


import com.example.onlineexam.domain.MsgUnread;
import com.example.onlineexam.domain.MsgUnreadExample;
import com.example.onlineexam.mapper.MsgUnreadMapper;
import com.example.onlineexam.req.MsgUnreadReq;
import com.example.onlineexam.resp.MsgUnreadResp;
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
public class MsgUnreadService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(MsgUnreadService.class);

    @Resource
    public MsgUnreadMapper msgUnreadMapper;

    public PageResp<MsgUnreadResp> list(MsgUnreadReq msgUnreadReq) {
        //固定写法
        MsgUnreadExample example = new MsgUnreadExample();
        //固定写法
        MsgUnreadExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(msgUnreadReq.getPage(), msgUnreadReq.getSize());
        //类接收返回的数据
        List<MsgUnread> sortsList = msgUnreadMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<MsgUnreadResp> data = CopyUtil.copyList(sortsList, MsgUnreadResp.class);
        //定义分页获取总数
        PageInfo<MsgUnread> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<MsgUnreadResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(MsgUnreadReq msgUnreadReq) {
        MsgUnread msgUnread = CopyUtil.copy(msgUnreadReq, MsgUnread.class);
        //固定写法
        MsgUnreadExample example = new MsgUnreadExample();
        //固定写法
        MsgUnreadExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(msgUnreadReq.getUid())) {
            msgUnreadMapper.insertSelective(msgUnread);
        } else {
            //更新数据
            msgUnreadMapper.updateByPrimaryKeySelective(msgUnread);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        msgUnreadMapper.deleteByPrimaryKey(id);
    }


    public MsgUnread findusermsg(Integer id) {
        //查询数据
        MsgUnread msgUnread =   msgUnreadMapper.selectByPrimaryKey(id);
        return msgUnread;
    }

}
