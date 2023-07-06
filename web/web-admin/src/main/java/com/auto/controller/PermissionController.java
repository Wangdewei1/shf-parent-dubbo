package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Permission;
import com.auto.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    private static final String PAGE_CREATE = "permission/create";

    public static final String PAGE_SUCCESS = "common/successPage";

    private static final String LIST_ACTION = "redirect:/permission";

    private static final String PAGE_EDIT = "permission/edit";


    /**
     * 菜单功能
     * @param model
     * @return
     */
    @GetMapping
    public String index(Model model){
        List<Permission> permissionList = permissionService.findAll();
        model.addAttribute("list",permissionList);
        return PAGE_INDEX;
    }

    /**
     *  到 新增菜单页面
     */
    @GetMapping("/create")
    public String create(Permission permission,Model model){
        model.addAttribute("permission",permission);
        return PAGE_CREATE;
    }

    /**
     * 保存 菜单数据
     */
    @PostMapping("/save")
    public String save(Permission permission,Model model){
        //新增菜单
        permissionService.insert(permission);
        model.addAttribute("messagePage","新增菜单成功");
        return PAGE_SUCCESS;
    }

    /**
     * 删除
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        permissionService.delete(id);
        return LIST_ACTION;
    }

    /**
     * 显示修改页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return PAGE_EDIT;
    }

    /**
     * 保存修改信息
     */
    @PostMapping("/update")
    public String update(Permission permission,Model model){
        permissionService.update(permission);
        model.addAttribute("messagePage", "修改菜单成功");
        return PAGE_SUCCESS;
    }


}
