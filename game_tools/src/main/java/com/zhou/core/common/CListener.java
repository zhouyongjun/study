package com.zhou.core.common;

import com.zhou.util.CParam;
import com.zhou.util.CResult;

public interface CListener extends CTickable{
	/**
	 * ���� 
	 * @param param
	 * @return
	 */
	CResult listen(CParam param);
}
