/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.form;

import com.ruisitech.bi.entity.common.BaseEntity;
import com.ruisitech.bi.util.RSBIUtils;
import lombok.Data;

@Data
public class FormType extends BaseEntity {

	private String name;
	private String text;
	private String note;
	private Integer ord;
	private String icon;

	@Override
	public void validate() {
		this.name = RSBIUtils.htmlEscape(this.name);
		this.text = RSBIUtils.htmlEscape(this.text);
		this.note = RSBIUtils.htmlEscape(this.note);
	}
}
