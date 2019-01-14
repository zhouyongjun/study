package com.zhou.core.esb.server.filter.encode;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.zhou.core.esb.common.Const;

public class DefaultProtocolEncoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		Const.log(this.getClass().getName(),"encode",session);
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		Const.log(this.getClass().getName(),"dispose",session);
	}

}
