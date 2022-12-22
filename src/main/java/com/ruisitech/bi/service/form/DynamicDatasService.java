/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.form;

import com.rsbi.ext.engine.dao.DaoHelper;
import com.ruisitech.bi.entity.form.FormMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DynamicDatasService
 * @Description DynamicDatasService
 * @Author huangqin
 * @Date 2022/12/20 10:23 上午
 */
@Service
public class DynamicDatasService {

    @Autowired
    private FormMetaService formMetaService;

    @Resource(name = "daoHelper")
    private DaoHelper daoHelper;

    public List<Map<String, Object>> queryDatas(Map<String, Object> comp){
        String matchTable = (String) comp.get("matchTable");
        FormMeta formMeta = formMetaService.selectByPrimaryKey(matchTable);
        String sql = this.createSql(comp, formMeta.getTableName());
        return daoHelper.queryForList(sql);
    }

    private String createSql(Map<String, Object> comp, String tname){
        String matchCol = (String) comp.get("matchTableCol");
        StringBuffer sb = new StringBuffer();
        sb.append("select "+matchCol+" as \"col\" from");
        sb.append(" "+tname+" ");
        return sb.toString();
    }
}
