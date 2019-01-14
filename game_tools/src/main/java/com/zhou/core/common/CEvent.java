package com.zhou.core.common;

import com.zhou.util.CParam;
import com.zhou.util.CResult;

public interface CEvent {
	CResult handle(CParam param) throws Exception;
	void load(CParam param) throws Exception;
}
