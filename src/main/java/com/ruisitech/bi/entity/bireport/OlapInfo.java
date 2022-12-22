package com.ruisitech.bi.entity.bireport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruisitech.bi.entity.common.BaseEntity;
import com.ruisitech.bi.util.RSBIUtils;
import lombok.Data;

@Data
public class OlapInfo extends BaseEntity {

	private String id;
	private String pageInfo;
	private String pageName;
	private String createUserName;

	@Override
	public void validate() {
		this.pageName = RSBIUtils.htmlEscape(this.pageName);
	}
}
