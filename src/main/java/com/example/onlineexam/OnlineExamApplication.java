package com.example.onlineexam;

import com.example.onlineexam.util.SnowFlake;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;

@SpringBootApplication
@MapperScan("com.example.onlineexam.mapper")
@EnableScheduling
@EnableAsync
@EnableCaching
public class OnlineExamApplication {


    private static final Logger LOG = LoggerFactory.getLogger(OnlineExamApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OnlineExamApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功");
        LOG.info("地址:\thttp://127.0.0.1:{}",env.getProperty("server.port"));

    }
}
