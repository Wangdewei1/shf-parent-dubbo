package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.anno.RedisCatch;
import com.auto.entity.Dict;
import com.auto.mapper.DictMapper;
import com.auto.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据字典   封装固定的数据
 */
@Service(interfaceClass = DictService.class)
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    /**
     * 查询数据字典
     * @param id
     * @return
     */
    @RedisCatch
    @Override
    public List<Map<String, Object>> findListByParentId(Long id) {

        //1.调用持久层方法，根据父节点id查询List<Dict>字典
        List<Dict> dictList = dictMapper.findListByParentId(id);

        //2.使用stream流，将dictList返回新的集合
        return dictList.stream()
                //dict是dictList集合中的每一个元素
                .map(dict -> {
                    Map<String,Object> zNodes = new HashMap<>();
                    //向znode中存放id
                    zNodes.put("id", dict.getId());
                    //向znode中存放name
                    zNodes.put("name", dict.getName());
                    //向znode中存放isParent
                    zNodes.put("isParent", dictMapper.countIsParent(id) > 0);
                    return zNodes;
                }).collect(Collectors.toList());
    }

    /**
     * 判断是否有父节点
     * @param id
     * @return
     */
    @RedisCatch
    @Override
    public Integer countIsParent(Long id) {
        return dictMapper.countIsParent(id);
    }

    /**
     * 根据字典编码获取 字典列表信息
     * @param parentCode
     * @return
     */
    @RedisCatch
    @Override
    public List<Dict> findDictListByDicCode(String parentCode) {
        return dictMapper.findDictListByDicCode(parentCode);
    }

    /**
     * 根据父节点的id获取子节点的列表
     * @param parentId
     * @return
     */
    @RedisCatch
    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        return dictMapper.findListByParentId(parentId);
    }
}
