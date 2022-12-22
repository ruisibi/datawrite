/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.write;

import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.dao.DaoHelper;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.entity.write.DataSaveDto;
import com.ruisitech.bi.entity.write.FileUploadDto;
import com.ruisitech.bi.service.form.FormBaseService;
import com.ruisitech.bi.service.form.FormMetaColService;
import com.ruisitech.bi.service.form.FormMetaService;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName WriteSaveService
 * @Description WriteSaveService
 * @Author huangqin
 * @Date 2022/10/31 10:54 上午
 */
@Service
public class WriteSaveService extends FormBaseService {

    @Autowired
    private FormMetaService formMetaService;

    @Resource(name = "daoHelper")
    private DaoHelper daoHelper;

    @Value("${spring.datasource.dbType}")
    private String dbName;

    @Autowired
    private FormMetaColService formMetaColService;

    @Autowired
    private UpLoadFileService upLoadFileService;

    public void saveData(DataSaveDto dto){
        FormMeta formMeta = formMetaService.selectByPrimaryKey(dto.getTableId());
        JSONObject json = JSONObject.parseObject(formMeta.getTableCfg());
        JSONObject comps = json.getJSONObject("comps");

        List<FormMetaCol> cols = formMetaColService.selectByTableId(dto.getTableId());

        String sql = this.createSql(formMeta, cols);
        daoHelper.execute(sql, ps->{
            int idx = 1;
            for(FormMetaCol col : cols){
                JSONObject comp = comps.getJSONObject(col.getId());
                String compType = comp.getString("type");
                Object val = dto.getValues().get(col.getId());
                if(val == null){
                    ps.setNull(idx, Types.INTEGER);
                }else{
                    String type = col.getColType();
                    if ("Double".equals(type)) {
                        ps.setDouble(idx, Double.parseDouble(val.toString()));
                    } else if ("Int".equals(type)) {
                        ps.setInt(idx, (Integer)val);
                    } else if ("String".equals(type)) {
                        //特殊处理图形上传
                        if("upload".equals(compType)){
                            List<FileUploadDto> files = upLoadFileService.listFiles(val.toString());
                            ps.setString(idx, files == null ? null : JSONObject.toJSONString(files));
                        }else {
                            ps.setString(idx, val.toString());
                        }
                    } else if ("Date".equals(type)) {
                        JSONObject prop = comp.getJSONObject("properties");
                        SimpleDateFormat sdf = new SimpleDateFormat(prop.getString("fmt"));
                        try {
                            ps.setDate(idx, new java.sql.Date(sdf.parse(val.toString()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                idx++;
            }
            ps.setString(idx, UUID.randomUUID().toString().replaceAll("-", ""));
            idx++;
            ps.setString(idx, RSBIUtils.getLoginUserInfo().getId());
            idx++;
            ps.setInt(idx, 0);
            idx++;
            ps.setTimestamp(idx, new Timestamp(new Date().getTime()));
            ps.executeUpdate();
            return null;
        });
    }

    public String createSql(FormMeta form, List<FormMetaCol> cols){
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(form.getTableName());
        sb.append("(");
        for(FormMetaCol col : cols){
            sb.append(col.getColName());
            sb.append(",");
        }
        sb.append(tbbId + ",");
        sb.append(tbbUser+",");
        sb.append(auditState+",");
        sb.append(createDate);
        sb.append(")");
        sb.append(" values(");
        for(FormMetaCol col : cols){
            sb.append("?");
            sb.append(",");
        }
        sb.append("?,");
        sb.append("?,");
        sb.append("?,");
        sb.append("?");
        sb.append(")");
        return sb.toString();
    }

    public void updateData(DataSaveDto dto){
        FormMeta formMeta = formMetaService.selectByPrimaryKey(dto.getTableId());
        JSONObject json = JSONObject.parseObject(formMeta.getTableCfg());
        JSONObject comps = json.getJSONObject("comps");

        List<FormMetaCol> cols = formMetaColService.selectByTableId(dto.getTableId());

        String sql = this.createUpdateSql(formMeta, cols);
        daoHelper.execute(sql, ps->{
            int idx = 1;
            for(FormMetaCol col : cols){
                JSONObject comp = comps.getJSONObject(col.getId());
                String compType = comp.getString("type");
                Object val = dto.getValues().get(col.getId());
                if(val == null){
                    ps.setNull(idx, Types.INTEGER);
                }else{
                    String type = col.getColType();
                    if ("Double".equals(type)) {
                        ps.setDouble(idx, Double.parseDouble(val.toString()));
                    } else if ("Int".equals(type)) {
                        ps.setInt(idx, (Integer)val);
                    } else if ("String".equals(type)) {
                        //特殊处理图形上传
                        if("upload".equals(compType)){
                            List<FileUploadDto> files = upLoadFileService.listFiles(val.toString());
                            ps.setString(idx, files == null ? null : JSONObject.toJSONString(files));
                        }else {
                            ps.setString(idx, val.toString());
                        }
                    } else if ("Date".equals(type)) {
                        JSONObject prop = comp.getJSONObject("properties");
                        SimpleDateFormat sdf = new SimpleDateFormat(prop.getString("fmt"));
                        try {
                            ps.setDate(idx, new java.sql.Date(sdf.parse(val.toString()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                idx++;
            }
            ps.setString(idx, dto.getId());
            ps.executeUpdate();
            return null;
        });
    }

    private String createUpdateSql(FormMeta form, List<FormMetaCol> cols){
        StringBuilder sb = new StringBuilder("update ");
        sb.append(form.getTableName());
        sb.append(" set ");
        for(FormMetaCol col : cols){
            sb.append(col.getColName());
            sb.append(" = ");
            sb.append("?");
            sb.append(",");
        }
        sb.append(updateDate);
        sb.append(" = ");
        sb.append(RSBIUtils.getDbDateFunction(dbName));
        sb.append(",");
        sb.append(auditState);
        sb.append(" = ");
        sb.append("0");
        sb.append(" where ");
        sb.append(tbbId);
        sb.append(" = ");
        sb.append("?");
        return sb.toString();
    }

    public void deleteData(DataSaveDto dto){
        FormMeta formMeta = formMetaService.selectByPrimaryKey(dto.getTableId());
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<dto.getIds().size(); i++){
            sb.append("'");
            sb.append(dto.getIds().get(i));
            sb.append("'");
            if(i != dto.getIds().size() - 1){
                sb.append(",");
            }
        }
        String sql = "delete from " + formMeta.getTableName()+" where "+tbbId+" in ("+sb+")";
        daoHelper.execute(sql);
    }
}
