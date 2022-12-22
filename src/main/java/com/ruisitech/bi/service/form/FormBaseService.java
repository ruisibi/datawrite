/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.form;

/**
 * 表单基础类
 * @Author huangqin
 * @Date 2022/10/25 4:45 下午
 */
public class FormBaseService {

    /**
     * 填报表默认字段
     */
    public static final String tbbId = "tmp_data_id";
    public static final String tbbUser = "tmp_user_id";
    //增加审核状态的字段, 0, 未审核， 2.审核通过，3.审核不通过
    public static final String auditState = "tmp_audit_state";
    public static final String createDate = "tmp_create_date";
    public static final String updateDate = "tmp_update_date";
}
