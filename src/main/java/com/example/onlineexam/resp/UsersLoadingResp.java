package com.example.onlineexam.resp;

import java.io.Serializable;

//序列化，使用redis之后需要序列化
public class UsersLoadingResp implements Serializable {
    private Long userId;

    private String userName;

    private Integer code = 0;
    private String token;
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

    public Long getUserid() {
        return userId;
    }

    public void setUserid(Long userid) {
        this.userId = userid;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }
    @Override
    public String toString() {
        return "UserLoadingResp{" +
                "userid=" + userId +
                ", username='" + userName + '\'' +
                ", code=" + code +
                ", token='" + token + '\'' +
                '}';
    }
}