package com.ruisitech.bi.entity.bireport;

import com.ruisitech.bi.entity.common.PageParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 明细提取dto
 * @author hq
 *
 */
@Data
public class TableDetailDto extends PageParam implements Serializable {

	private Map<String, String> pms;

	private String cubeId;

}
