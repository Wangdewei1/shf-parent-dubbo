package com.auto.service;

import java.util.List;
import java.util.Map;

public interface DictService {

    List<Map<String, Object>> findListByParentId(Long id);

    Integer countIsParent(Long id);
}
