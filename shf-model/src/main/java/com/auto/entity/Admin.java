package com.auto.entity;

import com.auto.gruop.ValidatedGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
@Data
public class Admin extends BaseEntity {
	private static final long serialVersionUID = 1L;
	//用户名
	@NotBlank(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} , message = "用户名不能为空")
	@Size(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} ,max = 10 , min = 4 ,message = "用户名长度4 - 10位")
	private String username;
	//密码
	@NotBlank(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class},message = "密码不能为空")
	@Size(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} ,min = 8,max = 16,message = "密码长度必须在 8 - 16 位")
	private String password;
	//姓名
	@NotBlank(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} , message = "姓名不能为空")
	@Size(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} ,max = 8 , min = 4 ,message = "用户名长度4 - 8位")
	private String name;
	//手机
	@NotBlank(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} , message = "手机号不能为空")
	@Pattern(groups = {ValidatedGroup.UpdateGroup.class,ValidatedGroup.InsertGroup.class} , regexp = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$",message = "手机号不符合规范")
	private String phone;
	//头像地址   
	private String headUrl;
	//描述   
	private String description;

	private List<Role> roleList;
}

