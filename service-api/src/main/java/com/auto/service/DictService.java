package com.auto.service;

import com.auto.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {

    List<Map<String, Object>> findListByParentId(Long id);

    Integer countIsParent(Long id);

    List<Dict> findDictListByDicCode(String parentCode);

    List<Dict> findDictListByParentId(Long parentId);
}
