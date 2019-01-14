package  com.zhou.core.gm.cs.core.server.console;

import org.apache.mina.core.session.IoSession;

import com.zhou.core.gm.cs.core.server.console.filter.codec.AbstractGmReq;

/**
 * 后台请求处理
 * @author zhouyongjun
 *
 */
public abstract class AbstractGmHandle extends AbstractGmReq{
	public abstract boolean execute(AbstractGmReq request,IoSession session);
	
}
