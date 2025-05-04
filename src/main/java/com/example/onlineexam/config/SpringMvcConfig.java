package com.example.onlineexam.config;//
//
//package com.example.blog.config;
//
//import com.example.blog.interceptor.ActionInterceptor;
//import com.example.blog.interceptor.LoginInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//
//    @Resource
//    LoginInterceptor loginInterceptor;
//
//    @Resource
//    ActionInterceptor actionInterceptor;
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/test/**",
//                        "/redis/**",
//                        "/user/login",
//                        "/**/list",
//                        "/**/save",
//                        "/**/like",
//                        "/**/focus",
//                        "/**/collect"
//
//                );
//        //
//        registry.addInterceptor(actionInterceptor)
//                .addPathPatterns(
//                        "/*/save",
//                        "/*/delete/**",
//                        "/*/reset-password");
//    }
//}
//
