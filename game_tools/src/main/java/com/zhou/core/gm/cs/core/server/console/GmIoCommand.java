package  com.zhou.core.gm.cs.core.server.console;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.chain.IoHandlerCommand;

import com.zhou.core.gm.cs.buffer.GmIoBuffer;
import com.zhou.core.gm.cs.core.server.GmServer;
import com.zhou.core.gm.cs.core.server.console.filter.codec.AbstractGmReq;
import com.zhou.core.gm.cs.core.server.console.filter.codec.AbstractGmResp;
import com.zhou.core.gm.cs.log.GmLog;
import com.zhou.test.gm.GmServerMain;
	/**
	 * 执行Handler命令
	 * @author zhouyongjun
	 *
	 */
public class GmIoCommand implements IoHandlerCommand{

	@Override
	public void execute(NextCommand next, IoSession session, Object message)throws Exception {
		try {
			GmIoBuffer in = (GmIoBuffer)message;
			GmUser user = GmServer.getInstance().getGmMgr().getGmClientUser(session.getId());
//			AbstractGmReq request = getRequest(session, in);
//			if (request == null) {
//				return;
//			}
//			GmLog.info("GM request[0x" + Integer.toHexString(request.getProtocolId())+"] handle["+request.getClass().getSimpleName()+"] gm os_user["+request.client_os_user+"],ip["+request.client_ips+"]");
//			request.handle(in, session, user);
			GmServer.getInstance().getGmMgr().sendMessageToClinet(session, new AbstractGmResp(0) {
				
				@Override
				public void body() {
					out.putInt(1);
				}
			});
		} catch (Exception e) {
			GmLog.error(e);
		}
	}
	
	public AbstractGmReq getRequest(IoSession session,GmIoBuffer in) {
		int pro_id = in.getInt();
		//获取协议对象
		AbstractGmReq request = AbstractGmReq.getRequest(pro_id);
		if (request == null) {
			GmLog.info("GM request[0x" + Integer.toHexString(pro_id)+"] is not exist...");
			in.position(in.limit());
			return null;
		}
		request.client_os_user = in.getString();
		request.client_ips = in.getString();
		return request;
	}

}
