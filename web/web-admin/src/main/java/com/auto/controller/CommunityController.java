package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Community;
import com.auto.entity.Dict;
import com.auto.service.CommunityService;
import com.auto.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController {

    private static final String ACTION_COMMUNITY_INDEX = "community/index";
    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    //获取小区列表
    @RequestMapping
    public String toCommunityPage(@RequestParam Map<String,String> filters, Model model){

        //1.根据字典编码获取beijing的所以区域
        List<Dict> areaList = dictService.findDictListByDicCode("beijing");
        //2.获取分页 将分页数据储存到请求域
        PageInfo<Community> page = communityService.findPage(filters);
        model.addAttribute("page", page);

        //3.将dictList字典存到请求域
        model.addAttribute("areaList", areaList);

        //4.将filters存到请求域中
        if (filters.get("areaId") == null){
            filters.put("areaId", "0");
        }
        if (filters.get("plateId") == null){
            filters.put("plateId","0");
        }
        model.addAttribute("filters", filters);
        return ACTION_COMMUNITY_INDEX;
    }
}
