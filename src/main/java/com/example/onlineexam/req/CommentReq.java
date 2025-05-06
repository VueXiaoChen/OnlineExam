package com.example.onlineexam.req;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentReq extends PageReq{

    /**
     * 评论主id
     */
    private Integer id;

    /**
     * 评论的视频id
     */

    private Integer vid;

    /**
     * 发送者id
     */
    private Integer uid;

    /**
     * 根节点评论的id,如果为0表示为根节点
     */

    private Integer rootId;

    /**
     * 被回复的评论id，只有root_id为0时才允许为0，表示根评论
     */

    private Integer parentId;

    /**
     * 回复目标用户id
     */

    private Integer toUserId;

    /**
     * 评论内容
     */

    private String content;

    /**
     * 该条评论的点赞数
     */

    private Integer love;

    /**
     * 不喜欢的数量
     */
    private Integer bad;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")

    private Date createTime;

    /**
     * 是否置顶 0普通 1置顶
     */

    private Integer isTop;

    /**
     * 软删除 0未删除 1已删除
     */

    private Integer isDeleted;

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

    public Integer getRootId() {
        return rootId;
    }

    public void setRootId(Integer rootId) {
        this.rootId = rootId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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
        sb.append(", rootId=").append(rootId);
        sb.append(", parentId=").append(parentId);
        sb.append(", toUserId=").append(toUserId);
        sb.append(", content=").append(content);
        sb.append(", love=").append(love);
        sb.append(", bad=").append(bad);
        sb.append(", createTime=").append(createTime);
        sb.append(", isTop=").append(isTop);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append("]");
        return sb.toString();
    }
}
