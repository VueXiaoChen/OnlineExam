package com.example.onlineexam.req;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserReq extends PageReq{

    /**
     * 用户ID
     */
    private Integer uid;
    /**
     * 用户账号
     */

    private String username;

    /**
     * 用户密码
     */

    private String password;

    /**
     * 用户昵称
     */

    private String nickname;

    /**
     * 性别 0女 1男 2未知
     */

    private Integer gender;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", username=").append(username);
        sb.append(", uid=").append(uid);
        sb.append(", password=").append(password);
        sb.append(", nickname=").append(nickname);
        sb.append(", gender=").append(gender);
        sb.append("]");
        return sb.toString();
    }
}
