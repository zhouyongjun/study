package com.zhou.core.flow;

public class FlowWithIdBuilder<T,V> {
	boolean isNeedOX;
	T id;
	public FlowWithIdBuilder<T,V> setIsNeedOx(boolean isNeedOX){
		this.isNeedOX = isNeedOX;
		return this;
	}
	
	public FlowWithIdBuilder<T,V> setId(T id){
		this.id = id;
		return this;
	}
	
	public FlowWithId<T,V> build() {
		return new FlowWithId<T,V>(isNeedOX,id);
	}

	
}
