package com.ruisitech.bi.service.bireport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.dao.DaoHelper;
import com.rsbi.ext.engine.dao.DatabaseHelper;
import com.rsbi.ext.engine.util.P;
import com.rsbi.ext.engine.view.builder.dsource.DataSourceBuilder;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.dsource.DataSourceContext;
import com.rsbi.ext.engine.view.context.grid.PageInfo;
import com.rsbi.ext.engine.view.exception.ExtConfigException;
import com.rsbi.ext.engine.wrapper.ExtRequest;
import com.rsbi.ext.engine.wrapper.TestRequestImpl;
import com.ruisitech.bi.entity.bireport.TableDetailDto;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.entity.form.FormMetaCol;
import com.ruisitech.bi.service.form.FormBaseService;
import com.ruisitech.bi.service.form.FormMetaColService;
import com.ruisitech.bi.service.form.FormMetaService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 提取表格明细Service
 * @author hq
 *
 */
@Service
public class TableDetailService extends BaseCompService {

	@Autowired
	private FormMetaColService formMetaColService;

	@Autowired
	private FormMetaService formMetaService;

	@Autowired
	private DaoHelper daoHelper;

	/**
	 * 获取表格的字段
	 * @param dto
	 * @return
	 */
	public List<FormMetaCol> getTableHeader(TableDetailDto dto){
		List<FormMetaCol> cols = formMetaColService.selectByTableId(dto.getCubeId());
		return cols;
	}

	/**
	 * 根据纬度提取明细
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> detailDatas(TableDetailDto dto) throws ExtConfigException {
		String sql = this.createSql(dto);
		PageInfo page = new PageInfo();
		if(dto.getPage() != null){
			page.setCurtpage(dto.getPage() - 1);
		}
		if(dto.getRows() != null){
			page.setPagesize(dto.getRows());
		}
		List<Map<String, Object>> ls = this.querySql(sql, page, dto);
		dto.setTotal((int)page.getAllsize());
		return ls;
	}

	private List<Map<String, Object>> querySql(String sql, PageInfo page, TableDetailDto dto) throws ExtConfigException {

		if(page == null){
			return daoHelper.queryForList(sql);
		}else{
			String nsql = "select count(*) cnt from (" + sql + ") ttt";
			int cnt = daoHelper.queryForInt(nsql);
			page.setAllsize(cnt);
			DatabaseHelper db = ExtContext.getInstance().getDatabaseHelper("mysql");
			sql = db.getQueryPageSql(sql, page);
			return daoHelper.queryForList(sql);
		}
	}

	public String createSql(TableDetailDto dto){
		StringBuffer sb = new StringBuffer();

		sb.append("select ");
		List<FormMetaCol> cols = formMetaColService.selectByTableId(dto.getCubeId());
		for(int i=0; i<cols.size(); i++){
			FormMetaCol col = cols.get(i);
			sb.append(col.getColName());
			sb.append(" as ");
			sb.append("c" + i);
			if(i != cols.size() - 1){
				sb.append(",");
			}
		}
		FormMeta formMeta = formMetaService.selectByPrimaryKey(dto.getCubeId());
		sb.append(" from " + formMeta.getTableName() + " a0");


		sb.append(" where ");
		sb.append(FormBaseService.auditState);
		sb.append(" = 2");

		for(Map.Entry<String, String> p : dto.getPms().entrySet()){
			String name = p.getKey();
			String val = p.getValue();
			FormMetaCol col = findColByAlias(name, cols);
			String colname = col.getColName();
			String type = col.getColType();
			if("Double".equalsIgnoreCase(type) || "Int".equalsIgnoreCase(type)){
				sb.append(" and " + colname  + " = " + val);
			}else{
				sb.append(" and " + colname + " = '" + val+"'");
			}
		}
		return sb.toString().replaceAll("@", "'");
	}

	private FormMetaCol findColByAlias(String alias, List<FormMetaCol> cols){
		FormMetaCol ret = null;
		for(int i=0; i<cols.size(); i++){
			FormMetaCol col = cols.get(i);
			String name = col.getColName();
			if(name.equalsIgnoreCase(alias)){
				ret = col;
				break;
			}
		}
		return ret;
	}

	public void exportDatas(TableDetailDto dto, HttpServletResponse res) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sql = this.createSql(dto);
		List<Map<String, Object>> ls = this.querySql(sql, null, dto);
		OutputStream os =  res.getOutputStream();

		WritableWorkbook workbook = Workbook.createWorkbook( os );
		WritableSheet sheet = workbook.createSheet( "页1", 0);
		List<FormMetaCol> cols = formMetaColService.selectByTableId(dto.getCubeId());
		//写head
		for(int i=0; i<cols.size(); i++){
			FormMetaCol col = cols.get(i);
			WritableCell cell = new Label( i, 0, col.getColNote() );
			sheet.addCell(cell);
		}
		//写数据
		for(int i=0; i<ls.size(); i++){
			Map<String, Object> data = ls.get(i);
			for(int j=0; j<cols.size(); j++){
				//JSONObject col = cols.getJSONObject(i);
				WritableCell cell = null;
				Object value = data.get("c"+j);
				if(value == null){
					cell = new Label( j, i, "" );
				}else{
					if(value instanceof Date){
						Date d = (Date)value;
						cell = new Label( j, i + 1, sdf.format( d ) );
					}else if(value instanceof Integer){
						cell = new jxl.write.Number(j, i + 1, ((Integer) value).intValue());
					}else if(value instanceof Double){
						cell = new jxl.write.Number(j, i + 1, ((Double) value).doubleValue());
					} else if(value instanceof BigDecimal) {
						cell = new jxl.write.Number(j, i + 1, ((BigDecimal) value).doubleValue());
					}else{
						cell = new Label( j, i + 1, value.toString() );
					}
				}
				sheet.addCell( cell );
			}
		}
		workbook.write( );
		workbook.close( );
	}

}
