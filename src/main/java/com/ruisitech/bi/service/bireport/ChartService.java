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
import com.rsbi.ext.engine.view.context.chart.ChartContextImpl;
import com.rsbi.ext.engine.view.context.chart.ChartKeyContext;
import com.rsbi.ext.engine.view.context.dc.grid.GridDataCenterContext;
import com.rsbi.ext.engine.view.context.dc.grid.GridDataCenterContextImpl;
import com.rsbi.ext.engine.view.context.dc.grid.GridFilterContext;
import com.rsbi.ext.engine.view.context.dc.grid.GridSetConfContext;
import com.rsbi.ispire.dc.grid.GridFilter;
import com.ruisitech.bi.entity.bireport.*;
import com.ruisitech.bi.entity.form.FormMeta;
import com.ruisitech.bi.mapper.bireport.AreaMapper;
import com.ruisitech.bi.service.form.FormBaseService;
import com.ruisitech.bi.service.form.FormMetaService;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class ChartService extends BaseCompService {

	public final static String deftMvId = "mv.chart.tmp";

	@Resource
	private AreaMapper areaMapper;

	@Autowired
	private FormMetaService formMetaService;

	public @PostConstruct void init() {

	}

	public @PreDestroy void destory() {

	}

	public MVContext json2MV(ChartQueryDto chart, boolean xlsdata) throws Exception{

		//??????MV
		MVContext mv = new MVContextImpl();
		mv.setChildren(new ArrayList<Element>());
		String formId = ExtConstants.formIdPrefix + IdCreater.create();
		mv.setFormId(formId);
		mv.setMvid(deftMvId);

		//??????chart
		ChartContext cr = this.json2Chart(chart, false);
		cr.setXlsData(xlsdata);

		String sql = this.createSql(chart, 0);
		GridDataCenterContext dc = this.createDataCenter(chart.getChartJson(), sql);
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
		return mv;
	}

	public ChartContext json2Chart(ChartQueryDto chart, boolean is3g){
		ChartJSONDto chartJson = chart.getChartJson();
		List<KpiDto> kpiJson = chart.getKpiJson();
		ChartContext ctx = new ChartContextImpl();
		ctx.setLabel(chartJson.getLabel());
		//??????x
		DimDto xcol = chartJson.getXcol();
		if(xcol != null){
			String tp = xcol.getType();
			String alias = xcol.getAlias();
			String key = xcol.getTableColKey();
			String txt = xcol.getTableColName();
			if("day".equals(tp)){
				ctx.setDateType(tp);
				ctx.setDateTypeFmt(xcol.getDateformat());
			}
			if(key != null && key.length() > 0 && txt != null && txt.length() > 0){  //???????????????????????????????????????????????????
				ctx.setXcolDesc(key); //????????????ID,???????????????
				ctx.setXcol(txt);
			}else{
				ctx.setXcolDesc(alias);
				ctx.setXcol(alias);
			}
		}


		KpiDto kpiInfo = kpiJson.get(0);
		String y = kpiInfo.getAlias();
		ctx.setYcol(y);

		//??????????????????????????????????????? y2col
		if(kpiJson.size() > 1 && kpiJson.get(1) != null){
			ctx.setY2col(kpiJson.get(1).getAlias());
		}
		if(kpiJson.size() > 2 && kpiJson.get(2) != null){
			ctx.setY3col(kpiJson.get(2).getAlias());
		}
		DimDto scol = chartJson.getScol();
		if(scol != null){
			String alias = scol.getAlias();
			String key = scol.getTableColKey();
			String txt = scol.getTableColName();
			if(key != null && key.length() > 0 && txt != null && txt.length() > 0){  //???????????????????????????????????????????????????
				ctx.setScol(txt); //????????????ID,???????????????
			}else{
				ctx.setScol(alias);
			}
		}

		ctx.setShape(chartJson.getType());
		if(is3g){
			//??????????????????????????????100%
			ctx.setWidth("100%");
		}else{
			ctx.setWidth("auto");
		}
		ctx.setHeight("240");

		//??????ID
		ctx.setId(chart.getId());

		//??????????????????
		List<ChartKeyContext> properties = new ArrayList<ChartKeyContext>();
		ChartKeyContext val1 = new ChartKeyContext("margin", is3g?"30, 20, 50, 75":"30, 20, 50, 90");  //???????????????????????????
		properties.add(val1);

		//????????????  (???SQL????????????????????????????????????????????????????????????????????????????????? )
		if(kpiInfo.getRate() != null){
			ctx.setRate(kpiInfo.getRate());
		}
		if(kpiJson.size() > 1 && kpiJson.get(1) != null){
			ctx.setRate2(kpiJson.get(1).getRate());
		}
		if(kpiJson.size() > 2 && kpiJson.get(2) != null){
			ctx.setRate3(kpiJson.get(2).getRate());
		}

		properties.add(new ChartKeyContext("ydesc",kpiInfo.getKpi_name()+ "(" + super.writerUnit(kpiInfo.getRate()) +kpiInfo.getUnit()+")"));

		//?????????????????????
		if(kpiInfo.getFmt() != null && kpiInfo.getFmt().length() > 0){
			properties.add(new ChartKeyContext("formatCol","kpi_fmt"));
		}

		if(kpiInfo.getUnit() != null && kpiInfo.getUnit().length() > 0){
			properties.add(new ChartKeyContext("unitCol","kpi_unit"));
		}
		//????????????
		properties.add(new ChartKeyContext("action","drillChart"));

		if("pie".equals(ctx.getShape())){
			properties.add(new ChartKeyContext("showLegend","true"));
			//ctx.setHeight("280"); //??????????????????,??????
			if(!is3g){
				ctx.setWidth("600");
			}
		}
		if("gauge".equals(ctx.getShape()) && !is3g){
			ctx.setWidth("210");
		}
		if("radar".equals(ctx.getShape()) && !is3g){
			ctx.setHeight("340"); //??????????????????????????????
		}
		if("map".equals(ctx.getShape()) && !is3g){
			ctx.setWidth("600");
			ctx.setHeight("350");
		}
		if("bubble".equals(ctx.getShape()) || "scatter".equals(ctx.getShape())){
			KpiDto kpiInfo2 = kpiJson.get(1);
			//??????????????????????????????????????????xdesc
			properties.add(new ChartKeyContext("xdesc", kpiInfo2.getKpi_name() + "(" +  super.writerUnit(kpiInfo2.getRate()) +kpiInfo2.getUnit()+")"));
			properties.add(new ChartKeyContext("formatCol2", kpiInfo2.getFmt()));
			properties.add(new ChartKeyContext("unitCol2", kpiInfo2.getUnit()));
			//???????????????
			if("bubble".equals(ctx.getShape())){
				KpiDto kpiInfo3 = kpiJson.get(2);
				properties.add(new ChartKeyContext("formatCol3", kpiInfo3.getFmt()));
				properties.add(new ChartKeyContext("unitCol3", kpiInfo3.getUnit()));
			}
		}
		//?????????????????????????????????????????????
		if("line".equals(ctx.getShape()) || "column".equals(ctx.getShape())){
			properties.add(new ChartKeyContext("legendLayout","horizontal"));
		}
		//???????????????Legend
		if("pie".equals(ctx.getShape())){
			properties.add(new ChartKeyContext("showLegend","false"));
		}

		//??????????????????????????????????????? mapJson
		if("map".equals(ctx.getShape())){
			properties.add(new ChartKeyContext("mapJson",chartJson.getMaparea()));
		}
		//?????????????????????????????????
		if(is3g){
			properties.add(new ChartKeyContext("routeXaxisLable","-45"));
		}
		//????????????
		properties.add(new ChartKeyContext("showLabel", "false"));
		if(xcol != null){
			Integer top = xcol.getTop();
			if(top != null){
				properties.add(new ChartKeyContext("xcnt", String.valueOf(top)));
			}
		}

		ctx.setProperties(properties);

		return ctx;
	}

	/**
	 * ??????sql????????????????????????????????????????????????SQL
	 * ?????????????????????????????????????????????????????????X??????(xcol)????????????????????????xcol
	 * ???????????????????????????????????????????????????????????????(scol)??????????????????????????????
	 * release ???????????????????????????, 0 ?????????????????????1??????????????????????????????2????????????????????????
	 * @param chart
	 * @param release
	 * @return
	 * @throws ParseException
	 */
	public String createSql(ChartQueryDto chart, int release) throws ParseException{
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		List<DimDto> dims = chart.getChartJson().getDims();
		for(int i=0; i<dims.size(); i++){
			DimDto dim = dims.get(i);
			String key = dim.getTableColKey();
			String txt = dim.getTableColName();
			if(key != null && txt != null && key.length() >0 && txt.length() >0){
				sql.append(key+", " + txt + ",");
			}else{
				sql.append(" "+dim.getColname()+" as "+dim.getAlias()+", ");
			}

		}

		//?????????????????? ????????? ??????
		KpiDto info = chart.getKpiJson().get(0);
		if(info.getFmt() != null && info.getFmt().length() > 0){
			sql.append("'"+info.getFmt()+"' kpi_fmt,");
		}
		//??????????????????????????????
		if(info.getUnit() != null && info.getUnit().length() > 0){
			//sql.append("'" + ChartService.formatUnits(info)+info.getUnit()+"' kpi_unit,");
			sql.append("'" + info.getUnit()+"' kpi_unit,");
		}

		//???????????????
		if(chart.getKpiJson().size() > 1){
			KpiDto sinfo = chart.getKpiJson().get(1);
			if(sinfo != null) {
				if (sinfo.getFmt() != null && sinfo.getFmt().length() > 0) {
					sql.append("'" + sinfo.getFmt() + "' kpi_fmt2,");
				}
				if (sinfo.getUnit() != null && sinfo.getUnit().length() > 0) {
					sql.append("'" + sinfo.getUnit() + "' kpi_unit2,");
				}
			}
		}
		//???????????????
		if(chart.getKpiJson().size() > 2){
			KpiDto sinfo = chart.getKpiJson().get(2);
			if(sinfo != null) {
				if (sinfo.getFmt() != null && sinfo.getFmt().length() > 0) {
					sql.append("'" + sinfo.getFmt() + "' kpi_fmt3,");
				}
				if (sinfo.getUnit() != null && sinfo.getUnit().length() > 0) {
					sql.append("'" + sinfo.getUnit() + "' kpi_unit3,");
				}
			}
		}

		List<KpiDto> kpis = chart.getKpiJson();
		if(kpis.size() == 0){
			sql.append(" 0 kpi_value ");
		}else{
			for(int i=0; i<kpis.size(); i++){
				KpiDto kpi = kpis.get(i);
				if(kpi == null){
					continue;
				}
				if(kpi.getCalc() != null && kpi.getCalc() == 1){  //??????????????????????????????
					sql.append(kpi.getCol_name() + " ");
				}else{  //??????????????????
					String name = super.convertKpiName(kpi);
					sql.append( name + " ");
				}
				sql.append(kpi.getAlias());
				if(i != kpis.size() - 1){
					sql.append(",");
				}
			}
		}

		FormMeta formMeta = formMetaService.selectByPrimaryKey(chart.getCubeId());
		String master = formMeta.getTableName();
		sql.append(" from " + master + " a0");
		sql.append(" where ");
		sql.append(FormBaseService.auditState);
		sql.append(" = 2");

		for(int i=0; i<dims.size(); i++){
			DimDto dim = dims.get(i);
			//??????????????????
			if(dim.getType().equals("day")){
				if(dim.getDay() != null){
					sql.append(" and " + dim.getColname() + " between '"+dim.getDay().getStartDay()+"' and '" + dim.getDay().getEndDay()+"'");
				}else
				if(dim.getVals() != null && dim.getVals().size() > 0){
					String vls = RSBIUtils.dealStringParam(dim.getVals());
					sql.append(" and " + dim.getColname() + " in ("+vls+")");
				}
			}else
			if(dim.getType().equals("month")){
				if(dim.getMonth() != null){
					sql.append(" and " + dim.getColname() + " between '"+dim.getMonth().getStartMonth()+"' and '" + dim.getMonth().getEndMonth()+"'");
				}else
				if(dim.getVals() != null && dim.getVals().size() > 0){
					String vls = RSBIUtils.dealStringParam(dim.getVals());
					sql.append(" and " + dim.getColname() + " in ("+vls+")");
					//isDealDate = true;
				}
			}else{
				//??????????????????
				if(dim.getVals() != null && dim.getVals().size() > 0){
					String  vls = RSBIUtils.dealIntegerParam(dim.getVals());
					if("string".equalsIgnoreCase(dim.getValType())){
						vls = RSBIUtils.dealStringParam(dim.getVals());
					}
					sql.append(" and " + dim.getColname() + " in ("+vls+")");
				}
			}

		}

		//???????????????????????????
		List<ParamDto> params = chart.getParams();
		for(int i=0; params!=null&&i<params.size(); i++){
			ParamDto param = params.get(i);
			String cubeId = param.getCubeId();
			String tp = param.getType();
			String colname = param.getColname();
			String alias = param.getAlias();
			String dateformat = param.getDateformat();
			//????????????????????????????????????????????????????????????????????????
			if((tp.equals("day") || tp.equals("month"))){
				if(release == 0 && param.getSt() != null && param.getSt().length() > 0 ){
					sql.append(" and " + colname + " between '"+ param.getSt() + "' and '" +  param.getEnd() + "'");
				}else if(release == 1){
					sql.append(" and " + colname + " between '$s_"+alias+"' and '$e_"+alias+"'");
				}else if(release == 2){
					sql.append(" #if($"+alias+" != '') and " + colname + " = $"+alias + " #end");   //????????????????????????,????????????????????????
				}
			}else{
				if(release == 0 && param.getVals() != null && param.getVals().size() > 0){
					//?????????????????????
					String  vls = RSBIUtils.dealIntegerParam(param.getVals());
					if("string".equalsIgnoreCase(param.getValType())){
						vls = RSBIUtils.dealStringParam(param.getVals());
					}
					sql.append(" and " +  colname + " in ("+vls+")");
				}else if(release == 1 || release == 2){
					sql.append(" #if($"+alias+" != '') and " + colname + " in ($extUtils.printVals($"+alias+", '"+param.getValType()+"')) #end");
				}
			}
		}

		/**
		Map<String, Object> linkAccept = chart.getChartJson().getLinkAccept();
		if(linkAccept != null && !linkAccept.isEmpty()){
			String col = (String)linkAccept.get("col");
			String valtype = (String)linkAccept.get("valType");
			String ncol = "$" + col;
			if("string".equalsIgnoreCase(valtype)){
				ncol = "'" + ncol + "'";
			}
			sql.append(" and  " + col + " = " + ncol);
		}
		**/

		if(dims.size() > 0){
			sql.append(" group by ");
			for(int i=0; i<dims.size(); i++){
				DimDto dim = dims.get(i);
				String key = dim.getTableColKey();
				String txt = dim.getTableColName();
				if(key != null && txt != null && key.length() >0 && txt.length() >0){
					sql.append(key+", " + txt);
				}else{
					sql.append(dim.getColname());
				}
				if(i != dims.size() - 1){
					sql.append(",");
				}
			}
		}
		//??????????????????
		StringBuffer filter = new StringBuffer("");
		for(KpiDto kpi : chart.getKpiJson()){
			if(kpi == null){
				continue;
			}
			if(kpi.getFilter() != null){
				filter.append(" and "+kpi.getCol_name()+" ");
				String tp = kpi.getFilter().getFilterType();
				filter.append(tp);
				filter.append(" ");
				double val1 = kpi.getFilter().getVal1();
				if(kpi.getFmt() != null && kpi.getFmt().endsWith("%")){
					val1 = val1 / 100;
				}
				filter.append(val1 * (kpi.getRate() == null ? 1 : kpi.getRate()));
				if("between".equals(tp)){
					double val2 = kpi.getFilter().getVal2();
					if(kpi.getFmt() != null && kpi.getFmt().endsWith("%")){
						val2 = val2 / 100;
					}
					filter.append(" and " + val2 * (kpi.getRate() == null ? 1 : kpi.getRate()));
				}
			}
		}
		if(filter.length() > 0){
			sql.append(" having 1=1 " + filter);
		}
		if(dims.size() > 0){
			StringBuffer order = new StringBuffer();
			order.append(" order by ");
			KpiDto kpi = chart.getKpiJson().get(0);
			if(kpi.getSort() != null && kpi.getSort().length() > 0){
				order.append(kpi.getAlias() + " " + kpi.getSort()) ;
				order.append(",");
			}
			for(int i=0; i<dims.size(); i++){
				DimDto dim = dims.get(i);
				if(dim.getDimord() != null && dim.getDimord().length() > 0){
					order.append(dim.getTableColKey() != null && dim.getTableColKey().length() > 0 ? dim.getTableColKey() : dim.getColname());
					order.append(" ");
					order.append(dim.getDimord());
					order.append(",");
				}
			}
			if(order.length() <= 11 ){  //????????????????????? order by ??????

			}else{
				//?????????????????????????????????
				 sql.append(order.toString().substring(0, order.length() - 1));
			}
		}
		String ret = sql.toString();
		//?????? ## ??? ?????????##???velocity??????????????????
		ret = ret.replaceAll("##", "\\$extUtils.printJH()").replaceAll("@", "'");
		return ret;
	}

	/**
	 * ???????????????dataCenter
	 * @param sql
	 * @param chartJson
	 * @return
	 * @throws IOException
	 */
	public GridDataCenterContext createDataCenter(ChartJSONDto chartJson, String sql) throws IOException{
		List<DimDto> dims = chartJson.getDims();
		GridDataCenterContext ctx = new GridDataCenterContextImpl();
		GridSetConfContext conf = new GridSetConfContext();
		conf.setUseCache(false);
		ctx.setConf(conf);
		ctx.setId("DC-" + IdCreater.create());
		String name = TemplateManager.getInstance().createTemplate(sql);
		ctx.getConf().setTemplateName(name);
		String maparea = chartJson.getMaparea();
		String type = chartJson.getType();
		if("map".equals(type) && maparea != null && maparea.length() > 0 && !"china".equals(maparea)){  //????????????????????????????????????????????????????????????????????????
			for(int i=0; i<dims.size(); i++){
				DimDto dim = dims.get(i);
				if(dim.getType().equals("city")){  //??????
					GridFilterContext filter = new GridFilterContext();
					filter.setColumn(dim.getTableColName() != null && dim.getTableColName().length() > 0 ? dim.getTableColName() : dim.getAlias());
					filter.setFilterType(GridFilter.in);
					List<Area> ls = areaMapper.listCityByProvCode(maparea);
					StringBuffer sb = new StringBuffer();
					for(int j=0; j<ls.size(); j++){
						Area a = ls.get(j);
						sb.append(a.getCityName());
						if(j != ls.size() - 1){
							sb.append(",");
						}
					}
					filter.setValue(sb.toString());
					ctx.getProcess().add(filter);  //??????????????????
				}
			}
		}
		return ctx;
	}
}
