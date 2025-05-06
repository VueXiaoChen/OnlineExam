package com.example.onlineexam.service;


import com.example.onlineexam.domain.Chat;
import com.example.onlineexam.domain.ChatExample;
import com.example.onlineexam.mapper.ChatMapper;
import com.example.onlineexam.req.ChatReq;
import com.example.onlineexam.resp.ChatResp;
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
public class ChatService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(ChatService.class);

    @Resource
    public ChatMapper chatMapper;

    public PageResp<ChatResp> list(ChatReq chatReq) {
        //固定写法
        ChatExample example = new ChatExample();
        //固定写法
        ChatExample.Criteria criteria = example.createCriteria();
        //分页(获取从页面传来的数据)
        PageHelper.startPage(chatReq.getPage(), chatReq.getSize());
        //类接收返回的数据
        List<Chat> sortsList = chatMapper.selectByExample(example);
        //将返回的数据进行封装,某些信息是不需要返回的
        List<ChatResp> data = CopyUtil.copyList(sortsList, ChatResp.class);
        //定义分页获取总数
        PageInfo<Chat> pageInfo = new PageInfo<>(sortsList);
        //定义分页
        PageResp<ChatResp> pageResp = new PageResp<>();
        //将分页的数据进行总和
        pageResp.setTotal(pageInfo.getTotal());
        //将分页的数据加入类
        pageResp.setList(data);
        return pageResp;
    }
    //增加修改数据
    public void save(ChatReq chatReq) {
        Chat chat = CopyUtil.copy(chatReq, Chat.class);
        //固定写法
        ChatExample example = new ChatExample();
        //固定写法
        ChatExample.Criteria criteria = example.createCriteria();
        //增加数据
        if (ObjectUtils.isEmpty(chatReq.getId())) {
            chatMapper.insertSelective(chat);
        } else {
            //更新数据
            chatMapper.updateByPrimaryKeySelective(chat);
        }
    }

    //删除数据
    public void delete(Integer id) {
        //删除数据
        chatMapper.deleteByPrimaryKey(id);
    }

}
