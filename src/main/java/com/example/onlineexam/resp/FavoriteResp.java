package com.example.onlineexam.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class FavoriteResp {

    /**
     * 收藏夹ID
     */
    private Integer fid;

    /**
     * 所属用户ID
     */
    private Integer uid;

    /**
     * 收藏夹类型 1默认收藏夹 2用户创建
     */
    private Integer type;

    /**
     * 对外开放 0隐藏 1公开
     */
    private Integer visible;

    /**
     * 收藏夹封面
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String description;

    /**
     * 收藏夹中视频数量
     */
    private Integer count;

    /**
     * 是否删除 0否 1已删除
     */
    private Integer isDelete;

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fid=").append(fid);
        sb.append(", uid=").append(uid);
        sb.append(", type=").append(type);
        sb.append(", visible=").append(visible);
        sb.append(", cover=").append(cover);
        sb.append(", title=").append(title);
        sb.append(", description=").append(description);
        sb.append(", count=").append(count);
        sb.append(", isDelete=").append(isDelete);
        sb.append("]");
        return sb.toString();
    }
}
