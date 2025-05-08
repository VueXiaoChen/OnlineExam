package com.example.onlineexam.domain;

import java.util.Date;
import java.util.List;


public class CommentTree {
    private Integer id;
    private Integer vid;
    private Integer rootId;
    private Integer parentId;
    private String content;
    private UserDTO user;
    private UserDTO toUser;
    private Integer love;
    private Integer bad;
    private List<CommentTree> replies;
    private Date createTime;
    private Long count;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<CommentTree> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentTree> replies) {
        this.replies = replies;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public UserDTO getToUser() {
        return toUser;
    }

    public void setToUser(UserDTO toUser) {
        this.toUser = toUser;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CommentTree{" +
                "id=" + id +
                ", vid=" + vid +
                ", rootId=" + rootId +
                ", parentId=" + parentId +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", toUser=" + toUser +
                ", love=" + love +
                ", bad=" + bad +
                ", replies=" + replies +
                ", createTime=" + createTime +
                ", count=" + count +
                '}';
    }
}
