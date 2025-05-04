package com.example.onlineexam.util;

import com.example.onlineexam.exception.BusinessException;
import com.example.onlineexam.exception.BusinessExceptionCode;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobClient {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(JobClient.class);
    /**
     * 参数传递key
     */
    public static final String PARAM_KEY = "params";

    /**
     * 程序调度器
     */
    private final Scheduler scheduler;

    /**
     * 注入监听器
     */
    public JobClient(Scheduler scheduler) throws SchedulerException {

        this.scheduler = scheduler;
    }

    /**
     * 添加定时任务
     */
    public void add(JobParamVO vo) {

        try {

            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(vo.getClassName()).getClass()).withIdentity(String.valueOf(vo.getId())).usingJobData(PARAM_KEY, vo.getParam()).build();
            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(vo.getCronExpression());
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(String.valueOf(vo.getId())).withSchedule(scheduleBuilder).build();
            // 创建定时任务
            scheduler.scheduleJob(jobDetail, trigger);
            LOG.info("状态1 : {}",vo.getClassName());
            LOG.info("状态 : {}",vo.getStatus());
            // 停止
            if (vo.getStatus().equals(true)) {
                stop(String.valueOf(vo.getId()));
            }
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionCode.ADDJOBERROE);
            //throw new CustomException("添加定时任务失败：" + e.getMessage());
        }
    }

    /**
     * 编辑定时任务
     */
    public void update(JobParamVO vo) {

        try {

            // 判断是否存在，存在先删除
            if (scheduler.checkExists(JobKey.jobKey(String.valueOf(vo.getId())))) {

                scheduler.deleteJob(JobKey.jobKey(String.valueOf(vo.getId())));
            }
            // 再创建
            add(vo);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionCode.UPDATEJOBERROE);
            //throw new CustomException("修改定时任务失败：" + e.getMessage());
        }
    }

    /**
     * 暂停任务
     */
    public void stop(String id) {
        try {
            // 判断是否存在
            if (scheduler.checkExists(JobKey.jobKey(id))) {

                scheduler.pauseJob(JobKey.jobKey(id));
            }
        } catch (SchedulerException e) {
            // 暂停定时任务失败
            throw new BusinessException(BusinessExceptionCode.PAUSEOBERROE);
            //throw new CustomException("暂停定时任务失败：" + e.getMessage());
        }
    }

    /**
     * 恢复任务
     */
    public void start(String id, JobParamVO jobParamVO) {
        try {

            // 判断是否存在
            if (!scheduler.checkExists(JobKey.jobKey(id))) {

                // 不存在，先添加
                add(jobParamVO);
            }
            // 再启动
            scheduler.resumeJob(JobKey.jobKey(id));
        } catch (SchedulerException e) {
            throw new BusinessException(BusinessExceptionCode.STARTJOBERROE);
            // 暂停定时任务失败
            //throw new CustomException("启动定时任务失败：" + e.getMessage());
        }
    }

    /**
     * 立即执行一次
     */
    public void run(String id, JobParamVO jobParamVO) {

        try {

            // 判断是否存在
            if (!scheduler.checkExists(JobKey.jobKey(id))) {

                // 不存在，先添加
                add(jobParamVO);
            }
            // 再执行
            scheduler.triggerJob(JobKey.jobKey(id));
        } catch (SchedulerException e) {
            throw new BusinessException(BusinessExceptionCode.EXECUTEJOBERROE);
            // 暂停定时任务失败
            //throw new CustomException("执行定时任务失败：" + e.getMessage());
        }
    }

    /**
     * 删除定时任务
     */
    public void delete(Long id) {

        try {

            // 停止触发器
            scheduler.pauseTrigger(TriggerKey.triggerKey(String.valueOf(id)));
            // 移除触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(String.valueOf(id)));
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(String.valueOf(id)));
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionCode.DELETEJOBERROE);
            //throw new CustomException("执行定时任务失败：" + e.getMessage());
        }
    }

    public static Job getClass(String className) throws Exception{
        Class<?> classname = Class.forName(className);
        return (Job)classname.getDeclaredConstructor().newInstance();
    }
}
