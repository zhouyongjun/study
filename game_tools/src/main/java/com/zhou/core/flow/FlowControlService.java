package com.zhou.core.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流量控制服务入口
 * @author zhouyongjun
 *
 */

public final class FlowControlService {
	private final static Logger logger = LoggerFactory.getLogger(FlowControlService.class); 
	@SuppressWarnings("rawtypes")
	private static Map<String,IFlowControl> controls = new HashMap<String,IFlowControl>();

	@SuppressWarnings("unchecked")
	public static <K,V> IFlowControl<K,V> getFlowControl(String flowId)
	{
		if (!controls.containsKey(flowId)) return null;
		return (IFlowControl<K, V>) controls.get(flowId);
	}
	
	
	public static <K,V> void putFlowControl(String flowId ,IFlowControl<K,V> flow)
	{
		controls.put(flowId, flow);
	}
	
	@SuppressWarnings("rawtypes")
	public static  Map<String, IFlowControl> getControls() {
		return controls;
	}
	
	public static <K,V> IFlowControl<K,V> putFlowControl(String flowId,K k,V v,boolean isNeedOxHex,int bytes)
	{
		try {
			IFlowControl<K,V> flow = getFlowControl(flowId);
			if (flow == null)
			{
				flow = new IFlowControlBuilder<K,V>().setFlowId(flowId).build();
				putFlowControl(flowId, flow);
			}
			flow.record(k, v, isNeedOxHex, bytes);
			return flow;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<IFlowControl> asFlowControlList()
	{
		return new ArrayList<>(controls.values());
	}
	
	@SuppressWarnings("rawtypes")
	public void daily(String flowId,Logger write_logger)
	{
		if (flowId == null)
		{
			for (IFlowControl flow : asFlowControlList())
			{
				flow.daily(write_logger);
			}	
		}else
		{
			IFlowControl flowControl = getFlowControl(flowId);
			if (flowControl != null) flowControl.daily(write_logger);
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public void writeLog(String flowId,Logger write_logger)
	{
		if (flowId == null)
		{
			for (IFlowControl flow : asFlowControlList())
			{
				flow.writeLog(write_logger);
			}	
		}else
		{
			IFlowControl flowControl = getFlowControl(flowId);
			if (flowControl != null) flowControl.writeLog(write_logger);
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public void clear(String flowId,Logger write_logger)
	{
		if (flowId == null)
		{
			for (IFlowControl flow : asFlowControlList())
			{
				flow.clear();
			}	
		}else
		{
			IFlowControl flowControl = getFlowControl(flowId);
			if (flowControl != null) flowControl.clear();
		}
		
	}
}
