package com.zhou.core.esb.server.filter.decode;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.zhou.core.esb.common.Const;

public class DefaultProtocolDecoder implements ProtocolDecoder {

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		Const.log(this.getClass().getName(),"decode",session);
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
		// TODO Auto-generated method stub
		Const.log(this.getClass().getName(),"finishDecode",session);

	}

	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		Const.log(this.getClass().getName(),"dispose",session);
	}

}
