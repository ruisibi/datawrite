/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.frame;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class Role extends BaseEntity {

	private String roleName;
	private String roleDesc;
	private Integer ord;
	private String userId; //角色所属用户ID

}
