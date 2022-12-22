package com.ruisitech.bi.entity.bireport;

import lombok.Data;

import java.util.List;

@Data
public class ChartQueryDto {

	private String id;
	private String cubeId;

	private List<KpiDto> kpiJson;

	private ChartJSONDto chartJson;

	private List<ParamDto> params;
}
