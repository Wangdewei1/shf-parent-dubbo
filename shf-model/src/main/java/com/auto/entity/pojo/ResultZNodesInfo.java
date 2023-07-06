package com.auto.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 给角色分配权限的
 * 返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultZNodesInfo implements Serializable {
    public static final Long serialVersionUID = 1L;

    private Long id;
    private Long pId;
    private String name;
    private Boolean open;
    private Boolean checked;
}
