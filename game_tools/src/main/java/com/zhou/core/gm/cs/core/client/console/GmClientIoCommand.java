package  com.zhou.core.gm.cs.core.client.console;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.chain.IoHandlerCommand;

import com.zhou.core.gm.cs.buffer.GmIoBuffer;
import com.zhou.core.gm.cs.core.client.console.filter.codec.ClientAbstractGmResp;
import com.zhou.core.gm.cs.log.GmLog;
	/**
	 * 执行Handler命令
	 * @author zhouyongjun
	 *
	 */
public class GmClientIoCommand implements IoHandlerCommand{

	@Override
	public void execute(NextCommand next, IoSession session, Object message)throws Exception {
		try {
			GmIoBuffer in = (GmIoBuffer)message;
			ClientAbstractGmResp request = getResponse(session, in);
			if (request == null) {
				return;
			}
			GmLog.info("GM response[0x" + Integer.toHexString(request.getProtocolId())+"] handle["+request.getClass().getSimpleName()+"]");
			byte result = in.get();
			String resultMsg = in.getString();
			request.handle(in, session, result,resultMsg);
		} catch (Exception e) {
			GmLog.error(e);
		}
	}
	
	public ClientAbstractGmResp getResponse(IoSession session,GmIoBuffer in) {
		int pro_id = in.getInt();
		//获取协议对象
		ClientAbstractGmResp request = ClientAbstractGmResp.getResponse(pro_id);
		if (request == null) {
			GmLog.info("GM response[0x" + Integer.toHexString(pro_id)+"] is not exist...");
			in.position(in.limit());
			return null;
		}
		return request;
	}

}
