package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.vo.HouseQueryVo;
import com.auto.entity.vo.HouseVo;
import com.auto.result.Result;
import com.auto.service.HouseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台 首页显示
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;

    /**
     * 查询前台首页信息
     * @param pageNum
     * @param pageSize
     * @param houseQueryVo
     * @return
     */
    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result findFrontIndexPage(@PathVariable("pageNum") Integer pageNum,
                                     @PathVariable("pageSize") Integer pageSize,
                                     @RequestBody HouseQueryVo houseQueryVo){

        //1.根据条件查询HouseVo
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum,pageSize,houseQueryVo);
        return Result.ok(pageInfo);
    }


    /**
     * 查看房源详情页
     */

}
