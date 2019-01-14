package com.zhou.core.esb.client.listen;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class ClientNetService implements IoServiceListener{

	@Override
	public void serviceActivated(IoService service) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serviceIdle(IoService service, IdleStatus idleStatus)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serviceDeactivated(IoService service) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void write(IoSession session,Object obj)
	{
		session.write(obj);
	}
	
}
