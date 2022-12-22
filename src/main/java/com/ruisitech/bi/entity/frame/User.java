/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.frame;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruisitech.bi.entity.common.BaseEntity;
import com.ruisitech.bi.util.RSBIUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public final class User extends BaseEntity implements Serializable {

	private String userId; //用户ID，和 id 字段存的内容相同

	/**
	 *
	 */
	private static final long serialVersionUID = 6096757156465671644L;

	private String staffId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date loginDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date regDate;
	private Integer logCnt;
	private String loginName;
	@JsonIgnore
	private String password;
	private String gender;
	private String mobilePhone;
	private String email;
	private String officeTel;
	private Integer state; //1 为启用， 0为停用。
	private String channel; //app 用户的设备ID
	private Integer deptId;
	private String deptCode;  //部门用来数据过滤的值
	private String deptName;
	@JsonIgnore
	private Date errDate; //输入密码错误时间
	private String wxid; //用户对应的微信Id
	private String logIp; //ip地址

	@JsonIgnore
	private List<String> urls; //能访问的URL

	public User() {

	}

	@Override
	public String toString() {
		return "id = " + this.getId() + ", name = " + this.loginName;
	}

	public void validate(){
		this.loginName = RSBIUtils.htmlEscape(this.loginName);
		this.staffId = RSBIUtils.htmlEscape(this.staffId);
	}
}
