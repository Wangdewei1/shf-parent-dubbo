package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.service.AclPermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 权限 管理模块
 */
@Controller
public class AclPermissionController {

    @Reference
    private AclPermissionService aclPermissionService;



}
