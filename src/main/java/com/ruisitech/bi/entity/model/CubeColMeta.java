package com.ruisitech.bi.entity.model;

import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

@Data
public class CubeColMeta extends BaseEntity {

	private String cubeId;
	/**
	 * 1 维度，2 度量
	 */
	private Integer colType;
	private String colId;
	private String tname;
	private String col;
	private String alias;
	/**
	 * 1 是， 0 否
	 */
	private Integer calc;
	private Integer ord;
	private String targetId;
	/**
	 * 维度或度量是否被修改
	 */
	private String isupdate;

	@Override
	public void validate() {

	}
}
