package com.ruisitech.bi.entity.bireport;

import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

import java.util.Map;

@Data
public class KpiDto {

	private String aggre;
	private String col_name;
	private String fmt;
	private String alias;
	private String kpi_name;
	private String ydispName;
	private String tname; //指标所在表名称
	private String descKey; //指标解释KEY
	private Integer rate; //指标倍率
	private String unit; //指标单位
	private String kpi_id; //指标ID
	private String sort; //指标排序方式，用在SQL中
	private String order; //客户端排序
	private Integer min; //y轴最小值
	private Integer max; //Y轴最大值，用在仪表盘中

	private Integer calc;  //是否计算指标

	private KpiFilterDto filter; //对指标进行过滤

	private Map<String, Object> style; //指标样式
	private Map<String, Object> warning;  //指标预警
	private String compute; //指标计算方式（同比、环比、占比、排名等计算）

	private String funcname;  //回调函数名称
	private String code;  //回调函数内容
	private Boolean mergeData; //合并第二纵轴的数据
	private String tfontcolor;
	private Integer tfontsize;  //字体大小
}
