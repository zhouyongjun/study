package com.zhou.core.email;

import com.zhou.core.common.CFilter;

/**
 * �ʼ�filter
 * @author zhouyongjun
 *
 */
public interface EmailFilter extends CFilter{
	
	void parse(String param);
}
