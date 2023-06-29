package com.auto.base;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 封装业务层基本的方法 基类  BaseService
 */
public interface BaseService<T> {
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
    PageInfo<T> findPage(Map<String,String> filters);
}
