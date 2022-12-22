/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.mapper.form;

import com.ruisitech.bi.entity.form.FormType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FormTypeMapper {

	List<FormType> listcataTree();

	void insertType(FormType type);

	void updateType(FormType type);

	void deleleType(@Param("id") String id);

	FormType getType(@Param("id") String id);

	Integer cntReport(@Param("id") String id);

}
