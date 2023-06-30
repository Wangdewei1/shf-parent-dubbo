package com.auto.mapper;

import com.auto.entity.Dict;

import java.util.List;

public interface DictMapper {
    //根据parentId获取数据字典集合
    List<Dict> findListByParentId(Long parentId);
    //判断是否是父节点
    Integer countIsParent(Long id);

    List<Dict> findDictListByDicCode(String parentCode);
}
