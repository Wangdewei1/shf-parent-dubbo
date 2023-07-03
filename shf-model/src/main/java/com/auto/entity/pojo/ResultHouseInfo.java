package com.auto.entity.pojo;

import com.auto.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装下去详情
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResultHouseInfo implements Serializable {
    public static final Long serialVersionUID = 1L;

    private House house;

    private Community community;

    private List<HouseBroker> houseBrokerList;
    //房产图片列表
    private List<HouseImage> houseImage1List;

    private List<HouseUser> houseUserList;
}
