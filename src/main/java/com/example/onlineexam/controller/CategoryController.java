package com.example.onlineexam.controller;


import com.example.onlineexam.mapper.CategoryMapper;
import com.example.onlineexam.req.CategoryReq;
import com.example.onlineexam.resp.CommonResp;
import com.example.onlineexam.resp.CategoryResp;
import com.example.onlineexam.resp.PageResp;
import com.example.onlineexam.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;
    @GetMapping("/list")
    //@Valid  开启参数检验
    public CommonResp list(@Validated CategoryReq categoryReq) {
        //返回信息里面定义返回的类型
        CommonResp<PageResp<CategoryResp>> resp = new CommonResp<>();
        //接收数据库返回的数据
        PageResp<CategoryResp> data = categoryService.list(categoryReq);
        //将信息添加到返回信息里
        resp.setMessage("获取成功");
        //将信息添加到返回信息里
        resp.setData(data);
        return resp;
    }

    @PostMapping("/save")
    //@RequestBody  定义传过来的参数是实体类
    public CommonResp save(@Validated @RequestBody CategoryReq categoryReq) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //保存数据
        categoryService.save(categoryReq);
        //将信息添加到返回信息里
        if (ObjectUtils.isEmpty(categoryReq.getMcId())) {

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
    public CommonResp delete(@PathVariable String mcId, @PathVariable String scId) {
        //返回信息里面定义返回的类型
        CommonResp resp = new CommonResp<>();
        //删除数据
        categoryService.delete(mcId,scId);
        //将信息添加到返回信息里
        resp.setMessage("删除成功");
        resp.setData("");
        return resp;
    }
    /**
     * 获取全部分区接口
     * @return CustomResponse对象
     */
    @GetMapping("/getall")
    public CommonResp getAll() {
        return categoryService.getAll();
    }
}
