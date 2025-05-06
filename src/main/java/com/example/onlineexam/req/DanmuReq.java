package com.example.onlineexam.req;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DanmuReq extends PageReq{

    /**
     * 弹幕ID
     */
    private Integer id;

    /**
     * 视频ID
     */

    private Integer vid;

    /**
     * 用户ID
     */

    private Integer uid;

    /**
     * 弹幕内容
     */

    private String content;

    /**
     * 字体大小
     */

    private Integer fontsize;

    /**
     * 弹幕模式 1滚动 2顶部 3底部
     */

    private Integer mode;

    /**
     * 弹幕颜色 6位十六进制标准格式
     */

    private String color;

    /**
     * 弹幕所在视频的时间点
     */

    private String timePoint;

    /**
     * 弹幕状态 1默认过审 2被举报审核中 3删除
     */

    private Integer state;

    /**
     * 发送弹幕的日期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFontsize() {
        return fontsize;
    }

    public void setFontsize(Integer fontsize) {
        this.fontsize = fontsize;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", vid=").append(vid);
        sb.append(", uid=").append(uid);
        sb.append(", content=").append(content);
        sb.append(", fontsize=").append(fontsize);
        sb.append(", mode=").append(mode);
        sb.append(", color=").append(color);
        sb.append(", timePoint=").append(timePoint);
        sb.append(", state=").append(state);
        sb.append(", createDate=").append(createDate);
        sb.append("]");
        return sb.toString();
    }
}
