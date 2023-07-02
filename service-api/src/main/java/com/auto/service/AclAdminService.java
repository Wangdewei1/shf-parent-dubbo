package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.Admin;

import java.util.List;

public interface AclAdminService extends BaseService<Admin> {
    List<Admin> findNotAdminListByHouseId(Long houseId);
}
