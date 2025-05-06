package com.example.onlineexam.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ChatResp {

    /**
     * 唯一主键
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Integer id;

    /**
     * 对象UID
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Integer userId;

    /**
     * 用户UID
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Integer anotherId;

    /**
     * 是否移除聊天 0否 1是
     */
    private Integer isDeleted;

    /**
     * 消息未读数量
     */
    private Integer unread;

    /**
     * 最近接收消息的时间或最近打开聊天窗口的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date latestTime;

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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getUnread() {
        return unread;
    }

    public void setUnread(Integer unread) {
        this.unread = unread;
    }

    public Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
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
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", unread=").append(unread);
        sb.append(", latestTime=").append(latestTime);
        sb.append("]");
        return sb.toString();
    }
}
