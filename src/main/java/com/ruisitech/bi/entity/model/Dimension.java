package com.ruisitech.bi.entity.model;

import lombok.Data;

@Data
public class Dimension extends CubeColMeta {

	private String name;
	private String type;
	private String colkey;
	private String coltext;
	private String dimord;
	private String ordcol;
	private String vtype;
	private String colTable;
	private String groupId;
	private String groupName;
	private String dateformat;
}
