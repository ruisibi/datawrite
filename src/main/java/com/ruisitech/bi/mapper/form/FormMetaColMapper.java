/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.mapper.form;

import com.ruisitech.bi.entity.form.FormMetaCol;

import java.util.List;

public interface FormMetaColMapper {

    int deleteByPrimaryKey(String id);

    int deleteByTableId(String tableId);

    int insert(FormMetaCol record);

    int insertSelective(FormMetaCol record);

    FormMetaCol selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FormMetaCol record);

    int updateByPrimaryKey(FormMetaCol record);

    List<FormMetaCol> selectByTableId(String id);
}
