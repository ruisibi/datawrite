package com.ruisitech.bi.entity.model;

import lombok.Data;

@Data
public class Measure extends CubeColMeta {

	private String name;
	private String kpinote;
	private String unit;
	private String fmt;
	private String aggre;
	/**
	 * 0否，1是
	 */
	private Integer calcKpi;
	private String cubeId;

	//获取聚合字段字符串
	public String getAggreCol() {
		if("count(distinct)".equals(aggre)){  //需要特殊处理
			return "count(distinct " + super.getCol() + ")";
		}else{  //sum/avg/max/min/count 不需要特殊处理
			return aggre +"(" + super.getCol() +")";
		}
	}

}
