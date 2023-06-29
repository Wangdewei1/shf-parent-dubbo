package com.auto.base;


import com.auto.constant.PageConstant;
import com.auto.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 业务层 实现类基类
 * @param <T>
 */
@Service
public  abstract class BaseServiceImpl<T>{
    public abstract BaseMapper<T> getBaseMapper();

    /**
     * 查询所有
     * @return
     */

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public List findAll() {
        return getBaseMapper().findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public T getById(Long id) {
        return getBaseMapper().getById(id);
    }

    /**
     * 新增
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insert(T t) {
        getBaseMapper().insert(t);
    }

    /**
     * 删除
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        getBaseMapper().delete(id);
    }

    /**
     * 修改
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(T t) {
        getBaseMapper().update(t);
    }

    /**
     * 根据条件分页
     * @param filters
     * @return
     */
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public PageInfo findPage(Map<String,String> filters) {
        //1.开启分页 根据分页插件
        PageHelper.startPage(CastUtil.castInt(filters.get("pageNum"), PageConstant.DEFAULT_PAGE_NUM),CastUtil.castInt(filters.get("pageSize"),PageConstant.DEFAULT_PAGE_SIZE));
        //2.根据条件查询数据
        List<T> list = getBaseMapper().findPage(filters);
        return new PageInfo(list,PageConstant.NAVIGATE_PAGES);
    }
}
