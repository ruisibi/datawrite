/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruisitech.bi.util.RSBIUtils;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {

	/**
	 * 以下为公共字段
	 */
	private String id;  //所有表的主键

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;  //创建时间

	private String createUser; //创建人

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updateDate; //更新时间

	@JsonIgnore
	private String dbName =  RSBIUtils.getConstant("dbName");

	/**
	 * 获取指定数据库的取时间函数
	 * @return
	 */
	@JsonIgnore
	public String getDateString(){
		return "now()";
	}

	public void validate(){

	}

}
