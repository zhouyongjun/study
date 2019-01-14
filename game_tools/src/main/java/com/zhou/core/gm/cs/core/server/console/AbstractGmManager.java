package  com.zhou.core.gm.cs.core.server.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.zhou.core.gm.cs.core.server.console.filter.codec.AbstractGmResp;
import com.zhou.core.gm.cs.core.server.console.filter.codec.DefaultGmResp;
import com.zhou.core.gm.cs.log.GmLog;
public abstract class AbstractGmManager implements IoServiceListener{
	protected Map<Long,GmUser> sessions = new HashMap<Long, GmUser>();
	protected Set<AbstractGmHandle> handles = new HashSet<AbstractGmHandle>();
	@Override
	public void serviceActivated(IoService arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void serviceDeactivated(IoService arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void serviceIdle(IoService arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		GmLog.info("sessionCreated ["+session.hashCode()+"]: " + session);
		sessions.put(session.getId(), GmUser.createEntry(session));
		addSession(session);
	}
	
	@Override
	public void sessionDestroyed(IoSession session) throws Exception {
		GmLog.info("sessionDestroyed ["+session.hashCode()+"]: " + session);
		removeSession(session);
	}
	

	/**
	 * GM客户端用户退出
	 * @param user
	 */
	public abstract void logout(GmUser user);
	
	public abstract void stop();
	/**
	 * 注册所有的Handle
	 */
	public abstract void registerHandles();
	public abstract void init();
	/**
	 * 启动GM后台服务器的相关数据
	 */
	public void startUp() {
		init();
		registerHandles();
	}
	
	
	public Map<Long, GmUser> getSessions() {
		return sessions;
	}
	public Set<AbstractGmHandle> getHandles() {
		return handles;
	}
	
	/**
	 * 停服 
	 */
	public void shutDown() {
		stopAllSessions();
		stop();
	}
	
	private void stopAllSessions() {
		for (GmUser user : sessions.values()) {
			logout(user);
		}
		sessions.clear();		
	}
	/**
	 *添加客户端的SESSION 
	 * @param session
	 */
	public void addSession(IoSession session) {
		sessions.put(session.getId(), GmUser.createEntry(session));
		GmLog.info("session[0x"+Long.toHexString(session.getId())+"] is being added,session : "+ session);
	}
	
	/**
	 * 删除客户端的SESSION
	 * @param session
	 */
	public void removeSession(IoSession session) {
		sessions.remove(session.getId());
		GmLog.info("session[0x"+Long.toHexString(session.getId())+"] is being remove,session : "+ session);
	}
	
	/**
	 * 获取GM帐号的信息
	 * @param sid
	 * @return
	 */
	public GmUser getGmClientUser(long sid) {
		if (sessions.containsKey(sid)) {
			return sessions.get(sid);
		}
		return null;		
	}
	/**
	 * Gm客户端登陆
	 * @param id
	 * @param gmName
	 */
	public void login(IoSession session, String gmName,int pro_id) {
		/*
		 * 考虑是否帐号已经登录过，若有就用则移除账号的老SESSION
		 */
		GmLog.info("GM  session["+session+"]user["+gmName+"] login !!!");
		List<GmUser> list = new ArrayList<GmUser>();
 		for (GmUser entry  : sessions.values()) {
			if (gmName != null && gmName.equals(entry.getGmUser())) {
				list.add(entry);
			}
		}
 		for (GmUser entry : list) {
 			DefaultGmResp response = new DefaultGmResp(pro_id,"您被强制退出，他人使用了该账号");
 			sendMessageToClinet(entry.getSession(), response);
 		}
 		GmUser entry  = sessions.get(session.getId());
 		entry.setGmUser(gmName);
	}
	
	/**
	 * 指定的客户端连接
	 * @param session
	 * @param response
	 */
	public void sendMessageToClinet(IoSession session, AbstractGmResp response) {
		if (response == null) {
			return;
		}
		session.write(response);
	}
	
	/**
	 * 所有的连接下发
	 * @param response
	 */
	public void sendMessageToClinetAll(AbstractGmResp response) {
		for (GmUser entry : sessions.values()) {
			sendMessageToClinet(entry.getSession(), response);
 		}
	}
	
}
