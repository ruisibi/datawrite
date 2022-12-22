package com.ruisitech.bi.service.bireport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.ExtConstants;
import com.rsbi.ext.engine.init.TemplateManager;
import com.rsbi.ext.engine.util.IdCreater;
import com.rsbi.ext.engine.view.context.Element;
import com.rsbi.ext.engine.view.context.MVContext;
import com.rsbi.ext.engine.view.context.MVContextImpl;
import com.rsbi.ext.engine.view.context.chart.ChartContext;
import com.rsbi.ext.engine.view.context.cross.CrossReportContext;
import com.rsbi.ext.engine.view.context.dc.grid.GridDataCenterContext;
import com.rsbi.ext.engine.view.context.form.*;
import com.rsbi.ext.engine.view.context.html.DivContext;
import com.rsbi.ext.engine.view.context.html.DivContextImpl;
import com.rsbi.ext.engine.view.context.html.TextContext;
import com.rsbi.ext.engine.view.context.html.TextContextImpl;
import com.ruisitech.bi.entity.bireport.ChartQueryDto;
import com.ruisitech.bi.entity.bireport.DimDto;
import com.ruisitech.bi.entity.bireport.ParamDto;
import com.ruisitech.bi.entity.bireport.TableQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
@Scope("prototype")
public class ReportService extends BaseCompService {

	public final static String deftMvId = "mv.export.tmp";

	@Autowired
	private ChartService chartService;
	@Autowired
	private TableService tableService;

	Map<String, InputField> params = new HashMap<>();

	public @PostConstruct void init() {

	}

	public @PreDestroy void destory() {

	}


	public MVContext json2MV(JSONObject json, int release) throws Exception{
		//创建MV
		MVContext mv = new MVContextImpl();
		mv.setChildren(new ArrayList<Element>());
		String formId = ExtConstants.formIdPrefix + IdCreater.create();
		mv.setFormId(formId);
		mv.setMvid(deftMvId);
		JSONArray ps = json.getJSONArray("params");
		ParamDto[] ls = JSONArray.toJavaObject(ps, ParamDto[].class);
		List<ParamDto> params = Arrays.asList(ls);
		//构建参数
		if(!ps.isEmpty()){
			if(release == 0){
				StringBuffer sb = new StringBuffer("参数： ");
				TextContext parStr = new TextContextImpl();
				for(int i=0; i<params.size(); i++){
					ParamDto param =params.get(i);
					String name = param.getName();
					String type = param.getType();
					//String colname = param.getString("colname");
					if("month".equals(type) || "day".equals(type)){
						sb.append(name + "(" + (param.getSt() == null ? "无" : param.getSt()) + " 至 " + (param.getEnd() == null ? "无" : param.getEnd()) + ")");
					}else{
						sb.append(name + "(" + (param.getValStrs() == null ? "无" : param.getValStrs())+")");
					}
					sb.append("  ");
				}
				parStr.setText(sb.toString());
				mv.getChildren().add(parStr);
				parStr.setParent(mv);
			}else {
				//把参数变成动态值
				DivContext div = new DivContextImpl();
				div.setStyleClass("rpeortParam");
				div.setChildren(new ArrayList<Element>());
				mv.getChildren().add(div);
				div.setParent(mv);
				for (int i = 0; i < params.size(); i++) {
					ParamDto param = params.get(i);
					String name = param.getName();
					String type = param.getType();
					String colname = param.getColname();
					List<String> values = param.getVals();

					InputField input = null;
					InputField input2 = null;
					if ("day".equalsIgnoreCase(type)) {
						DateSelectContext target = new DateSelectContextImpl();
						String val = (String) param.getSt();
						target.setDefaultValue(val == null ? "" : val.replaceAll("-", ""));
						target.setDesc("开始" + name);
						target.setId("s_" + colname);
						input = target;

						//创建第二个参数
						DateSelectContext target2 = new DateSelectContextImpl();
						String val2 = (String) param.getEnd();
						target2.setDefaultValue(val2 == null ? "" : val2.replaceAll("-", ""));
						target2.setDesc("结束" + name);
						target2.setId("e_" + colname);
						input2 = target2;
					} else if ("month".equalsIgnoreCase(type)) {
						SelectContextImpl target = new SelectContextImpl();
						String sql = this.createMonthSql();
						String template = TemplateManager.getInstance().createTemplate(sql);
						target.setTemplateName(template);
						input = target;
						input.setDefaultValue(param.getSt());
						input.setDesc("开始" + name);
						input.setId("s_" + colname);

						//创建第二个参数
						SelectContextImpl target2 = new SelectContextImpl();
						String template2 = TemplateManager.getInstance().createTemplate(sql);
						target2.setTemplateName(template2);
						target2.setDefaultValue(param.getEnd());
						target2.setDesc("结束" + name);
						target2.setId("e_" + colname);
						input2 = target2;
					} else {
						MultiSelectContextImpl target = new MultiSelectContextImpl();
						String sql = this.createDimSql(param);
						String template = TemplateManager.getInstance().createTemplate(sql);
						target.setTemplateName(template);
						input = target;
						input.setDefaultValue(values == null ? "" : values.get(0));
						input.setDesc(name);
						input.setId(colname);
					}
					div.getChildren().add(input);
					input.setParent(div);
					this.params.put(input.getId(), input);
					if (input2 != null) {
						div.getChildren().add(input2);
						input2.setParent(div);
						this.params.put(input2.getId(), input2);
					}
				}
				ButtonContext btn = new ButtonContextImpl();
				btn.setDesc("查询");
				btn.setType("button");
				btn.setMvId(new String[]{deftMvId});
				div.getChildren().add(btn);
				btn.setParent(div);
			}
		}
		JSONArray comps = json.getJSONArray("comps");
		List<String> dsids = new ArrayList<String>();
		for(int i=0; i<comps.size(); i++){
			JSONObject obj = comps.getJSONObject(i);
			String dsid = (String)obj.get("dsid");
			if(dsid != null && dsid.length() > 0 && !dsids.contains(dsid)){
				dsids.add(dsid);
			}
			String type = obj.getString("type");
			if("table".equals(type)){
				TableQueryDto dto = JSONObject.toJavaObject(obj, TableQueryDto.class);
				dto.setParams(params);
				createTable(mv, dto, release);
			}
			if("chart".equals(type)){
				ChartQueryDto dto = JSONObject.toJavaObject(obj, ChartQueryDto.class);
				dto.setParams(params);
				createChart(mv, dto, release);
			}
		}
		return mv;
	}

	/**
	 *
	 * @param mv
	 * @param release  判断当前是否为发布状态, 0 表示不是发布，1表示发布到多维分析，2表示发布到仪表盘
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public void createTable(MVContext mv, TableQueryDto table, int release) throws IOException, ParseException{
		String cubeId = table.getCubeId();
		if(cubeId == null || cubeId.length() == 0){
			return;
		}

		//添加kpiOther
		DimDto kpiOther = new DimDto();
		kpiOther.setType("kpiOther");
		table.getCols().add(kpiOther);
		CrossReportContext cr = tableService.json2Table(table);
		//移除kpiOther
		table.getCols().remove(table.getCols().size() - 1);
		cr.setId("comp");
		cr.setOut("html");
		cr.setShowData(true);

		String sql = tableService.createSql(table, release);
		//创建datacenter
		GridDataCenterContext dc = tableService.createDataCenter(sql, table);
		cr.setRefDataCetner(dc.getId());
		if(mv.getGridDataCenters() == null){
			mv.setGridDataCenters(new HashMap<String, GridDataCenterContext>());
		}
		mv.getGridDataCenters().put(dc.getId(), dc);

		mv.getChildren().add(cr);
		cr.setParent(mv);

		Map<String, CrossReportContext> crs = new HashMap<String, CrossReportContext>();
		crs.put(cr.getId(), cr);
		mv.setCrossReports(crs);

		String scripts = tableService.getScripts().toString();
		if( scripts.length() > 0){
			mv.setScripts(scripts);
		}
	}

	public void createChart(MVContext mv, ChartQueryDto chart, int release) throws IOException, ParseException{


		//创建钻取维度
		StringBuffer sb = new StringBuffer("");
		int cnt = 0;
		for(DimDto dim : chart.getChartJson().getParams()){
			if(cnt == 0){
				sb.append("钻取维：");
			}
			sb.append(dim.getDimdesc()+"("+dim.getValDesc()+") - ");
			cnt++;
		}
		String drillText = sb.toString();
		if(drillText.length() > 0){
			TextContext txt = new TextContextImpl();
			txt.setText(drillText);
			mv.getChildren().add(txt);
			txt.setParent(mv);
		}
		ChartContext cr = chartService.json2Chart(chart, false);
		cr.setId("comp");
		String sql = chartService.createSql(chart, release);
		GridDataCenterContext dc = chartService.createDataCenter(chart.getChartJson(), sql);
		cr.setRefDataCenter(dc.getId());
		if(mv.getGridDataCenters() == null){
			mv.setGridDataCenters(new HashMap<String, GridDataCenterContext>());
		}
		mv.getGridDataCenters().put(dc.getId(), dc);

		mv.getChildren().add(cr);
		cr.setParent(mv);

		Map<String, ChartContext> crs = new HashMap<String, ChartContext>();
		crs.put(cr.getId(), cr);
		mv.setCharts(crs);
	}

	public String createDimSql(ParamDto dim){
		String sql = "select "+dim.getTableColKey()+" \"value\", "+dim.getTableColName()+" \"text\" from ";
		sql += " order by "+dim.getTableColKey()+" "  + dim.getDimord();
		return sql;
	}

	public String createMonthSql(){
		String sql = "select mid \"value\", mname \"text\" from code_month order by mid desc";
		return sql;
	}

	public Map<String, InputField> getParams() {
		return params;
	}
}
