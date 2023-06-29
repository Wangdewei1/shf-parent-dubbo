package com.auto.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Role extends BaseEntity {
	private static final long serialVersionUID = 1L;
	//角色名称
	@NotBlank(message = "角色名不能为空")
	@Size(min = 4,max = 8 ,message = "角色名应在4-8个字符")
	private String roleName;
	//角色编码
	@NotBlank(message = "角色代码不能为空")
	@Size(min = 4,max = 6 ,message = "角色代码应在4-6个字符")
	private String roleCode;
	//描述   
	private String description;
}

