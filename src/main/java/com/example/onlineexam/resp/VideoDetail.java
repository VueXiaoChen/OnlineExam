package com.example.onlineexam.resp;

import com.example.onlineexam.domain.User;
import com.example.onlineexam.domain.Video;

public class VideoDetail {
    private Video video;
    private User user;


    public VideoDetail(Video video, User user) {
        this.video = video;
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "VideoDetail{" +
                "video=" + video +
                ", user=" + user +
                '}';
    }
}
