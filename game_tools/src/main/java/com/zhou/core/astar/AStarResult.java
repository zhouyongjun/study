package com.zhou.core.astar;

import java.util.Map;

import com.zhou.core.astar.AStar.Node;

public class AStarResult {
	Node node;
	long count;
	String message;
	long useTime;
	Map<RectCell,Node> closed;
	public static AStarResult newInstance(Node node,long count,long useTime,Map<RectCell,Node> closed)
	{
		AStarResult result = new AStarResult();
		result.node = node;
		result.count = count;
		result.useTime = useTime;
		result.closed = closed;
		return result;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public boolean isSucc() {
		return node != null;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Map<RectCell, Node> getClosed() {
		return closed;
	}
	public void setClosed(Map<RectCell, Node> closed) {
		this.closed = closed;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (isSucc())
		{
			sb.append("Succ...");
		}else
		{
			sb.append("Fail...");
		}
		sb.append(node == null ? "Node=null" : node.toString())
		.append(", count=").append(count).append(", time=").append(useTime);
		return sb.toString();
	}
}
