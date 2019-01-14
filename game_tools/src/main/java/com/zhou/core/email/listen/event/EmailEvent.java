package com.zhou.core.email.listen.event;

import com.zhou.core.common.CEvent;
import com.zhou.core.email.EmailListener;

public interface EmailEvent extends CEvent {
	String getShowName();
	EmailListener getListener();
}
