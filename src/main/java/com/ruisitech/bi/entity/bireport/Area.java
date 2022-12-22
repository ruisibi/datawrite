/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.bireport;

import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

/**
 * 地图地域对照表
 */
@Data
public class Area extends BaseEntity {

	private String provCode;
	private String provName;
	private String cityCode;
	private String cityName;
	private String townCode;
	private String townName;

	@Override
	public void validate() {

	}
}
