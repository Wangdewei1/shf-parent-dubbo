package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.en.DictCode;
import com.auto.en.HouseImageType;
import com.auto.en.HouseStatus;
import com.auto.entity.*;
import com.auto.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 房源管理
 */
@Controller
@RequestMapping("/house")
public class HouseController {

    private static final String ACTION_HOUSE_INDEX_PAGE = "house/index";

    public static final String PAGE_SUCCESS = "common/successPage";

    private static final String PAGE_CREATE = "house/create";
    private static final String ACTION_HOUSE_EDIT_PAGE = "house/edit";

    private static final String REDIRECT_ACTION_HOUSE_DELETE = "redirect:/house";
    private static final String ACTION_SHOW_PAGE = "house/show";
    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseUserService houseUserService;

    //加@RequestParamter 是防止Map被当成逻辑视图
    @RequestMapping
    @PreAuthorize("hasAnyAuthority('house.show')")
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

    /**
     * 新增房源信息
     */

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('house.create')")
    public String toAddPage(Model model){
        //查询房源初始化信息储存到model
        extractedHouseView(model);
        return PAGE_CREATE;
    }

    /**
     * 保存添加房源信息
     */
    @PreAuthorize("hasAnyAuthority('house.createq')")
    @PostMapping("/save")
    public String save(House house,Model model){
        //未发布
        house.setStatus(HouseStatus.UNPUBLISHED.getCode());
        houseService.insert(house);
        model.addAttribute("messagePage", "添加房源成功");
        return PAGE_SUCCESS;
    }

    /**
     * 根据id修改
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('house.edit')")
    public String toEditPage(@PathVariable("id") Long id,Model model) {
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        extractedHouseView(model);
        return ACTION_HOUSE_EDIT_PAGE;
    }

    /**
     * 保存修改数据
     */
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('house.edit')")
    public String update(House house,Model model){
        houseService.update(house);
        model.addAttribute("messagePage", "修改房源信息成功");
        return PAGE_SUCCESS;
    }

    /**
     *根据id删除
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('house.delete')")
    public String delete(@PathVariable("id") Long id){
        houseService.delete(id);
        return REDIRECT_ACTION_HOUSE_DELETE;
    }

    /**
     * 新增发布
     */
    @GetMapping("/publish/{id}/{status}")
    @PreAuthorize("hasAnyAuthority('house.publish')")
    public String publish(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseService.update(house);
        return REDIRECT_ACTION_HOUSE_DELETE;
    }

    /**
     * 根据房源id查询房源图片信息
     */
    @GetMapping("/{houseId}")
    public String findHouseImageInfo(@PathVariable("houseId") Long houseId,
                                     Model model){

        //调用房源详情
        getHouseImageInfo(houseId, model);
        //7.返回到详情页面的逻辑视图
        return ACTION_SHOW_PAGE;
    }

    /**
     * 封装 房源详情信息
     * @param houseId
     * @param model
     */
    private void getHouseImageInfo(Long houseId, Model model) {
        //1.根据房源id查询房源信息 并保存到请求域中
        House house = houseService.getById(houseId);
        model.addAttribute("house",house);
        //2.根据小区id查询小区详情 并保存到请求域中
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("community",community);
        //3.分局房源id和房源图片类型 1.房源图片 2.房产图片  查询 房源图片列表 并保存到请求域中
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(houseId, HouseImageType.HOUSE_IMAGE_TYPE.getType());
        model.addAttribute("houseImage1List",houseImage1List);
        //4.分局房源id和房源图片类型 1.房源图片 2.房产图片  查询 房产图片列表 并保存到请求域中
        List<HouseImage> houseImage2List = houseImageService.findHouseImageList(houseId, HouseImageType.HOUSE_IMAGE_TYPE_PROPERTY.getType());
        model.addAttribute("houseImage2List",houseImage2List);
        //5.根据房源id查询经纪人 列表
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerList(houseId);
        model.addAttribute("houseBrokerList",houseBrokerList);
        //6.根据房源id查询房东信息  列表 并保存到请求域中
        List<HouseUser> houseUserList = houseUserService.findHouseUserList(houseId);
        model.addAttribute("houseUserList",houseUserList);
    }
}
