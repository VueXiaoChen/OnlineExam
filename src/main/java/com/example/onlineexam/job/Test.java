package com.example.onlineexam.job;


import com.example.onlineexam.generator.util.DbUtil;
import com.example.onlineexam.generator.util.Field;
import com.example.onlineexam.service.CommentService;
import com.example.onlineexam.util.RedisUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class Test {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(Test.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;
    /*private static final Logger LOG = LoggerFactory.getLogger(Test.class);
    @Resource
    private WebSocsService webSocsService;
    @Scheduled(cron = "0/5 * * * * ?")
    public void cron() throws InterruptedException{
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        String dateString =format.format(new Date());
        webSocsService.sendInfo("我是测试webSocket的","55555555555");
        LOG.info("每隔5秒执行一次:{}",dateString);
    }*/


}
