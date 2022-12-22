/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.frame;

import com.ruisitech.bi.entity.common.BaseEntity;
import com.ruisitech.bi.util.RSBIUtils;
import lombok.Data;

/**
 * 组织机构，用来控制数据权限
 * @author hq
 *
 */
@Data
public class Department extends BaseEntity {

    private String text;  //用在 eaayui 的 tree

    private String deptCode;//

    private String deptName;//

    private String deptNote;//

    private String pid;//

    private Boolean children;  //tree 的状态

    private String icon; //图标

	@Override
	public void validate() {
		 this.deptCode = RSBIUtils.htmlEscape(this.deptCode);
		 this.deptName = RSBIUtils.htmlEscape(this.deptName);
		 this.deptNote = RSBIUtils.htmlEscape(this.deptNote);
	}
}
