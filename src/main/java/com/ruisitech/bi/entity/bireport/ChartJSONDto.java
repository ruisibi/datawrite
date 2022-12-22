package com.ruisitech.bi.entity.bireport;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ChartJSONDto {

	private String type;
	private Integer typeIndex;
	private List<DimDto> params;
	private String label;
	private DimDto xcol;
	private DimDto scol;
	private DimDto ycol; //"ycol":{"type":"kpi"}
	private Map<String, Object> link;

	private String maparea;
	private String mapAreaName;
	private String showLegend;
	private String legendLayout;
	private String legendpos;
	private String legendPosition;
	private String dataLabel;
	private String dataLabelColor;
	private String marginLeft;
	private String marginRight;
	private String markerEnabled;

	public List<DimDto> getDims(){
		List<DimDto> ret = new ArrayList<DimDto>();
		if(this.getXcol() != null && this.getXcol().getId() != null){
			ret.add(this.getXcol());
		}
		if(this.getScol() != null && this.getScol().getId() != null){
			ret.add(this.getScol());
		}
		if(this.getParams() != null && this.getParams().size() > 0){
			ret.addAll(this.getParams());
		}
		return ret;
	}
}
