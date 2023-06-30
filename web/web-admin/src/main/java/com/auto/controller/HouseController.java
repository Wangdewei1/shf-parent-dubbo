package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.en.DictCode;
import com.auto.entity.Dict;
import com.auto.entity.House;
import com.auto.service.CommunityService;
import com.auto.service.DictService;
import com.auto.service.HouseService;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 房源管理
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    //加@RequestParamter 是防止Map被当成逻辑视图
    @RequestMapping
    public String toHousePage(@RequestParam Map<String,String> filters, Model model){
        //1.
        PageInfo<House> page = houseService.findPage(filters);
        model.addAttribute("page", page);

        List<Dict> dictList = dictService.findDictListByDicCode(DictCode.BEIJING.getCode());
        model.addAttribute("dictList",dictList);


        return null;
    }
}
