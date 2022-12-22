/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.frame;

import com.ruisitech.bi.entity.common.BaseEntity;
import com.ruisitech.bi.util.RSBIUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Menu extends BaseEntity {

	private String menuPid;
	private String menuName;
	private String menuDesc;
	private Date menuDate;
	private Integer menuOrder;
	private String menuUrl;
	private String urls;
	private String mvs;
	private String avatar;

	private List<Menu> children;

	@Override
	public void validate() {
		this.menuName = RSBIUtils.htmlEscape(this.menuName);
		this.menuDesc = RSBIUtils.htmlEscape(this.menuDesc);
	}
}
