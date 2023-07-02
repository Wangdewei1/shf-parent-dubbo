package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.FileConstant;
import com.auto.entity.Admin;
import com.auto.service.AclAdminService;
import com.auto.util.FileUtil;
import com.auto.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        //1.上传文件的唯一名称
        String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());

        //2.设置上传到七牛云中的路径
        String randomDirPath = "shf/" + FileUtil.getRandomDirPath(FileConstant.DEFAULT_DIR_LEVEL, FileConstant.DEFAULT_DIR_SIZE);

        //3.转变成七牛云的储存路径
        String qinNiuDirPath = randomDirPath + uuidName;

        //4.加域名的七牛云储存路径
        String url = QiniuUtils.getUrl(qinNiuDirPath);

        //5.将图片上传到七牛云
        QiniuUtils.upload2Qiniu(multipartFile.getBytes(), qinNiuDirPath);

        //6.将土图片路径存到数据库
        Admin admin = aclAdminService.getById(id);
        admin.setHeadUrl(url);
        aclAdminService.update(admin);

        model.addAttribute("messagePage", "上传头像成功");

        return PAGE_SUCCESS;


    }
}
