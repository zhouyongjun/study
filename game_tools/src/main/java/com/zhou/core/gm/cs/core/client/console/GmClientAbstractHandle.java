package  com.zhou.core.gm.cs.core.client.console;

import org.apache.mina.core.session.IoSession;

import com.zhou.core.gm.cs.core.client.console.filter.codec.ClientAbstractGmResp;

/**
 * ��̨������
 * @author zhouyongjun
 *
 */
public abstract class GmClientAbstractHandle extends ClientAbstractGmResp{
	public abstract boolean execute(ClientAbstractGmResp request,IoSession session);
	
}
