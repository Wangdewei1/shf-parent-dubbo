package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Community;
import com.auto.entity.Dict;
import com.auto.service.CommunityService;
import com.auto.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController {

    private static final String ACTION_COMMUNITY_INDEX = "community/index";
    private static final String ACTION_COMMUNITY_ADD = "community/create";

    public static final String PAGE_SUCCESS = "common/successPage";
    private static final String ACTION_COMMUNITY_EDIT = "community/edit";
    private static final String ACTION_COMMUNITY_DELETE = "redirect:/community";
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

    //新增小区
    @RequestMapping("/create")
    public String toCreatePage(Model model){
        //1.
        List<Dict> dictList = dictService.findDictListByDicCode("beijing");
        //2.
        model.addAttribute("dictList", dictList);
        //3.
        return ACTION_COMMUNITY_ADD;
    }

    //保存小区信息
    @PostMapping("/save")
    public String savaCommunity(Community community,Model model){
        communityService.insert(community);
        model.addAttribute("messagePage", "添加小区成功");
        return PAGE_SUCCESS;
    }

    //修改小区
    @GetMapping("/edit/{id}")
    public String toEditPage(@PathVariable("id") Long id ,  Model model){
        //查询小区信息
        Community community = communityService.getById(id);

        model.addAttribute("community",community);

        //获取字典的作用  获取区域列表
        List<Dict> areaList = dictService.findDictListByDicCode("beijing");

        model.addAttribute("areaList",areaList);

        return ACTION_COMMUNITY_EDIT;
    }
    
    //保存修改小区信息
    @PostMapping("/update")
    public String updateCommunity(Community community,Model model){
        communityService.update(community);
        model.addAttribute("messagePage", "修改小区信息成功");
        return PAGE_SUCCESS;
    }

    //删除小区信息
    @GetMapping("/delete/{id}")
    public String deleteCommunity(@PathVariable("id") Long id){
        communityService.delete(id);
        return ACTION_COMMUNITY_DELETE;
    }
}
