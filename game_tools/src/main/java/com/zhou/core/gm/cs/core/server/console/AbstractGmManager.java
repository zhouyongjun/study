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
	 * GM�ͻ����û��˳�
	 * @param user
	 */
	public abstract void logout(GmUser user);
	
	public abstract void stop();
	/**
	 * ע�����е�Handle
	 */
	public abstract void registerHandles();
	public abstract void init();
	/**
	 * ����GM��̨���������������
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
	 * ͣ�� 
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
	 *��ӿͻ��˵�SESSION 
	 * @param session
	 */
	public void addSession(IoSession session) {
		sessions.put(session.getId(), GmUser.createEntry(session));
		GmLog.info("session[0x"+Long.toHexString(session.getId())+"] is being added,session : "+ session);
	}
	
	/**
	 * ɾ���ͻ��˵�SESSION
	 * @param session
	 */
	public void removeSession(IoSession session) {
		sessions.remove(session.getId());
		GmLog.info("session[0x"+Long.toHexString(session.getId())+"] is being remove,session : "+ session);
	}
	
	/**
	 * ��ȡGM�ʺŵ���Ϣ
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
	 * Gm�ͻ��˵�½
	 * @param id
	 * @param gmName
	 */
	public void login(IoSession session, String gmName,int pro_id) {
		/*
		 * �����Ƿ��ʺ��Ѿ���¼�������о������Ƴ��˺ŵ���SESSION
		 */
		GmLog.info("GM  session["+session+"]user["+gmName+"] login !!!");
		List<GmUser> list = new ArrayList<GmUser>();
 		for (GmUser entry  : sessions.values()) {
			if (gmName != null && gmName.equals(entry.getGmUser())) {
				list.add(entry);
			}
		}
 		for (GmUser entry : list) {
 			DefaultGmResp response = new DefaultGmResp(pro_id,"����ǿ���˳�������ʹ���˸��˺�");
 			sendMessageToClinet(entry.getSession(), response);
 		}
 		GmUser entry  = sessions.get(session.getId());
 		entry.setGmUser(gmName);
	}
	
	/**
	 * ָ���Ŀͻ�������
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
	 * ���е������·�
	 * @param response
	 */
	public void sendMessageToClinetAll(AbstractGmResp response) {
		for (GmUser entry : sessions.values()) {
			sendMessageToClinet(entry.getSession(), response);
 		}
	}
	
}
