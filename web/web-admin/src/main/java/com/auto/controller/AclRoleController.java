package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.auto.entity.Role;
import com.auto.entity.pojo.ResultZNodesInfo;
import com.auto.service.AclPermissionService;
import com.auto.service.AclRoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";

    @Reference
    private AclRoleService aclRoleService;

    @Reference
    private AclPermissionService aclPermissionService;

    @GetMapping("/frame")
    public String toIndexPage(){
        return INDEX_PAGE;
    }

    @PreAuthorize("hasAnyAuthority('role.show')")
    @RequestMapping
    public String findRolePage(Model model, @RequestParam Map<String,String> filters){
        //清理分页缓存
//        PageHelper.clearPage();
        PageInfo<Role> page = aclRoleService.findPage(filters);
        model.addAttribute("page",page);
        model.addAttribute("filters", filters);
        return INDEX_VIEW;
    }
    @PreAuthorize("hasAnyAuthority('role.create')")
    @GetMapping("/create")
    public String toAddRole(){
        return ADD_ROLE_PAGE;
    }

    @PreAuthorize("hasAnyAuthority('role.create')")
    @PostMapping("/save")
    public String saveAddRole(@Validated(Role.class) Role role, Model model){
        aclRoleService.insert(role);
        model.addAttribute("messagePage","用户添加成功");
        return PAGE_SUCCESS;
    }

    @PreAuthorize("hasAnyAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String toEditRole(@PathVariable("id") Long id,Model model){
        Role role = aclRoleService.getById(id);
        model.addAttribute("role", role);
        return ROLE_UPDATE_PAGE;
    }

    @PreAuthorize("hasAnyAuthority('role.edit')")
    @PostMapping("/update")
    public String updateRole(@Validated(Role.class) Role role , Model model){
        aclRoleService.update(role);
        model.addAttribute("messagePage", "修改角色成功");
        return PAGE_SUCCESS;
    }

    @PreAuthorize("hasAnyAuthority('role.delete')")
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id){
        aclRoleService.delete(id);
        return REDIRECT_INDEX_ROLE_PAGE;
    }

    /**
     * 给角色分配权限
     */
    @PreAuthorize("hasAnyAuthority('role.assign')")
    @RequestMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long roleId, Model model){

        //1.根据角色id查询所有权限列表，并存到请求域中
        //注意储存到请求域中的是zNodes Json字符串
        List<ResultZNodesInfo> zNodes = aclPermissionService.findPermissionListByRoleId(roleId);

        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        //2.将角色id存到请求域中
        model.addAttribute("roleId",roleId);

        return PAGE_ASSIGN_SHOW;
    }

    @PreAuthorize("hasAnyAuthority('role.assign')")
    @PostMapping("/assignPermission")
    public String saveAssignPermission(@RequestParam("roleId") Long roleId,
                                       @RequestParam("permissionIds") List<Long> permissionIds,
                                       Model model){

        //1.根据角色id和权限id列表保存  给角色分配权限
        aclPermissionService.assignPermission(roleId,permissionIds);

        model.addAttribute("messagePage", "保存权限成功");
        return PAGE_SUCCESS;
    }

}
