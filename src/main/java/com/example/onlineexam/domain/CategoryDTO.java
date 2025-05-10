package com.example.onlineexam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


public class CategoryDTO {
    private String mcId;
    private String mcName;
    private List<Map<String, Object>> scList;

    public String getMcId() {
        return mcId;
    }

    public void setMcId(String mcId) {
        this.mcId = mcId;
    }

    public String getMcName() {
        return mcName;
    }

    public void setMcName(String mcName) {
        this.mcName = mcName;
    }

    public List<Map<String, Object>> getScList() {
        return scList;
    }

    public void setScList(List<Map<String, Object>> scList) {
        this.scList = scList;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "mcId='" + mcId + '\'' +
                ", mcName='" + mcName + '\'' +
                ", scList=" + scList +
                '}';
    }
}
