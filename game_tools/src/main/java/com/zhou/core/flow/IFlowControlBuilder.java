package com.zhou.core.flow;


public class IFlowControlBuilder<T,V> {
	String flowId;
	public IFlowControlBuilder<T,V> setFlowId(String id)
	{
		this.flowId = id;
		return this;
	}
	public IFlowControl<T, V> build()
	{
		IFlowControl<T,V> control = new FlowControl<T, V>();
		control.setFlowId(flowId);
		return control;
	}
}
