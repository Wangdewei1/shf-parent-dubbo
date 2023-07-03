package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.pojo.ResultHouseInfo;
import com.auto.entity.vo.HouseQueryVo;
import com.auto.entity.vo.HouseVo;
import com.auto.result.Result;
import com.auto.service.*;
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

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private CommunityService communityService;

    @Reference
    private HouseUserService houseUserService;

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
    @GetMapping("/info/{id}")
    public Result toHouseInfoPage(@PathVariable("id") Long houseId){

        //封装查寻房源详情的方法
        ResultHouseInfo resultHouseInfo = houseService.findHouseInfos(houseId);
        return Result.ok(resultHouseInfo);
    }
}
