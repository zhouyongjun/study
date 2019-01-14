package com.zhou.core.email.listen.filter.send;

import com.zhou.util.CParam;
import com.zhou.util.CResult;
import com.zhou.util.CalendarUtil;

public class TimeOverFilter implements SendedFilter {
	int minute;
	long next_succ_date;
	@Override
	public void parse(String param) {
		minute = Integer.parseInt(param.split(":")[1]);
		next_succ_date = System.currentTimeMillis() + minute * CalendarUtil.MINUTE; 
		System.err.println("next_succ_date : " + CalendarUtil.format(next_succ_date));
	}

	@Override
	public CResult filter(CParam param) {
		if (next_succ_date > System.currentTimeMillis()) return CResult.RESULT_FAIL;
		System.err.println("next_succ_date : " + CalendarUtil.format(next_succ_date));
		next_succ_date = System.currentTimeMillis() + minute * CalendarUtil.MINUTE;
		return CResult.RESULT_SUCC;
	}

}
