package com.zhou.core.flow;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	/**
	 *流量控制类
	 * @author zhouyongjun
	 *
	 */
public class FlowControl<T,V> implements IFlowControl<T, V>{
	private final Logger logger = LoggerFactory.getLogger(FlowControl.class);
	AtomicLong flow_bytes = new AtomicLong();
	long begin_date = System.currentTimeMillis();
	Map<T,FlowWithId<T,V>> joy_flows = new ConcurrentHashMap<>();
	String flowId;
	protected FlowControl() {
		
	}
	
	public void record(T k,V v,boolean isNeedOxHex,int bytes) {
		try {
//			if (!GameConfig.FLOW_CONTROL) return;
			flow_bytes.addAndGet(bytes);
			FlowWithId<T,V> flow  = null;
			if (joy_flows.containsKey(k)) flow = joy_flows.get(k);
			if (flow == null)  {
				flow = new FlowWithIdBuilder<T,V>()
						.setIsNeedOx(isNeedOxHex)
						.setId(k)
						.build();
				joy_flows.put(k, flow);
			}
			flow.record(v, bytes);
		} catch (Exception e) {
			logger.error("flow req record ["+k+","+v+","+bytes+"] is error...",e);
		}
	}
	
	public String writeLog(Logger write_logger) {
		try {
//			if (!GameConfig.FLOW_CONTROL) return;
			final Logger final_logger = write_logger == null ? logger : write_logger;
			StringBuffer sb = new StringBuffer();
			sb.append("\nflow control[").append(getFlowId()).append("]\n  all bytes[").append(flow_bytes.get()).append("]\n");
			for (FlowWithId<T,V> flow : new ArrayList<>(joy_flows.values())) {
				sb.append(flow.buildStringLog()).append("\n");
			}
			final_logger.info(sb.toString());
			return sb.toString();
		} catch (Exception e) {
			logger.error("",e);
			return "";
		}
	}
	
	public void clear() {
		try {
			flow_bytes.set(0);
			joy_flows.clear();
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	public void daily(Logger logger) {
		writeLog(logger);
		clear();
	}

	@Override
	public void setFlowId(String id) {
		this.flowId = id;
	}

	@Override
	public String getFlowId() {
		return flowId;
	}

}
