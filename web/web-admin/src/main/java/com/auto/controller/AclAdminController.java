package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.FileConstant;
import com.auto.entity.Admin;
import com.auto.entity.Role;
import com.auto.result.Result;
import com.auto.service.AclAdminRoleService;
import com.auto.service.AclAdminService;
import com.auto.service.AclRoleService;
import com.auto.service.UploadQinNiuFileService;
import com.auto.util.FileUtil;
import com.auto.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    private static final String ACTION_UPLOAD_AVATAR = "admin/upload";
    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";

    @Reference
    private AclAdminService aclAdminService;

    @Reference
    private UploadQinNiuFileService uploadQinNiuFileService;

    @Reference
    private AclRoleService aclRoleService;

    @Reference
    private AclAdminRoleService aclAdminRoleService;

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
    public String addAdmin(@Validated(Admin.class) Admin admin , Model model){
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
    public String updateAdmin(@Validated(Admin.class) Admin admin, Model model){
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

    /**
     * 上传头像页面
     *
     */
    @GetMapping("/uploadShow/{id}")
    public String toUploadAvatarPage(@PathVariable("id") Long id,Model model){
        model.addAttribute("id", id);
        return ACTION_UPLOAD_AVATAR;
    }

    /**
     * 上传头像
     */
    @PostMapping("/upload/{id}")
    public String uploadAvatar(@PathVariable("id") Long id,
                               @RequestParam("file") MultipartFile multipartFile,
                               Model model) throws IOException {

        //1.获取七牛云的头像路径
        String qinNiuDirPath = uploadQinNiuFileService.getQinNiuPath(multipartFile.getBytes(), multipartFile.getOriginalFilename());

        //2.加域名的七牛云储存路径
        String url = QiniuUtils.getUrl(qinNiuDirPath);

        //3.将土图片路径存到数据库
        Admin admin = aclAdminService.getById(id);
        admin.setHeadUrl(url);
        aclAdminService.update(admin);

        model.addAttribute("messagePage", "上传头像成功");

        return PAGE_SUCCESS;

    }

    /**
     * 给用户分配角色
     */
    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long id,Model model){
        //1.将adminId存到请求域中
        model.addAttribute("adminId", id);
        //2.将未分配的角色列表存到请求域中 unAssignRoleList
        //3.将已分配的角色列表存到请求域中 assignRoleList
        Map<String, List<Role>> roleMap = aclRoleService.findUnAssignRoleListAssignRoleListByAdminId(id);
        //存的是map中的所有键值对
        model.addAllAttributes(roleMap);
        return PAGE_ASSIGN_SHOW;
    }

    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("roleIds") List<Long> roleIds,
                             @RequestParam("adminId") Long adminId,
                             Model model) {
        //1. 调用业务层的方法给当前用户分配角色
        aclAdminService.assignRole(roleIds, adminId);
        //2. 显示成功页面
        model.addAttribute("messagePage", "分配角色成功");
        return PAGE_SUCCESS;
    }
}
