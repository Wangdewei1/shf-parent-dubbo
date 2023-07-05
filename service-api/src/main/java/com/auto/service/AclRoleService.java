package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.Role;

import java.util.List;

/**
 * 后台用户管理 业务
 */
public interface AclRoleService extends BaseService<Role> {
    List<Role> findRoleListByAdminId(Long adminId);
}
