package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AclAdminMapper extends BaseMapper<Admin> {
    List<Admin> findNotAdminListByHouseId(Long houseId);
}
