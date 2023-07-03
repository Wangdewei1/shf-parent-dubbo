package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Dict;
import com.auto.result.Result;
import com.auto.service.DictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Reference
    private DictService dictService;


    /**
     * 根据dictCode 字典编码查询 主页导航信息
     * @param dictCode
     * @return
     */
    @GetMapping("/findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable("dictCode") String dictCode){
        List<Dict> dictList = dictService.findDictListByDicCode(dictCode);
        return Result.ok(dictList);
    }

    @GetMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId") Long parentId){
        List<Map<String, Object>> dictList = dictService.findListByParentId(parentId);
        return Result.ok(dictList);
    }
}
