package com.example.onlineexam.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.onlineexam.req.WebMessageReq;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/{token}")
@Component

public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 每个客户端一个token
     */
    private String token = "";

    private static int onlineCount = 0;
    private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();
    private static CopyOnWriteArraySet<WebSocketServer> webSockets =new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接成功
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;
        this.token = token;//接收到发送消息的人员编号
        webSocketSet.put(token, this);     //加入set中
        webSockets.add(this);
        addOnlineCount();           //在线数加1
        LOG.info("用户"+token+"加入！当前在线人数为" + getOnlineCount());
        try {
            WebMessageReq webMessageReq = new WebMessageReq();
            webMessageReq.setType("4");
            webMessageReq.setComment("用户"+token+"连接成功");
            sendMessage(JSON.toJSONString(webMessageReq));
        } catch (IOException e) {
            LOG.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session) {
        webSocketSet.remove(this.token);
        subOnlineCount();           //在线数减1
        LOG.info("连接关闭，token：{}，session id：{}！当前连接数：{}", this.token, session.getId(), getOnlineCount());
    }

    /**
     * 收到消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("收到消息：{}，内容：{}", token, message);
    }

    /**
     * 连接错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("发生错误", error);
        error.printStackTrace();
    }

    /**
     * 群发消息
     */
    public void sendInfo(String message) {
        for (String token : webSocketSet.keySet()) {
            try {
                webSocketSet.get(token).sendMessage(message);
            } catch (IOException e) {
                LOG.error("推送消息失败：{}，内容：{}", token, message);
            }
            LOG.info("推送消息：{}，内容：{}", token, message);
        }
    }
    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     * @param message
     * @param token
     * @throws IOException
     */
    public void sendtoUser(String message,String token) throws IOException {
        if (webSocketSet.get(token) != null) {
            if(!token.equals(token))
                webSocketSet.get(token).sendMessage( "用户" + token + "发来消息：" + " <br/> " + message);
            else
                webSocketSet.get(token).sendMessage(message);
        } else {
            //如果用户不在线则返回不在线信息给自己
            sendtoUser("当前用户不在线",token);
        }
    }
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
