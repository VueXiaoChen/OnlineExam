package com.example.onlineexam.resp;

import java.io.Serializable;

//序列化，使用redis之后需要序列化
public class UsersLoadingResp implements Serializable {
    private Long uid;

    private String username;

    private Integer code = 0;
    private String token;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UsersLoadingResp{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", code=" + code +
                ", token='" + token + '\'' +
                '}';
    }
}