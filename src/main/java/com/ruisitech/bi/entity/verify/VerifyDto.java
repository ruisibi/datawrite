/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.verify;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName VerifyDto
 * @Description VerifyDto
 * @Author huangqin
 * @Date 2022/11/22 5:51 下午
 */
@Data
public class VerifyDto {

    private String tableId;
    private Integer audit;  //审核状态
    private List<String> ids; //数据的ID列表
}
