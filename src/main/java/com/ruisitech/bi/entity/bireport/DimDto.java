package com.ruisitech.bi.entity.bireport;

import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class DimDto {

	private String id;
	private String type;
	private String colname; //码表在事实表中对应的字段名
	private String alias; //别名
	private List<String> vals; //码表的限制维
	private List<String> valDesc; //码表限制维的名称
	private String issum; //y,n两值
	private Integer calc;  //是否计算列
	private String tableColKey; //码表表KEY字段
	private String tableColName; //码表表name字段

	private String dimord; //维度排序方式
	private String ordcol; //维度排序字段
	private String dimdesc; //维度名称
	private String valType; //维度value 字段的类型，用在拼接sql中，判断是否增加单引号

	private String dimpos; //维度所在位置，行维度还是列维度
	private String pos; //col还是row, 用在图形中表示钻取维度的来源
	private String dateformat; //如果是时间维度，设置时间类型
	private String grouptype;
	private String iscas;
	private Integer top;
	private String topType;
	private String aggre;
	private Integer filtertype;
	private String st;
	private String end;
	private String cubeId;
	private String xdispName;
	private String tickInterval;
	private String routeXaxisLable;

	private QueryDayDto day;
	private QueryMonthDto month;

	public QueryDayDto getDay() {
		if(day == null){
			if("day".equals(type)  && st != null && st.length() > 0 && end != null && end.length() > 0){
				day = new QueryDayDto();
				day.setStartDay(this.st);
				day.setEndDay(this.end);
			}
		}
		return day;
	}
	public void setDay(QueryDayDto day) {
		this.day = day;
	}
	public QueryMonthDto getMonth() {
		if(month == null){
			if("month".equals(type)  && st != null && st.length() > 0 && end != null && end.length() > 0){
				month = new QueryMonthDto();
				month.setStartMonth(st);
				month.setEndMonth(end);
			}
		}
		return month;
	}
}
