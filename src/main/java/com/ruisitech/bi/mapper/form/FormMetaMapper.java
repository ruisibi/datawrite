/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.mapper.form;

import com.ruisitech.bi.entity.form.FormMeta;

import java.util.List;

public interface FormMetaMapper {
    int deleteByPrimaryKey(String id);

    int insert(FormMeta record);

    int insertSelective(FormMeta record);

    FormMeta selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FormMeta record);

    int updateByPrimaryKey(FormMeta record);

    List<FormMeta> list(String cataId);

    /**
     * 查询可以被填报的表单
     * @return
     */
    List<FormMeta> listWirteForms(String typeId);
}
