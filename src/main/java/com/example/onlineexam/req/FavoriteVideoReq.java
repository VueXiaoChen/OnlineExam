package com.example.onlineexam.req;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FavoriteVideoReq extends PageReq{

    /**
     * 唯一标识
     */
    private Integer id;

    /**
     * 视频ID
     */

    private Integer vid;

    /**
     * 收藏夹ID
     */

    private Integer fid;

    /**
     * 收藏时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

    private Date time;

    /**
     * 是否移除 null否 1已移除
     */
    private Integer isRemove;

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

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(Integer isRemove) {
        this.isRemove = isRemove;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", vid=").append(vid);
        sb.append(", fid=").append(fid);
        sb.append(", time=").append(time);
        sb.append(", isRemove=").append(isRemove);
        sb.append("]");
        return sb.toString();
    }
}
