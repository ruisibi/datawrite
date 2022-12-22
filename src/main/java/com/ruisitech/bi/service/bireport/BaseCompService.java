package com.ruisitech.bi.service.bireport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.util.P;
import com.rsbi.ext.engine.view.context.MVContext;
import com.rsbi.ext.engine.view.context.dsource.DataSourceContext;
import com.rsbi.ext.engine.view.context.form.InputField;
import com.rsbi.ext.engine.view.context.form.TextFieldContext;
import com.rsbi.ext.engine.view.context.form.TextFieldContextImpl;
import com.rsbi.ext.engine.view.exception.ExtConfigException;
import com.rsbi.ispire.dc.grid.GridShift;
import com.ruisitech.bi.entity.bireport.KpiDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 组件基类Service
 * @author hq
 *
 */
public abstract class BaseCompService {

	protected JSONObject pageBody; //页面配置信息

	public Map<String, String> createTableAlias(JSONObject dset){
		Map<String, String> tableAlias = new HashMap<String, String>();
		tableAlias.put(dset.getString("master"), "a0");
		JSONArray joinTabs = (JSONArray)dset.get("joininfo");
		for(int i=0; joinTabs != null && i<joinTabs.size(); i++){
			JSONObject tab = joinTabs.getJSONObject(i);
			tableAlias.put(tab.getString("ref"), "a" + (i+1));
		}
		return tableAlias;
	}


	public int type2value(String tp){
		int curDate = 4;;
		if(tp.equals("year")){
			curDate = 4;
		}else if(tp.equals("quarter")){
			curDate = 3;
		}else if(tp.equals("month")){
			curDate = 2;
		}else if(tp.equals("day")){
			curDate = 1;
		}
		return curDate;
	}

	public String loadFieldName(String aggre) {
		if("sum".equalsIgnoreCase(aggre)){
			return "合计值";
		}else if("avg".equalsIgnoreCase(aggre)){
			return "均值";
		}else if("max".equalsIgnoreCase(aggre)){
			return "最大值";
		}else if("min".equalsIgnoreCase(aggre)){
			return "最小值";
		}else if("count".equalsIgnoreCase(aggre)){
			return "计数";
		}else if("var".equalsIgnoreCase(aggre)){
			return "方差";
		}else if("sd".equalsIgnoreCase(aggre)){
			return "标准差";
		}else if("middle".equalsIgnoreCase(aggre)){
			return "中位数";
		}else{
			return "合计";
		}
	}

	public String resetVals(String inputval, String type, String dateFormat, int jstype) throws ParseException {
		if(jstype == 0){
			return inputval;
		}
		String[] vals = inputval.split(",");
		List<String> rets = new ArrayList<String>();
		for(String val : vals){
			//先添加他自己
			if(!rets.contains(val)){
				rets.add(val);
			}
			if(jstype == 1 || jstype == 3){ //上期
				String nval = GridShift.getDateShiftValue(val, type, dateFormat, "sq");
				if(!rets.contains(nval)){
					rets.add(nval);
				}
			}
			if(jstype == 2 || jstype == 3){ //同期
				String nval = GridShift.getDateShiftValue(val, type, dateFormat, "tq");
				if(!rets.contains(nval)){
					rets.add(nval);
				}
			}
		}
		return list2String(rets);
	}

	public String list2String(List<String> rets){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<rets.size(); i++){
			String ret = rets.get(i);
			sb.append(ret);
			if(i != rets.size() - 1){
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 根据指标计算的值筛选，从新设置时间字段的数据区间，主要针对日、月份的数据区间控制
	 */
	public String[] resetBetween(String start, String end, String type, String dateFormat, int jstype) throws ParseException {
		if(jstype == 0){ //无计算
			return new String[]{start, end};
		}
		if("day".equals(type)){
			if(jstype == 1 || jstype == 3){ //上期
				String nval = GridShift.getDateShiftValue(start, type, dateFormat, "sq");
				start = nval;
			}
			if(jstype == 2 || jstype == 3){ //同期
				String nval2 = GridShift.getDateShiftValue(start, type, dateFormat, "tq");
				start = nval2;
			}
			return new String[]{start, end};
		}else if("month".equals(type)){
			if(jstype == 1 || jstype == 3){ //上期
				String nval = GridShift.getDateShiftValue(start, type, dateFormat, "sq");
				start = nval;
			}
			if(jstype == 2 || jstype == 3){ //同期
				String nval = GridShift.getDateShiftValue(start, type, dateFormat, "tq");
				start = nval;
			}
			return new String[]{start, end};
		}else{
			return null;
		}
	}

	//输出单位比例
	public String writerUnit(Integer bd){
		if(bd == null){
			return "";
		}else{
			int v = bd.intValue();
			if(v == 1){
				return "";
			}else if(v == 100){
				return "百";
			}else if(v == 1000){
				return "千";
			}else if(v == 10000){
				return "万";
			}else if(v == 1000000){
				return "百万";
			}else if(v == 100000000){
				return "亿";
			}else{
				return "*" + v;
			}
		}
	}

	/**
	 * 获取字段别名
	 * @param kpi
	 * @return
	 */
	public String convertKpiName(KpiDto kpi){
		String colName = kpi.getCol_name();
		String tname = kpi.getTname();
		if(tname == null || tname.length() == 0){
			return colName;
		}
		String alias = kpi.getTname() + ".";
		String name = colName.replaceAll("\\((\\S+)\\)", "(" + alias+"$1" + ")");
		return name;
	}

	public void setPageBody(JSONObject pageBody) {
		this.pageBody = pageBody;
	}


}
