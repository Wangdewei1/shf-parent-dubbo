package com.auto.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Permission;
import com.auto.entity.RolePermission;
import com.auto.entity.pojo.ResultZNodesInfo;
import com.auto.helper.PermissionHelper;
import com.auto.mapper.AclPermissionMapper;
import com.auto.mapper.AclRolePermissionMapper;
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

    @Autowired
    private AclRolePermissionMapper aclRolePermissionMapper;

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
                    //permission.getParentId() == 0 用于定义过滤的条件。该条件检查每个元素的parentId属性是否等于0，只有符合条件的元素才会被保留下来。
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
     * 根据角色id查询权限列表
     *
     * @param roleId
     * @return
     */
    @Override
    public List<ResultZNodesInfo> findPermissionListByRoleId(Long roleId) {
        //根据角色id查询所有该角色的权限列表
//        List<Permission> permissionList = aclRolePermissionMapper.findPermissionListByRoleId(roleId);
        //查询所有权限
        List<Permission> permissionList = aclPermissionMapper.findAll();
        //根据角色id查询权限当前所有权限id
        List<Long> permissionIds = aclRolePermissionMapper.findPermissionIds(roleId);
        //判断权限列表是否不为空
        if (CollectionUtils.isNotEmpty(permissionList)) {
            return permissionList.stream()
                    .map(permission -> {
                        //封装返回给前端的数据
                        ResultZNodesInfo zNodesInfo = new ResultZNodesInfo();
                        //设置当前节点的权限id
                        zNodesInfo.setId(permission.getId());
                        //设置当前节点的PId  父id
                        zNodesInfo.setPId(permission.getParentId());
                        //设置当前节点的名字
                        zNodesInfo.setName(permission.getName());
                        //判断当前节点是否有子节点
                        if (aclPermissionMapper.permissionCount(permission.getId()) > 0) {
                            //有子节点,则可以打开菜单
                            zNodesInfo.setOpen(true);
                        } else {
                            zNodesInfo.setOpen(false);
                        }
                        //判断当前节点是否被选中，也就是是否被打对号
                        if (CollectionUtils.isNotEmpty(permissionIds) && permissionIds.contains(permission.getId())) {
                            //如果当前角色有权限，并且当角色的权限包含当前节点，则对号 也就是选中状态
                            zNodesInfo.setChecked(true);
                        } else {
                            zNodesInfo.setChecked(false);
                        }
                        return zNodesInfo;
                    }).collect(Collectors.toList());
        }
        //否则返回空集合
        return Collections.EMPTY_LIST;
    }

    /**
     * 保存给角色 分配权限
     *
     * @param roleId        角色id
     * @param permissionIds 是所有的角色的权限id的集合列表
     */
/*    @Override
    public void saveAssignPermissionByRoleIdAndPermissionIds(Long roleId, List<Long> permissionIds) {
        //1.查询当前用户的所有Id权限 列表  根据用户id
        List<Long> permissionIdList = aclRolePermissionMapper.findPermissionIds(roleId);
        //2.查询要删除的角色权限所有id
        if (permissionIdList == null) {
            permissionIds.stream()
                    .map(permissionId -> {
                        //3.根据角色id和要移除的权限id集合列表查询  角色权限列表
                        RolePermission rolePermission = aclRolePermissionMapper.findPermissionListByRoleIdAndPermissionIds(roleId, permissionIds);
                        //4.判断要删除的权限 和 角色是否关联
                        if (rolePermission == null) {
                            rolePermission = new RolePermission();
                            rolePermission.setRoleId(roleId);
                            rolePermission.setPermissionId(permissionId);
                            aclRolePermissionMapper.insert(rolePermission);
                            return rolePermission;
                        } else {
                            if (rolePermission.getPermissionId() == 1) {
                                rolePermission.setIsDeleted(0);
                                aclRolePermissionMapper.update(rolePermission);
                                return rolePermission;
                            }
                            return Collections.EMPTY_LIST;
                        }
                    }).collect(Collectors.toList());
        } else {
            List<Long> removePermissionIds = permissionIds.stream()
                    .filter(permissionId -> !permissionIdList.contains(permissionId))
                    .collect(Collectors.toList());

            permissionIds.stream()
                    .map(permissionId -> {
                        //3.根据角色id和要移除的权限id集合列表查询  角色权限列表
                        RolePermission rolePermission = aclRolePermissionMapper.findPermissionListByRoleIdAndPermissionIds(roleId, removePermissionIds);
                        //4.判断要删除的权限 和 角色是否关联
                        if (rolePermission == null) {
                            rolePermission = new RolePermission();
                            rolePermission.setRoleId(roleId);
                            rolePermission.setPermissionId(permissionId);
                            aclRolePermissionMapper.insert(rolePermission);
                            return rolePermission;
                        } else {
                            if (rolePermission.getPermissionId() == 1) {
                                rolePermission.setIsDeleted(0);
                                aclRolePermissionMapper.update(rolePermission);
                                return rolePermission;
                            }
                            return Collections.EMPTY_LIST;
                        }
                    }).collect(Collectors.toList());
        }

    }*/
    @Override
    public void assignPermission(Long roleId, List<Long> permissionIds) {
        //1. 删除roleId对应的所有分配权限记录
        aclRolePermissionMapper.deleteByRoleId(roleId);
        //2. 给roleId新增permissionIds中的所有permissionId对应的分配权限记录
        if (CollectionUtils.isNotEmpty(permissionIds)) {
            aclRolePermissionMapper.insertBatch(roleId,permissionIds);
        }
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
        //p是集合里面的每条permission，让集合中的parentId等于permission中的id，符合条件的保留下来
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