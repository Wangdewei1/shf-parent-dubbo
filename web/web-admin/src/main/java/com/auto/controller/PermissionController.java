package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Permission;
import com.auto.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 菜单管理
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    private final static String PAGE_INDEX = "permission/index";

    @GetMapping
    public String index(Model model){
        List<Permission> permissionList = permissionService.findAll();
        model.addAttribute("list",permissionList);
        return PAGE_INDEX;
    }
}
