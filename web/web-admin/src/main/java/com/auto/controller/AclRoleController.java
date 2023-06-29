package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.Role;
import com.auto.service.AclRoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/role")
public class AclRoleController {
    private static final String ROLE_UPDATE_PAGE = "role/edit";

    public static final String INDEX_VIEW = "role/index";

    public static final String INDEX_PAGE = "frame/index";

    //重定向角色管理页面
    public static final String REDIRECT_INDEX_ROLE_PAGE = "redirect:/role";

    public static final String PAGE_SUCCESS = "common/successPage";

    public static final String ADD_ROLE_PAGE = "role/create";

    @Reference
    private AclRoleService aclRoleService;

    @GetMapping("/frame")
    public String toIndexPage(){
        return INDEX_PAGE;
    }

    @RequestMapping
    public String findRolePage(Model model, @RequestParam Map<String,String> filters){
        //清理分页缓存
//        PageHelper.clearPage();
        PageInfo<Role> page = aclRoleService.findPage(filters);
        model.addAttribute("page",page);
        model.addAttribute("filters", filters);
        return INDEX_VIEW;
    }

    @GetMapping("/create")
    public String toAddRole(){
        return ADD_ROLE_PAGE;
    }

    @PostMapping("/save")
    public String saveAddRole(@Validated(Role.class) Role role, Model model){
        aclRoleService.insert(role);
        model.addAttribute("messagePage","用户添加成功");
        return PAGE_SUCCESS;
    }

    @GetMapping("/edit/{id}")
    public String toEditRole(@PathVariable("id") Long id,Model model){
        Role role = aclRoleService.getById(id);
        model.addAttribute("role", role);
        return ROLE_UPDATE_PAGE;
    }

    @PostMapping("/update")
    public String updateRole(@Validated(Role.class) Role role , Model model){
        aclRoleService.update(role);
        model.addAttribute("messagePage", "修改角色成功");
        return PAGE_SUCCESS;
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id){
        aclRoleService.delete(id);
        return REDIRECT_INDEX_ROLE_PAGE;
    }

}
