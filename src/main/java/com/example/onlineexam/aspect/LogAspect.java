package com.example.onlineexam.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;

import com.example.onlineexam.domain.OperateLog;
import com.example.onlineexam.mapper.OperateLogMapper;
import com.example.onlineexam.util.Mylog;
import com.example.onlineexam.util.RequestContext;
import com.example.onlineexam.util.SnowFlake;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Aspect
@Component
public class LogAspect {

    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);
    private static final ThreadLocal<OperateLog> OPERATE_LOG_THREAD_LOCAL = new ThreadLocal<>();
    /** 定义一个切点 */
    @Pointcut("execution(public * com.example.*.controller..*Controller.*(..))")
    public void controllerPointcut() {}

    @Resource
    private SnowFlake snowFlake;


    private OperateLog operateLog  =new OperateLog() ;

    @Autowired
    private OperateLogMapper operateLogMapper;
    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        long logId = snowFlake.nextId();
        // 增加日志流水号
        MDC.put("LOG_ID", String.valueOf(logId));
        operateLog.setLogId(logId);
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signatures = (MethodSignature)joinPoint.getSignature();
        //获取切入点的所有方法
        Method method = signatures.getMethod();
        //获取操作（获取方法注解上的类,比如Controller类中的 @Mylog(value="增加视频地址")）
        Mylog mylog = method.getAnnotation(Mylog.class);
        if(mylog!=null){
            String value = mylog.value();
            operateLog.setLogOperation(value);
        }
        // 打印请求信息
        LOG.info("------------- 开始 -------------");
        LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("类名方法: {}.{}", signature.getDeclaringTypeName(), name);
        LOG.info("远程地址: {}", request.getRemoteAddr());
        //Reids的使用
        //获取IP地址
        RequestContext.setRemoteAddr(getRemoteIp(request));
        // 打印请求参数
        Object[] args = joinPoint.getArgs();
        // LOG.info("请求参数: {}", JSONObject.toJSONString(args));

        Object[] arguments  = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        // 排除字段，敏感字段或太长的字段不显示
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("请求参数: {}", JSONObject.toJSONString(arguments, excludefilter));

        operateLog.setLogIp(request.getRemoteAddr());
        operateLog.setLogParams(JSONObject.toJSONString(arguments, excludefilter));
        operateLog.setLogMethod(request.getMethod());
        operateLog.setLogUrl(request.getRequestURL().toString());
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        operateLog.setLogStartdate(String.valueOf(startTime));
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("返回结果: {}", JSONObject.toJSONString(result, excludefilter));
        LOG.info("------------- 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
        //储存结果
        operateLog.setLogResult(JSONObject.toJSONString(result, excludefilter));
        //结束时间
        operateLog.setLogEnddate(String.valueOf(System.currentTimeMillis() - startTime));
        //耗时
        operateLog.setLogResponsetime(String.valueOf(System.currentTimeMillis() - startTime));
        operateLog.setLogOperationtime(new Date());
//        try {
//            // 检查是否已经存在（防止重复插入）
//            if (operateLog.getLogId() != null &&
//                    operateLogMapper.selectByPrimaryKey(operateLog.getLogId()) == null) {
//                operateLogMapper.insertSelective(operateLog);
//                LOG.debug("操作日志保存成功，ID: {}", operateLog.getLogId());
//            } else {
//                LOG.warn("操作日志已存在或ID为空，跳过保存。ID: {}", operateLog.getLogId());
//            }
//        } catch (DuplicateKeyException e) {
//            LOG.warn("主键冲突，跳过保存。ID: {}", operateLog.getLogId());
//        } catch (Exception e) {
//            LOG.error("保存操作日志失败", e);
//        }
        return result;
    }

    /**
     * 使用nginx做反向代理，需要用该方法才能取到真实的远程IP
     * @param request
     * @return
     */
    public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
