package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Admin;
import com.auto.entity.Permission;
import com.auto.entity.Role;
import com.auto.service.AclAdminService;
import com.auto.service.AclPermissionService;
import com.auto.service.AclRoleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 管理主页
 */
@Controller
public class IndexController {
    @Reference
    private AclAdminService aclAdminService;

    @Reference
    private AclRoleService aclRoleService;

    @Reference
    private AclPermissionService aclPermissionService;

    public static final String INDEX_PAGE = "frame/index";

    @RequestMapping("/")
    public String index(Model model){

        //1.查询用户信息
        //获取SpringSecurity中的 User对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //根据用户名查询 用户信息
        Admin admin = aclAdminService.getUserByUsername(user.getUsername());
        model.addAttribute("admin", admin);
        //2.查询所有角色列表
        List<Role> roleList = aclRoleService.findRoleListByAdminId(admin.getId());
        model.addAttribute("roleList", roleList);
        //3.根据用户Id查询所有权限列表

        List<Permission> permissionList = aclPermissionService.findPermissionListByAdminId(admin.getId());
        model.addAttribute("permissionList", permissionList);
        return INDEX_PAGE;
    }
}
