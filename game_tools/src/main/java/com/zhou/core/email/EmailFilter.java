package com.zhou.core.email;

import com.zhou.core.common.CFilter;

/**
 * ÓÊ¼þfilter
 * @author zhouyongjun
 *
 */
public interface EmailFilter extends CFilter{
	
	void parse(String param);
}
