package com.example.onlineexam.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.UsersLoadingResp;
import com.example.onlineexam.util.LoginUserContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;


/**
 * 拦截器：Spring框架特有的，常用于登录校验，权限校验，请求日志打印
 */
@Component
public class ActionInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ActionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // OPTIONS请求不做校验,
        // 前后端分离的架构, 前端会发一个OPTIONS请求先做预检, 对预检请求不做校验
        if("OPTIONS".equals(request.getMethod().toUpperCase())){
            return true;
        }

        UsersLoadingResp usersLoadingResp= LoginUserContext.getUser();
        if ("admin".equals(usersLoadingResp.getUsername())) {
            // admin用户不拦截
            return true;
        }
        LOG.info("操作被拦截");
        response.setStatus(HttpStatus.OK.value());
        CommonResp commonResp = new CommonResp();
        commonResp.setSuccess(false);
        commonResp.setMessage("哈哈，操作被拦截了，你就当操作成功了！示例网站暂不开放增删改操作");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONObject.toJSON(commonResp));
        return false;
    }

}
