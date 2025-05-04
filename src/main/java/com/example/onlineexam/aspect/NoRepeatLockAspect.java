package com.example.onlineexam.aspect;

import com.alibaba.fastjson.JSON;

import com.example.onlineexam.exception.BusinessException;
import com.example.onlineexam.exception.BusinessExceptionCode;
import com.example.onlineexam.util.NoRepeatLock;
import com.example.onlineexam.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Aspect
@Component

public class NoRepeatLockAspect {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(NoRepeatLockAspect.class);

    @Resource
    public RedisUtil redisUtil;
    @Pointcut(value = "@annotation(noRepeatLock)")
    public void PointCutNoRepeatLock(NoRepeatLock noRepeatLock) {}

    @Around(value = "PointCutNoRepeatLock(noRepeatLock)",argNames = "joinPoint,noRepeatLock")
    public Object around(ProceedingJoinPoint joinPoint,NoRepeatLock noRepeatLock) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) attributes.getRequest();
        //传递参数的类型
        String type = noRepeatLock.limitType().name();
        //默认的key
        String key = ":noRepeat_Lock";
        //网址
        String url = request.getRequestURI();
        //IP地址
        String ipAddress = request.getRemoteAddr();
        //判断是否是参数传递还是token传递方式

        LOG.info(String.valueOf(noRepeatLock.lockTime()));
        if (type.equalsIgnoreCase(NoRepeatLock.Type.PARAM.name())) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            //获取方法
            Method method = methodSignature.getMethod();
            //获取方法名
            String className = method.getDeclaringClass().getName();
            //获取参数
            LOG.info("测试数据：{}",JSON.toJSONString(joinPoint.getArgs()));
            key = key + "keys"+String.format("%s-%s-%s-%s", ipAddress, url, className, method);
        } else {
            String requestToken = request.getHeader("token");
            if (ObjectUtils.isEmpty((requestToken))) {
                return "token不存在,非法请求";
            }
            key = key+String.format("%s-%s",requestToken,url);
        }
        //将key进行MD5加密
        key = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
        if(!redisUtil.validateRepeat(key,noRepeatLock.lockTime())){
            LOG.info("不要重复提交");
            throw new BusinessException(BusinessExceptionCode.NO_Repeated_Submission);
        }
        /*if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            LOG.info("11111111111");
            redisTemplate.opsForValue().setIfAbsent(key, noRepeatLock.lockTime(), Duration.ofSeconds(3));
            try {
                result = joinPoint.proceed();
            } catch (Throwable throwable) {
                redisTemplate.delete(key);
                return "处理异常,请重试";
            }
        }else{
            LOG.info("2222");
        }*/
        return joinPoint.proceed();
    }
}
