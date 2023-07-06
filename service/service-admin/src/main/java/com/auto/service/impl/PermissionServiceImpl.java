package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.Permission;
import com.auto.helper.PermissionHelper;
import com.auto.mapper.AclPermissionMapper;
import com.auto.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 菜单管理
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private AclPermissionMapper aclPermissionMapper;

    @Override
    public BaseMapper<Permission> getBaseMapper() {
        return aclPermissionMapper;
    }

    /**
     * 重写findAl方法
     * @return
     */
    @Override
    public List<Permission> findAll() {
        List<Permission> permissionList = aclPermissionMapper.findAll();
        return PermissionHelper.build(permissionList);
    }

    /**
     * 重写insert方法
     */
    @Override
    public void insert(Permission permission) {
        //根据权限父id 和 名称查询
        Permission dbPermission = aclPermissionMapper.findPermissionByParentIdAndName(permission.getParentId(),permission.getName());
        if (dbPermission != null){
            //则证明有子菜单
            throw new RuntimeException("menu is exited");
        }
        super.insert(permission);
    }

    /**
     * 重写delete方法
     */
    @Override
    public void delete(Long id) {
        //判断当前要删除的这个菜单它里边是否有子菜单:以当前要删除的菜单id作为parentId查询子菜单数量
        Integer count = aclPermissionMapper.findCountByParentId(id);
        if (count > 0) {
            //说明当前要删除的菜单还有子菜单，则不能删除
            throw new RuntimeException("menu has children,can not delete!!!");
        }
        //如果要删除的这个菜单没有子菜单，则可以删除
        super.delete(id);
    }

    /**
     * 重写update
     */
    @Override
    public void update(Permission permission) {
        //根据id查询修改前的permission的信息
        Permission originPermission = aclPermissionMapper.getById(permission.getId());
        //根据要修改的这个权限的parentId和要修改成为的name进行查找
        Permission dbPermission = aclPermissionMapper.findPermissionByParentIdAndName(originPermission.getParentId(), permission.getName());
        if (dbPermission != null && !dbPermission.getId().equals(originPermission.getId())) {
            //说明要修改成为的菜单是已存在的，不能修改
            throw new RuntimeException("menu already exist!!!");
        }
        //如果要修改成为的菜单不存在，才能修改
        super.update(permission);
    }
}
