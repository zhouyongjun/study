package  com.zhou.core.gm.cs.core.server.console;

import org.apache.mina.core.session.IoSession;

	/**
	 * GM�ͻ��˶���
	 * @author zhouyongjun
	 *
	 */
public class GmUser {
	private String gmUser;
	private IoSession session;
	public String getGmUser() {
		return gmUser;
	}
	public void setGmUser(String gmUser) {
		this.gmUser = gmUser;
	}
	public IoSession getSession() {
		return session;
	}
	public void setSession(IoSession session) {
		this.session = session;
	}
	public static GmUser createEntry(IoSession session) {
		GmUser entry = new GmUser();
		entry.session = session;
		return entry;
	}
	
	public String toString() {
		return gmUser + session.toString()+"["+hashCode()+"]";
	}
}
