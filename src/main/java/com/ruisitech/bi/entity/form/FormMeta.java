/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FormMeta extends BaseEntity {

    private String tableName;//

    private String tableNote;//表中文名

    private String tableDesc;//表备注信息

    private String tableCfg;  //json配置

    private String cataId; //分类

    private List<FormMetaCol> cols; //表字段

    private Integer version; //0或空表示未构建表

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buildDate;  //表构建时间

    public String getRealTableName(){
        String tname = this.getTableName() == null ? ("t_" + this.getId()): this.getTableName();
        return tname;
    }
}
