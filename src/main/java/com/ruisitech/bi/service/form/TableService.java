/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.form;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.dao.DaoHelper;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.util.RSBIUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * 表管理
 * @Author huangqin
 * @Date 2022/10/26 10:03 上午
 */
@Service("dwTableService")
public class TableService extends FormBaseService {

    private static Logger log = Logger.getLogger(FormMetaService.class);

    @Autowired
    private FormMetaService formMetaService;

    @Resource(name = "daoHelper")
    private DaoHelper daoHelper;

    @Autowired
    private FormMetaColService formMetaColService;

    @Transactional(rollbackFor = Exception.class)
    public void createTable(String id){
        FormMeta meta = formMetaService.selectByPrimaryKey(id);
        JSONObject json = JSONObject.parseObject(meta.getTableCfg());
        String delsql = "drop table if exists " + meta.getRealTableName();
        daoHelper.execute(delsql);

        String sql = this.createTableSql(json, meta);
        daoHelper.execute(sql);
        log.info(sql);

        //更新FormMeta
        meta.setVersion(1);
        meta.setBuildDate(new Date());
        formMetaService.updateByPrimaryKeySelective(meta);

        //写字段信息到表
        formMetaColService.deleteByTableId(id);
        JSONObject comps = json.getJSONObject("comps");
        JSONArray layouts = json.getJSONArray("layout");
        layouts.sort((a, b) -> {
            JSONObject a1 = (JSONObject) a;
            JSONObject b1 = (JSONObject) b;
            int x1 = a1.getInteger("x");
            int y1 = a1.getInteger("y");

            int x2 = b1.getInteger("x");
            int y2 = b1.getInteger("y");

            if (y1 == y2) {
                return x1 - x2;
            } else {
                return y1 - y2;
            }
        });
        for(int i=0; i<layouts.size(); i++) {
            JSONObject layout = layouts.getJSONObject(i);
            JSONObject comp = comps.getJSONObject(layout.getString("i"));
            String type = comp.getString("type");
            if("text".equals(type) || "table".equals(type)){
                continue;
            }
            String col = comp.getString("matchCol");
            String javaType = this.coverType2JavaType(type);
            String name = comp.getString("name");
            Integer length = comp.getInteger("length");
            String compId = comp.getString("id");
            Boolean required = comp.getBoolean("required");
            Boolean searchCol = comp.getBoolean("searchCol");
            FormMetaCol formMetaCol = new FormMetaCol();
            formMetaCol.setId(compId);  //把组件ID做字段ID
            formMetaCol.setColName(col);
            formMetaCol.setColDesc(name);
            formMetaCol.setColNote(name);
            formMetaCol.setColType(javaType);
            formMetaCol.setColOrd(i + 1);
            formMetaCol.setTableId(id);
            formMetaCol.setColLength(length);
            //formMetaCol.setColLength();
            formMetaCol.setCreateDate(new Date());
            formMetaCol.setUpdateDate(formMetaCol.getCreateDate());
            formMetaCol.setCreateUser(RSBIUtils.getLoginUserInfo().getUserId());
            formMetaCol.setCompType(type);
            formMetaCol.setRequired(required != null && required ? 1 : 0);
            formMetaCol.setSearchCol(searchCol != null && searchCol ? 1: 0 );
            formMetaColService.insertSelective(formMetaCol);
        }
    }

    public String createTableSql(JSONObject json, FormMeta form){
        JSONObject comps = json.getJSONObject("comps");
        String tname = form.getRealTableName();
        StringBuffer sb = new StringBuffer("");
        sb.append("create table " + tname + " (\n");
        int idx = 1;
        for(String key : comps.keySet()){
            JSONObject comp = comps.getJSONObject(key);
            String type = comp.getString("type");
            if("text".equals(type) || "table".equals(type)){
                continue;
            }
            String matchCol = comp.getString("matchCol");
            if(matchCol == null){
                matchCol = "c_" + idx;
                comp.put("matchCol", matchCol);
            }
            Integer length = comp.getInteger("length");
            Integer scale = comp.getInteger("scale");
            String colType = this.coverType2ColumnType(type, length, scale);
            sb.append("\t");
            sb.append(matchCol);
            sb.append(" ");
            sb.append(colType);
            sb.append(",");
            sb.append("\n");
            idx++;
        }
        /**
         * 对于填报表，字段创建填报表的ID, 和 userId 字段。表示这个数据的唯一标识和谁填的这行数据。
         */
        sb.append(""+tbbId+" varchar(36)");
        sb.append(" not null PRIMARY KEY,\n");
        sb.append(""+auditState+" int(1)");
        sb.append(",\n");
        sb.append(""+tbbUser+" varchar(36)");
        sb.append(",\n");
        sb.append("" + createDate + " datetime");
        sb.append(",\n");
        sb.append("" + updateDate + " datetime");
        sb.append(")");
        sb.append(" ENGINE=MyISAM CHARSET=utf8 COMMENT='"+form.getTableDesc()+"'");
        return sb.toString();
    }

    private String coverType2ColumnType(String compType, Integer length, Integer scale){
        if("input".equals(compType) || "radio".equals(compType) || "checkbox".equals(compType) ||
                "select".equals(compType)) {
            return "varchar(" + (length == null ? "200" : length) + ")";
        }else if("upload".equals(compType)){  //图片上传
            return "text";
        }else if("inputNumber".equals(compType)){
            return "DECIMAL(" + (length == null ? "16" : length)+","+(scale == null ? 2:scale)+")";
        }else if("switch".equals(compType) || "slider".equals(compType)){
            return "int("+(length == null ? "11" : length)+")";
        }else if( "datepicker".equals(compType) ){
            return "datetime";
        }else{
            throw new RuntimeException(compType + "类型未映射。");
        }
    }

    private String coverType2JavaType(String compType){
        if("input".equals(compType) || "radio".equals(compType) || "checkbox".equals(compType) ||
                "select".equals(compType) || "upload".equals(compType)) {
            return "String";
        }else if("inputNumber".equals(compType)){
            return "Double";
        }else if("switch".equals(compType) || "slider".equals(compType)){
            return "Int";
        }else if( "datepicker".equals(compType) ){
            return "Date";
        }else{
            throw new RuntimeException(compType + "类型未映射。");
        }
    }
}
