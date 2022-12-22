/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.common;

import lombok.Data;

@Data
public class DSColumn {
	private Integer idx;
	private String name;
	private String type;
	private String dispName;
	private Integer length; //字段长度
	private Integer scale; //小数位长度
	private String tname;
	private Boolean isshow = true; //是否显示字段
	private String expression;
	private String defvalue; //默认值
}
