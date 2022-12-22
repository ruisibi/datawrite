package com.ruisitech.bi.entity.bireport;

import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class ParamDto {

	private String type;
	private String colname;
	private String alias;
	private String valType;
	private String dateformat;
	private String st;
	private String end;
	private List<String> vals;
	private List<String> valStrs;
	private String valDesc;
	private String id;
	private String cubeId;
	private String colDesc;
	private String tableName;
	private String dimord;
	private String tableColKey;
	private String tableColName;
	private String dsid;
	private String grouptype;
	private String name;
	private Integer filtertype;
	private Integer calc; //是否动态指标
}
