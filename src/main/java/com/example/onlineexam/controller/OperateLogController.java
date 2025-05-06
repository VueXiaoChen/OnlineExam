package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.OperateLogMapper;
import com.example.onlineexam.req.OperateLogReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.OperateLogResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.OperateLogService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
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

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody OperateLogReq operateLogReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        operateLogService.save(operateLogReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(operateLogReq.getLogId())) {

            resp.setMessage("保存成功");
        } else {

            resp.setMessage("修改成功");
        }
        //将信息添加到返回信息里
        return resp;
    }

    //单个删除
    @GetMapping("/delete/{id}")
    //@PathVariable与{blogId}是绑定的
    public CommonResp delete(@PathVariable Long id) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //删除数据
        operateLogService.delete(id);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }
}
