package com.example.onlineexam.domain;

import java.util.Date;

public class ChatDetailed {
    private Integer id;

    private Integer userId;

    private Integer anotherId;

    private String content;

    private Integer userDel;

    private Integer anotherDel;

    private Integer withdraw;

    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAnotherId() {
        return anotherId;
    }

    public void setAnotherId(Integer anotherId) {
        this.anotherId = anotherId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserDel() {
        return userDel;
    }

    public void setUserDel(Integer userDel) {
        this.userDel = userDel;
    }

    public Integer getAnotherDel() {
        return anotherDel;
    }

    public void setAnotherDel(Integer anotherDel) {
        this.anotherDel = anotherDel;
    }

    public Integer getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Integer withdraw) {
        this.withdraw = withdraw;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", anotherId=").append(anotherId);
        sb.append(", content=").append(content);
        sb.append(", userDel=").append(userDel);
        sb.append(", anotherDel=").append(anotherDel);
        sb.append(", withdraw=").append(withdraw);
        sb.append(", time=").append(time);
        sb.append("]");
        return sb.toString();
    }
}