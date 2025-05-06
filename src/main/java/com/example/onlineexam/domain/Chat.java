package com.example.onlineexam.domain;

import java.util.Date;

public class Chat {
    private Integer id;

    private Integer userId;

    private Integer anotherId;

    private Integer isDeleted;

    private Integer unread;

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