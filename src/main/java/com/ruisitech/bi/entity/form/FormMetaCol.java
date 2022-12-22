/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.form;

import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class FormMetaCol extends BaseEntity {

    private String colName;//

    private String colType;//

    private Integer colLength;//

    private String colNote;//

    private Integer colOrd;//

    private String tableId;//

    private String expression;//表达式

    private String inputType;//输入类型，文本/选择

    private String defvalue;//字段默认值

    private String colDesc;//填报时的提示信息

    private String options;//被选值

    private String valuestype;//值类型(固定值、动态值)

    private String matchTable;//值通过关联表获取

    private String matchCol;//对应的字段

    private String useCol;//用一个字段

    private String updateCol;//去更新目标表的字段

    private String tCondition;//条件

    private String matchColText;//

    private String incomeTname;//

    private Integer colScale;//

    private Integer required;//

    private Integer searchCol;//

    private String compType; //组件类型
}
