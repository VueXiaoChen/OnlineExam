package com.example.onlineexam.req;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChatDetailedReq extends PageReq{

    /**
     * 唯一主键
     */
    private Integer id;

    /**
     * 消息发送者
     */

    private Integer userId;

    /**
     * 消息接收者
     */

    private Integer anotherId;

    /**
     * 消息内容
     */

    private String content;

    /**
     * 发送者是否删除
     */

    private Integer userDel;

    /**
     * 接受者是否删除
     */

    private Integer anotherDel;

    /**
     * 是否撤回
     */

    private Integer withdraw;

    /**
     * 消息发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

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
