package com.auto.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Permission;
import com.auto.helper.PermissionHelper;
import com.auto.mapper.AclPermissionMapper;
import com.auto.service.AclPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限业务相关
 */
@Service(interfaceClass = AclPermissionService.class)
public class AclPermissionServiceImpl extends BaseServiceImpl<Permission> implements AclPermissionService {

    @Autowired
    private AclPermissionMapper aclPermissionMapper;

    @Override
    public BaseMapper<Permission> getBaseMapper() {
        return aclPermissionMapper;
    }

    /**
     * 根据用户Id查询权限列表
     *
     * @param adminId
     * @return
     */
    @Override
    public List<Permission> findPermissionListByAdminId(Long adminId) {

        List<Permission> permissionList;
        //判断当前用户是否为超级管理员
        if (adminId == 1L) {
            //如果是，则查所有
            permissionList = aclPermissionMapper.findAll();
        } else {
            //如果不是，则根据adminId查询
            permissionList = aclPermissionMapper.findPermissionListByAdminId(adminId);
        }

        //判断当前权限列表是否为空
        if (CollectionUtils.isNotEmpty(permissionList)) {

            //判断当前权限是否超级管理员 ， 也就是父id为0
            return permissionList.stream()
                    .filter(permission -> permission.getParentId() == 0)
                    .map(permission -> {
                        //因为数据库表中没有level和子菜单children
                        //所以此时通过映射一个新集合的方式
                        //设置层级
                        permission.setLevel(1);
                        //因为一级菜单没有父菜单，所以不设置parentName
//                    permission.setParentName();
                        //设置下级列表
                        //下级菜单可能有多个，所以这时我们递归获取下级列表的菜单集合的方法
                        permission.setChildren(getChildren(permission, permissionList));
                        return permission;
                    }).collect(Collectors.toList());

//            return PermissionHelper.build(permissionList);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 递归查询所有子菜单
     *
     * @param permission
     * @param permissionList
     * @return
     */
    private List<Permission> getChildren(Permission permission, List<Permission> permissionList) {
        //注意这里的permission的parentId是现在菜单的id
        //p是下一级Id
        return permissionList.stream().filter(p -> p.getParentId().equals(permission.getId()))
                .map(p -> {
                    //设置层级
                    p.setLevel(permission.getLevel() + 1);
                    //设置父id名称
                    p.setParentName(permission.getName());
                    //设置下级列表
                    p.setChildren(getChildren(p, permissionList));
                    return p;
                }).collect(Collectors.toList());
    }


}