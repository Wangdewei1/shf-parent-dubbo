package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Admin;
import com.auto.service.AclAdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户 管理模块
 */
@Controller
@RequestMapping("/admin")
public class AclAdminController {
    private static final String ADMIN_PAGE_USER = "admin/index";
    private static final String ACCTION_ADD_USER = "admin/create";
    //操作成功页面
    public static final String PAGE_SUCCESS = "common/successPage";
    private static final String ACCTION_EDIT_USER = "admin/edit";
    private static final String REDIRECT_ADMIN_INDEX = "redirect:/admin";

    @Reference
    private AclAdminService aclAdminService;

    /**
     * 查询所有 根据条件
     */
    @RequestMapping
    public String findAdminPage(@RequestParam Map<String,String> filters , Model model){
        PageInfo<Admin> page = aclAdminService.findPage(filters);
        model.addAttribute("page", page);
        model.addAttribute("filters", filters);
        return ADMIN_PAGE_USER;
    }

    /**
     * 新增页面
     */
    @GetMapping("/create")
    public String toAddAdminPage(){
        return ACCTION_ADD_USER;
    }

    /**
     * 添加用户
     */
    @PostMapping("/save")
    public String addAdmin(Admin admin , Model model){
        aclAdminService.insert(admin);
        model.addAttribute("messagePage", "用户添加成功");
        return PAGE_SUCCESS;
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{id}")
    public String toEditAdmin(@PathVariable Long id , Model model){
        Admin admin = aclAdminService.getById(id);
        model.addAttribute("admin", admin);
        return ACCTION_EDIT_USER;
    }

    /**
     * 提交修改用户信息
     */
    @PostMapping("/update")
    public String updateAdmin(Admin admin,Model model){
        aclAdminService.update(admin);
        model.addAttribute("messagePage", "用户修改成功");
        return PAGE_SUCCESS;
    }

    /**
     * 删除用户
     */
    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Long id){
        aclAdminService.delete(id);
        return REDIRECT_ADMIN_INDEX;
    }
}
