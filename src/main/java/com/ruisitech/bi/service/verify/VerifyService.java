/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.verify;

import com.rsbi.ext.engine.dao.DaoHelper;
import com.rsbi.ext.engine.util.DaoUtils;
import com.rsbi.ext.engine.view.context.grid.PageInfo;
import com.rsbi.ext.engine.view.exception.ExtConfigException;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.entity.verify.VerifyDto;
import com.ruisitech.bi.entity.write.DataLoadDto;
import com.ruisitech.bi.mapper.form.FormMetaMapper;
import com.ruisitech.bi.service.form.FormBaseService;
import com.ruisitech.bi.service.form.FormMetaColService;
import com.ruisitech.bi.service.form.FormMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 数据审核Service
 * @Author huangqin
 * @Date 2022/11/22 5:31 下午
 */
@Service
public class VerifyService extends FormBaseService {

    @Resource
    private FormMetaMapper mapper;

    @Autowired
    private FormMetaColService formMetaColService;

    @Autowired
    private FormMetaService formMetaService;

    @Resource(name = "daoHelper")
    private DaoHelper daoHelper;

    public List<FormMeta> list(String typeId){
        return mapper.listWirteForms(typeId);
    }

    public List<Map<String, Object>> queryTableDatas(DataLoadDto dto) throws ExtConfigException {
        List<FormMetaCol> cols = formMetaColService.selectByTableId(dto.getTableId());
        String sql = this.createSql(cols, dto.getTableName(), dto);
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

    private String createSql(List<FormMetaCol> cols, String tname, DataLoadDto dto){
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
        sql.append(" from ");
        sql.append(tname);
        sql.append(" where "+ auditState +" = 0 ");
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

    public void verify(VerifyDto dto){
        FormMeta meta = formMetaService.selectByPrimaryKey(dto.getTableId());
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<dto.getIds().size(); i++){
            sb.append("?");
            if(i != dto.getIds().size() - 1) {
                sb.append(",");
            }
        }
        String sql = "update " + meta.getTableName()+" set " + auditState + " = ? where "+tbbId+" in ("+sb+")";
        daoHelper.execute(sql, ps->{
            ps.setInt(1, dto.getAudit());
            for(int i=0; i<dto.getIds().size(); i++) {
                ps.setString(i + 2, dto.getIds().get(i));
            }
            ps.executeUpdate();
            return null;
        });
    }

    public FormMeta getTableInfo(String tableId){
        FormMeta formMeta = formMetaService.selectByPrimaryKey(tableId);
        List<FormMetaCol> cols = formMetaColService.selectByTableId(tableId);
        formMeta.setCols(cols);
        return formMeta;
    }
}
