package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.result.Result;
import com.auto.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;

    @GetMapping("/findZnodes")
    @ResponseBody
    public Result findListByParentId(@RequestParam(value = "id",defaultValue = "0") Long id){

        //1.根据id获取字典  封装
        List<Map<String, Object>> zNodes = dictService.findListByParentId(id);
        //2.返回异步
        return Result.ok(zNodes);
    }


}
