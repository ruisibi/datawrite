/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.form;

import com.alibaba.fastjson.JSONObject;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.mapper.form.FormMetaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @ClassName FormMetaService
 * @Description FormMetaService
 * @Author huangqin
 * @Date 2022/9/20 2:06 下午
 */
@Service
public class FormMetaService extends FormBaseService {

    @Resource
    private FormMetaMapper mapper;

    public int deleteByPrimaryKey(String id){
        return mapper.deleteByPrimaryKey(id);
    }

    public int insert(FormMeta record){
        return mapper.insert(record);
    }

    public int insertSelective(FormMeta record){
        return mapper.insertSelective(record);
    }

    public FormMeta selectByPrimaryKey(String id){
        return mapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(FormMeta record){
        return mapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(FormMeta record){
        return mapper.updateByPrimaryKey(record);
    }

    public List<FormMeta> list(String cataId){
        return mapper.list(cataId);
    }

    public List<FormMeta> listWirteForms(String typeId){
        return mapper.listWirteForms(typeId);
    }
}
