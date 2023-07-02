package com.auto.en;

/**
 * 房源图片 类型
 * 1.房源图片
 * 2.房产图片
 */
public enum HouseImageType {

    HOUSE_IMAGE_TYPE("房源图片",1),
    HOUSE_IMAGE_TYPE_PROPERTY("房产图片",2);

    private Integer type;

    private String message;

    HouseImageType(String message,Integer code){

    }

    public Integer getType() {
        return type;
    }
}
