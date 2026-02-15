package com.example.onlineexam.service;

import com.example.onlineexam.websocket.WebSocketServer;
import jakarta.annotation.Resource;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class WebSocsService {
    //打印日志
    /*private static final Logger LOG = (Logger) LoggerFactory.getLogger(UserService.class);*/
    @Resource
    public WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message,String logId){
        MDC.put("LOG_ID",logId);
        webSocketServer.sendInfo(message);
    }

    @Async
    public void sendtoUser(String message,String logId,String token) throws IOException {
        MDC.put("LOG_ID",logId);
        webSocketServer.sendtoUser(message,token);
    }
    @Async
    public void sendtoUsers(String message,String token) throws IOException {
        webSocketServer.sendtoUser(message,token);
    }
}
