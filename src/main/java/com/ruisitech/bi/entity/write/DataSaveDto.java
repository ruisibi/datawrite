/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.write;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DataSaveDto
 * @Description DataSaveDto
 * @Author huangqin
 * @Date 2022/10/31 10:51 上午
 */
@Data
public class DataSaveDto {

    private String id; //数据行ID
    private String tableId;
    private Map<String, Object> values;
    private List<String> ids; //用在删除时
}
