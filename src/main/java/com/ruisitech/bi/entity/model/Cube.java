package com.ruisitech.bi.entity.model;

import com.ruisitech.bi.entity.common.BaseEntity;
import com.ruisitech.bi.util.RSBIUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Cube extends BaseEntity {


	private String cubeName;
	private String desc;
	/**
	 * 主表
	 */
	private String tableId;
	private String tableName;

	private List<Dimension> dims;
	private List<Measure> kpis;

	/**
	 * 需要删除的对象, 包含 id, type
	 */
	private List<Map<String, Object>> delObj;

	 @Override
	public void validate() {
		 this.cubeName = RSBIUtils.htmlEscape(this.cubeName);
		 this.desc = RSBIUtils.htmlEscape(this.desc);
	 }
}
