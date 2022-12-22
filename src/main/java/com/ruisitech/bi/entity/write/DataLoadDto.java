/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.write;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName DataLoadDto
 * @Description DataLoadDto
 * @Author huangqin
 * @Date 2021/11/20 3:13 下午
 */
@Data
public class DataLoadDto {

    private Integer page;
    private Integer rows;
    private Integer total;
    private String tableId;
    private String tableName;
    private Map<String, Object> pms; //参数
}
