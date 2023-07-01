package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.en.DictCode;
import com.auto.entity.Community;
import com.auto.entity.Dict;
import com.auto.entity.House;
import com.auto.service.CommunityService;
import com.auto.service.DictService;
import com.auto.service.HouseService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 房源管理
 */
@Controller
@RequestMapping("/house")
public class HouseController {

    private static final String ACTION_HOUSE_INDEX_PAGE = "house/index";
    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    //加@RequestParamter 是防止Map被当成逻辑视图
    @RequestMapping
    public String toHousePage(@RequestParam Map<String,String> filters, Model model){
        //1.分页搜索房源列表 并存到请求域中
        PageInfo<House> page = houseService.findPage(filters);
        model.addAttribute("page", page);
        model.addAttribute("filters", filters);
        //2.查询所有小区以及字典里的数据 并存到请求域中
        extractedHouseView(model);
        return ACTION_HOUSE_INDEX_PAGE;
    }

    /**
     * 抽取
     * @param model
     */
    private void extractedHouseView(Model model) {
        //3.查询所有小区信息
        List<Community> communityList = communityService.findAll();
        model.addAttribute("communityList",communityList);
        //4.查询所有户型，根据父节点的dictCode 查询子节点列表 并存到请求域  作用
        List<Dict> houseTypeList = dictService.findDictListByDicCode(DictCode.HOUSETYPE.getCode());
        model.addAttribute("houseTypeList",houseTypeList);
        //5.查询所有楼层  ，并储存到请求域
        List<Dict> floorList = dictService.findDictListByDicCode(DictCode.FLOOR.getCode());
        model.addAttribute("floorList", floorList);
        //6.查询所有建筑结构，并存到请求域
        List<Dict> buildStructureList = dictService.findDictListByDicCode(DictCode.BUILDSTRUCTURE.getCode());
        model.addAttribute("buildStructureList", buildStructureList);
        //7.查看所有朝向，并存到请求域
        List<Dict> directionList = dictService.findDictListByDicCode(DictCode.DIRECTION.getCode());
        model.addAttribute("directionList", directionList);
        //8.查看所有装修情况
        List<Dict> decorationList = dictService.findDictListByDicCode(DictCode.DECORATION.getCode());
        model.addAttribute("decorationList", decorationList);
        //9.查询所有房屋用途
        List<Dict> houseUseList = dictService.findDictListByDicCode(DictCode.HOUSEUSE.getCode());
        model.addAttribute("houseUseList", houseUseList);
    }
}
