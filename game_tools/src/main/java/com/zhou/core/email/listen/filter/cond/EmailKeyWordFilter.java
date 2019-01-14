package com.zhou.core.email.listen.filter.cond;

import java.util.HashSet;
import java.util.Set;

import com.zhou.core.email.EmailConfig;
import com.zhou.util.CParam;
import com.zhou.util.CResult;
/**
 * ¹Ø¼ü×Ö¹ýÂË
 * @author zhouyongjun
 *
 */
public class EmailKeyWordFilter implements CondFilter{
	//¹Ø¼ü×Ö
	protected Set<String[]> matching_keywords = new HashSet<>();
	@Override
	public CResult filter(CParam param) {
		/*
		 * ÊÇ·ñÆ¥Åä¹Ø¼ü×Ö
		 */
		String text = param.get("text",String.class);
		if (matching_keywords.isEmpty() || text == null || text.length() == 0)
			return CResult.RESULT_FAIL;
		aa : for (String[] keywords : matching_keywords) {
			for (String keyword : keywords) {
				if (!text.contains(keyword)) continue aa;
			}
			return CResult.RESULT_SUCC;
		}
		return CResult.RESULT_FAIL;
	}
	@Override
	public void parse(String data) {
		String[] arrays = data.split("\\|");
		Set<String[]> temps = new HashSet<>();
		for (String string :arrays) {
			if (string.isEmpty()) continue;
			temps.add(string.split("\\&"));
		}
		this.matching_keywords = temps;
	}

}
