package com.auto.base;

import java.util.List;
import java.util.Map;

/**
 * 对mapper基本方法的封装
 * @param <T> 基类
 */
public interface BaseMapper <T> {

    /**
     * 查询所有
     */
    List<T> findAll();

    /**
     * 根据id查询
     */
    T getById(Long id);

    /**
     * 新增
     */
    void insert(T t);

    /**
     * 根据id删除
     */
    void delete(Long id);

    /**
     * 修改
     */
    void update(T t);

    /**
     * 根据搜索条件 查询
     */
    List<T> findPage(Map<String,String> filters);


}
