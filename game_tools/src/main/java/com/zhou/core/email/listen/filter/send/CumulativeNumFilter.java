package com.zhou.core.email.listen.filter.send;

import com.zhou.util.CParam;
import com.zhou.util.CResult;

/**
 * ÀÛ¼ÆÊýÁ¿
 * @author zhouyongjun
 *
 */
public class CumulativeNumFilter implements SendedFilter{
	int num;
	@Override
	public void parse(String param) {
		num = Integer.parseInt(param.split(":")[1]);
	}

	@Override
	public CResult filter(CParam param) {
		int temp = param.get("num", Integer.class);
		if (temp >= num) return CResult.RESULT_SUCC;
		return CResult.RESULT_FAIL;
	}

}
