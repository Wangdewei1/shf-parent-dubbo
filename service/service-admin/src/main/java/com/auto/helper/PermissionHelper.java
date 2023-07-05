package com.auto.helper;


import com.auto.entity.Permission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * stream
 */
public class PermissionHelper {
    /**
     * 构建父子菜单的关系
     * @param permissionList
     * @return
     */
    public static List<Permission> build(List<Permission> permissionList) {
        return permissionList.stream()
                .filter(permission -> permission.getParentId() == 0)
                .map(permission -> {
                    //4.2 设置permission的level属性为1
                    permission.setLevel(1);
                    //4.3 不设置permission的parentName属性,因为一级菜单没有父菜单
                    //4.4 设置permission的children属性为所有的二级菜单
                    permission.setChildren(getChildren(permission, permissionList));
                    return permission;
                }).collect(Collectors.toList());
    }

    /**
     * 递归查询permission的所有子菜单
     * @param permission
     * @param permissionList
     * @return
     */
    private static List<Permission> getChildren(Permission permission, List<Permission> permissionList) {
        //1. 从permissionList中过滤出permission的所有的子菜单
        return permissionList.stream()
                .filter(p -> p.getParentId().equals(permission.getId()))
                .map(p -> {
                    //2. 设置子菜单level
                    p.setLevel(permission.getLevel()+1);
                    //3. 设置子菜单的parentName
                    p.setParentName(permission.getName());
                    //4. 设置子菜单的children
                    p.setChildren(getChildren(p,permissionList));
                    return p;
                }).collect(Collectors.toList());
    }
}