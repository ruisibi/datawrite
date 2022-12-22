package com.ruisitech.bi.entity.bireport;

import com.ruisitech.bi.entity.common.BaseEntity;
import lombok.Data;

import java.text.ParseException;

@Data
public class QueryMonthDto {

	private String startMonth;
	private String endMonth;

	public int getBetweenMonth() throws ParseException{
		int year1 = Integer.parseInt(this.startMonth.substring(0,4));
		int year2 = Integer.parseInt(this.endMonth.substring(0,4));

		int month1 = Integer.parseInt(this.startMonth.substring(4,6));
		int month2 = Integer.parseInt(this.endMonth.substring(4,6));

		int betweenMonth = month2 - month1;
		int betweenYear = year2 - year1;


		return betweenYear * 12 + betweenMonth;
	}
}
