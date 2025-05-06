package com.example.onlineexam.req;

import java.io.Serializable;

//序列化，使用redis之后需要序列化
public class UsersLoadingReq implements Serializable {
    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户密码
     */

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UsersLoadingReq{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}