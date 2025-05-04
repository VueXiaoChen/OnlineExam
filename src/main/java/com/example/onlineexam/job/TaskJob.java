package com.example.onlineexam.job;

import com.alibaba.fastjson.JSON;
import com.example.onlineexam.util.JobClient;
import jakarta.annotation.Resource;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;



public class TaskJob implements Job {

    @Resource
    public RedisTemplate redisTemplate;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取参数
        String param = jobExecutionContext.getJobDetail().getJobDataMap().getString(JobClient.PARAM_KEY);
        System.out.println("定时任务执行:" + param);
        //定时所有玩家发送通知
        //redisTemplate.convertAndSend(RedisCode.TOPIC_SENDALL, JSON.parseObject(param));
        //定时指定玩家发送通知(param是参数)
        //redisTemplate.convertAndSend(RedisCode.TOPIC_SENDALL, JSON.parseObject(param));
    }
}
