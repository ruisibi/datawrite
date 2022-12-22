package com.ruisitech.bi.entity.bireport;


import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
public class QueryDayDto {

	private String startDay;
	private String endDay;

	public int getBetweenDay() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		long l1 = sdf.parse(this.startDay).getTime();
		long l2 = sdf.parse(this.endDay).getTime();
		long result = Math.abs(l1 - l2) / (24 * 60 * 60 * 1000);
		return (int)result;
	}

}
