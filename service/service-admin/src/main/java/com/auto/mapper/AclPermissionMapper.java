package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限业务相关
 */
public interface AclPermissionMapper extends BaseMapper<Permission> {
    List<Permission> findPermissionListByAdminId(Long adminId);

    Integer permissionCount(Long id);

    Permission findPermissionByParentIdAndName(@Param("parentId") Long parentId, @Param("name") String name);

    Integer findCountByParentId(Long id);
}
