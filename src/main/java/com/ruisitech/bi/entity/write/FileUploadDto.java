/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.write;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName FileUploadDto
 * @Description FileUploadDto
 * @Author huangqin
 * @Date 2022/11/21 6:06 下午
 */
@Data
public class FileUploadDto implements Serializable {

    public FileUploadDto(){

    }

    public FileUploadDto(Long name, String extName, String fileName){
        this.name = name;
        this.extName = extName;
        this.fileName = fileName;
    }

    private Long name;  //名称 uid对象
    private String extName;  //扩展名
    private String fileName;  //上传的文件名
}
