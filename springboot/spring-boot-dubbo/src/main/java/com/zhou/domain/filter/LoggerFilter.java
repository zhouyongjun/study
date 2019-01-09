package com.zhou.domain.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;

/**
 * 支持 Dubbo Filter 过滤器
 * @author zhouyongjun
 *
 */
@Activate
public class LoggerFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {
		RpcInvocation rpcInvocation = (RpcInvocation)invocation;
//		rpcInvocation.addAttachments(attachments);
		RpcContext context = RpcContext.getContext();
		//是否服务调用方
		boolean isConsumerSide = context.isConsumerSide();
		//是否服务提供方
		boolean isProviderSide = context.isProviderSide();
		//获取HOST地址
		String host = context.getLocalHost();
		//获取PORT
		int port = context.getLocalPort();
		
		String remoteAddress = context.getRemoteAddressString();
		//获取被调用的服务接口名称
		String serviceInterface = context.getUrl().getServiceInterface();
		//获取被调用的服务方法名
		String methodName = context.getMethodName();
		System.err.println(">>>>>>> Logge  Filter before...");
		Result result = invoker.invoke(invocation);
		System.err.println(">>>>>>> LoggerFilter after...");
		return result;
	}

}
