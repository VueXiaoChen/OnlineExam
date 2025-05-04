package com.example.onlineexam.config;

import org.springframework.stereotype.Component;

@Component
public class GlobalConfig {
    private String UserName;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GlobalConfig{");
        sb.append("UserName='").append(UserName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
