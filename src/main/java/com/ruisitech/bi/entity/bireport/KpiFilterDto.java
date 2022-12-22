package com.ruisitech.bi.entity.bireport;

import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

@Data
public class KpiFilterDto {

	private String kpi;
	private String filterType; //>,<,=,qj 四种
	private Double val1;
	private Double val2; //在区间匹配的时候，需要val2
}
