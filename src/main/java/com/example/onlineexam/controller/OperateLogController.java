package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.OperateLogMapper;
import com.example.onlineexam.req.OperateLogReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.OperateLogResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.OperateLogService;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/operateLog")
public class OperateLogController {
    @Resource
    private OperateLogService operateLogService;


    @Autowired
    private OperateLogMapper operateLogMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated OperateLogReq operateLogReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<OperateLogResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<OperateLogResp> data = operateLogService.list(operateLogReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }
}
