package com.example.onlineexam.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {

    @GetMapping("favicon.ico")
    public ResponseEntity<Void> returnNoFavicon() {
        // 返回 204 No Content，告诉浏览器没有图标
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
