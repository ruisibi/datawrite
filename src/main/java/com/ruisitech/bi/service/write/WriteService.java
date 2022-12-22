/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.write;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.dao.DaoHelper;
import com.rsbi.ext.engine.util.DaoUtils;
import com.rsbi.ext.engine.view.context.grid.PageInfo;
import com.rsbi.ext.engine.view.exception.ExtConfigException;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.entity.write.DataLoadDto;
import com.ruisitech.bi.mapper.form.FormMetaMapper;
import com.ruisitech.bi.service.form.FormBaseService;
import com.ruisitech.bi.service.form.FormMetaColService;
import com.ruisitech.bi.service.form.FormMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WriteService
 * @Description WriteService
 * @Author huangqin
 * @Date 2022/10/23 11:30 上午
 */
@Service
public class WriteService extends FormBaseService  {

    @Resource
    private FormMetaMapper mapper;

    @Autowired
    private FormMetaService formMetaService;

    @Autowired
    private FormMetaColService formMetaColService;

    @Resource(name = "daoHelper")
    private DaoHelper daoHelper;

    public List<FormMeta> list(String typeId){
        return mapper.listWirteForms(typeId);
    }

    public FormMeta getTableInfo(String tableId){
        FormMeta formMeta = formMetaService.selectByPrimaryKey(tableId);
        List<FormMetaCol> cols = formMetaColService.selectByTableId(tableId);
        //添加审核字段
        FormMetaCol col = new FormMetaCol();
        col.setId(auditState);
        col.setColName(auditState);
        col.setColNote(auditState);
        col.setColDesc("状态");
        col.setCompType("String");
        col.setCompType("base"); //字段类型为系统自建
        cols.add(col);
        formMeta.setCols(cols);
        return formMeta;
    }

    public List<Map<String, Object>> queryTableDatas(DataLoadDto dto) throws ExtConfigException {
        List<FormMetaCol> cols = formMetaColService.selectByTableId(dto.getTableId());
        String sql = this.createSql(cols, dto.getTableName(), dto,null);
        //分页
        PageInfo page = new PageInfo();
        if(dto.getPage() != null){
            page.setCurtpage(dto.getPage() - 1);
        }
        if(dto.getRows() != null){
            page.setPagesize(dto.getRows());
        }
        List<Map<String, Object>> ls = DaoUtils.calPage(sql, page, daoHelper);
        dto.setTotal((int)page.getAllsize());
        return ls;
    }

    public Map<String, Object> getDataById(String tableId, String dataId){
        FormMeta formMeta = formMetaService.selectByPrimaryKey(tableId);
        List<FormMetaCol> cols = formMetaColService.selectByTableId(tableId);
        String sql = this.createSql(cols, formMeta.getTableName(), null, dataId);
        List<Map<String, Object>> dts = daoHelper.queryForList(sql, new Object[]{dataId});
        return dts.get(0);
    }

    private String createSql(List<FormMetaCol> cols, String tname, DataLoadDto dto, String id){
        //拼接sql
        StringBuffer sql = new StringBuffer("select ");
        for(int i=0; i<cols.size(); i++){
            FormMetaCol m = cols.get(i);
            String name = m.getColName();
            sql.append(name);
            sql.append(" as ");
            sql.append(" \""+m.getId()+"\" ");
            sql.append(",");
        }
        sql.append(tbbId);
        sql.append(",");
        sql.append(createDate);
        sql.append(",");
        sql.append(auditState);
        sql.append(" from ");
        sql.append(tname);
        sql.append(" where 1=1 ");
        if(id != null && id.length() > 0){
            sql.append(" and ");
            sql.append(tbbId +" = ? ");
        }
        if(dto != null){
            Map<String, Object> pms = dto.getPms();
            for(Map.Entry<String, Object> pm : pms.entrySet()){
                String key = pm.getKey();
                Object vls = pm.getValue();
                if(vls == null){
                    continue;
                }
                sql.append(" and ");
                sql.append(key);
                if(vls instanceof String){
                   sql.append(" = ");
                   sql.append("'");
                   sql.append(vls);
                   sql.append("'");
                }else if(vls instanceof List){
                    List<Object> ls = (List<Object>)vls;
                    sql.append(" between ");
                    Object val1 = ls.get(0);
                    Object val2 = ls.get(1);
                    sql.append(val1);
                    sql.append(" and ");
                    sql.append(val2);
                }
            }
        }

        sql.append(" order by ");
        sql.append(createDate);
        sql.append(" desc");
        return sql.toString();
    }

    public Map<String, Object> loadOpts(String tableId){
        Map<String, Object> ret = new HashMap<>();
        FormMeta formMeta = formMetaService.selectByPrimaryKey(tableId);
        JSONObject json = JSONObject.parseObject(formMeta.getTableCfg());
        JSONObject comps = json.getJSONObject("comps");
        for(String key : comps.keySet()){
            JSONObject comp = comps.getJSONObject(key);
            String id = comp.getString("id");
            Boolean searchCol = comp.getBoolean("searchCol");
            String type = comp.getString("type");
            if(searchCol != null && searchCol && ("radio".equals(type) || "select".equals(type))){
                JSONArray opts = comp.getJSONArray("options");
                ret.put(id, opts);
            }
        }
        return ret;
    }
}
