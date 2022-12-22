/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.form;

import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.mapper.form.FormMetaColMapper;
import com.ruisitech.bi.mapper.form.FormMetaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FormMetaColService
 * @Description FormMetaColService
 * @Author huangqin
 * @Date 2022/10/26 10:34 上午
 */
@Service
public class FormMetaColService {

    @Resource
    private FormMetaColMapper mapper;

    public int deleteByTableId(String tableId){
        return mapper.deleteByTableId(tableId);
    }

    public int deleteByPrimaryKey(String id){
        return mapper.deleteByPrimaryKey(id);
    }

    public int insert(FormMetaCol record){
        return mapper.insert(record);
    }

    public int insertSelective(FormMetaCol record){
        return mapper.insertSelective(record);
    }

    public FormMetaCol selectByPrimaryKey(String id){
        return  mapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(FormMetaCol record){
        return mapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(FormMetaCol record){
        return mapper.updateByPrimaryKey(record);
    }

    public List<FormMetaCol> selectByTableId(String id) {
        return mapper.selectByTableId(id);
    }
}
