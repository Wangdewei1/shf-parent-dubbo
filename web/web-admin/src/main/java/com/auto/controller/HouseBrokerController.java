package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Admin;
import com.auto.entity.HouseBroker;
import com.auto.service.AclAdminService;
import com.auto.service.HouseBrokerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 房源经纪人管理模块
 */
@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {

    private static final String ACTION_ADD_BROKER = "houseBroker/create";

    public static final String PAGE_SUCCESS = "common/successPage";

    private static final String PAGE_EDIT = "houseBroker/edit";

    private static final String SHOW_ACTION = "redirect:/house/";
    @Reference
    private AclAdminService aclAdminService;

    @Reference
    private HouseBrokerService houseBrokerService;

    /**
     * 到新增经纪人页面
     * @param houseId
     * @param model
     * @return
     */
    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('house.editBroker')")
    public String toHouseBrokerPage(@RequestParam("houseId") Long houseId, Model model){
        //1.查询出不是当前房源的经纪人列表 并存到请求域中
        List<Admin> adminList = aclAdminService.findNotAdminListByHouseId(houseId);
        model.addAttribute("adminList",adminList);
        //2.将houseId存到请求域中
        model.addAttribute("houseId", houseId);
        //3.显示新增页面
        return ACTION_ADD_BROKER;
    }

    /**
     * 保存经纪人
     */
    @PostMapping("/save")
    public String addHouseBroker(HouseBroker houseBroker, Model model){
        //1.根据id查询
        Admin admin = aclAdminService.getById(houseBroker.getBrokerId());

        //2.封装
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());

        houseBrokerService.insert(houseBroker);

        model.addAttribute("messagePage", "添加经纪人成功");
        return PAGE_SUCCESS;
    }

    /**
     * 回显经纪人
     */

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('house.editBroker')")
    public String edit(@PathVariable("id") Long id, Model model){
        //1. 列出经纪人列表:不包含当前房源已关联的那些admin(除了当前id表示的这条经纪人数据)
        //1.1 根据id查询出当前房源经纪人信息
        HouseBroker houseBroker = houseBrokerService.getById(id);
        //1.2 根据houseId查询出当前房源未关联的admin列表(但是必须查出当前这条经纪人数据)
        List<Admin> adminList = aclAdminService.findNotAdminListByHouseId(houseBroker.getHouseId());
        //1.3 查询出当前经纪人信息,并添加到adminList
        Admin admin = aclAdminService.getById(houseBroker.getBrokerId());
        adminList.add(admin);
        //1.4 将adminList添加到请求域
        model.addAttribute("adminList",adminList);
        //2. 将当前经纪人信息存储到请求域
        model.addAttribute("houseBroker",houseBroker);
        return PAGE_EDIT;
    }

    /**
     * 保存经纪人
     * @param houseBroker
     * @param model
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('house.editBroker')")
    public String update(HouseBroker houseBroker,Model model){
        //此时houseBroker中有俩属性:id(经纪人数据的id字段,就是作为修改的条件),brokerId是要修改成经纪人的那个用户的id
        //2. 根据用户的id查询用户信息
        Admin admin = aclAdminService.getById(houseBroker.getBrokerId());
        //3. 修改经纪人其实是:根据经纪人的id作为条件，修改broker_id、broker_name、broker_head_url
        //设置houseBroker的brokerName和brokerHeadUrl
        if(admin != null){
            houseBroker.setBrokerName(admin.getName());
            houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        }
        //4. 调用业务层的方法修改
        houseBrokerService.update(houseBroker);
        //5. 显示成功页面
        model.addAttribute("messagePage", "修改经纪人信息成功");
        return PAGE_SUCCESS;
    }

    /**
     * 删除经纪人
     */
    @RequestMapping("/delete/{houseId}/{id}")
    @PreAuthorize("hasAnyAuthority('house.editBroker')")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){
        houseBrokerService.delete(id);

        return SHOW_ACTION + houseId;
    }

}
