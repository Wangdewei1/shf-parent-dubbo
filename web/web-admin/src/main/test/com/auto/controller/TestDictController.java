package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.service.DictService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@ContextConfiguration(locations = "classpath:spring/spring-*.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDictController {
    @Reference
    private DictService dictService;

    @Test
    public void getDictDataTest(){

        List<Map<String, Object>> list = dictService.findListByParentId(0l);
        list.stream().forEach(System.out::println);

    }
}
