package com.auto.entity;

import com.auto.gruop.ValidatedGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * 包名:com.atguigu.entity
 *
 * @author Leevi
 * 日期2022-03-22  21:41
 */
@Data
public class BaseEntity implements Serializable {
    @Null(groups = ValidatedGroup.InsertGroup.class , message = "添加操作id必须为空")
    @NotNull(groups = ValidatedGroup.UpdateGroup.class , message = "修改操作id必须不为空")
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Integer isDeleted;
}
