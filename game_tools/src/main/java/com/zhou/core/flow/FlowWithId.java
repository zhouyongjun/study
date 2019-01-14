package com.zhou.core.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	/**
	 * 流量控制单元类型
	 * @author zhouyongjun
	 *
	 * @param <T> 单元ID
	 * @param <V>  流量变化类型
	 */
public class FlowWithId<T,V> {
	private final Logger logger = LoggerFactory.getLogger(FlowWithId.class);
	public T id;
	public AtomicLong all_flow = new AtomicLong();
	public Map<V,VBytes> key_flows = new ConcurrentHashMap<>();
	boolean isNeedOxHex;//鏄惁16杩涘埗
	
	public FlowWithId(boolean isNeedOxHex,T key)  {
		this.isNeedOxHex = isNeedOxHex;
		this.id = key;
	}
	
	public static class VBytes
	{
		long bytes;
		long times;
		public synchronized void add(long add)
		{
			times ++;
			bytes += add;
		}
		
		public long getBytes() {
			return bytes;
		}
		
		public long getTimes() {
			return times;
		}
	}
	
	public void record(V key,int bytes) {
		try {
//			if (!GameConfig.FLOW_CONTROL) return;
			all_flow.addAndGet(bytes);
			VBytes atomic  = null;
			if (key_flows.containsKey(key)) atomic = key_flows.get(key);
			if (atomic == null)  {
				atomic = new VBytes();
				key_flows.put(key, atomic);
			}
			atomic.add(bytes);
		} catch (Exception e) {
			logger.error("FlowWithId record ["+key+","+key+","+bytes+"] is error...",e);
		}
	}
	/**
	 * 鍐欐棩蹇�
	 * @return
	 */
	public String buildStringLog() {
		StringBuffer sb = new StringBuffer();
		sb.append("    key[").append(id).append("] all bytes[").append(all_flow.get()).append("] :\n");
		List<Entry<V,VBytes>> entrys = new ArrayList<>(key_flows.entrySet());
		Collections.sort(entrys, new Comparator<Entry<V,VBytes>>() {
			@Override
			public int compare(Entry<V, VBytes> o1,
					Entry<V, VBytes> o2) {
				long del = o2.getValue().getBytes() - o1.getValue().getBytes();
				if (del > 0) return 1;
				else if (del < 0) return -1;
				return 0;
			}
		});
		for (Entry<V,VBytes> entry : entrys) {
			sb.append(isNeedOxHex ? ("      value[0x"+Integer.toHexString((Integer)entry.getKey())+"]") :("      value["+entry.getKey()+"]"))
			.append("\t times=").append(entry.getValue().getTimes()).append("\t bytes=").append(entry.getValue().getBytes()).append("\n");
		}
		return sb.toString();
	}
}
