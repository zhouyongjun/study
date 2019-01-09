package com.zyj.v1.ssh.online_server;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.util.SshUtil;
	/**
	 * 执行服务器组
	 * @author zhouyongjun
	 *
	 */
public class ServerGroup {
	public static final int ID_ALL = -3;
	public static final int ID_IN = -2;
	public static final int ID_ONLINE = -1;
	public static final int MAX_INNER_ID = 9;
	String name="";
	int groupId;//<10内网组，>=10外网组
	List<Server> servers = new ArrayList<Server>();
	public ServerGroup() {
		
	}
	
	public ServerGroup(int groupId,String name) {
		this.groupId = groupId;
		this.name = name;
	}
	
	public ServerGroup(int groupId,String name,List<Server> servers) {
		this.groupId = groupId;
		this.name = name;
		this.servers = servers;
	}
	
	/**
	 * 添加单个服务器
	 * @param server
	 */
	public void addOne(Server server) {
		if (servers.contains(server)) return;
		servers.add(server);
	}
	/**
	 * 添加一组服务器
	 * @param list
	 */
	public void addList(List<Server> list) {
		servers.addAll(list);
	}
	/**
	 * 删除单个服务器
	 * @param server
	 */
	public void removeOne(Server server) {
		servers.remove(server);
	}
	/**
	 * 删除一组服务器
	 * @param list
	 */
	public void  removeList(List<Server> list) {
		servers.removeAll(list);
	}
	
	public List<Server> getServers() {
		return servers;
	}
	
	public void setServers(List<Server> servers) {
		this.servers = servers;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String saveServerListToXml() {
		StringBuffer sb = new StringBuffer();
		for (Server server : servers) {
			sb.append(server.getId()).append(",");
		}
		return sb.toString();
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void load(Element e) {
		servers.clear();
		name = SshUtil.getAttriValue(e, "name");
		String[] vals = SshUtil.getAttriValue(e, "value").split(",");
		for (String val : vals) {
			if (val.length() == 0) {
				continue;
			}
			int id = Integer.parseInt(val);
			Server server = OnlineServerManager.getInstance().getServer(id);
			if (server == null) {
				continue;
			}
			servers.add(server);
		}
	}

	public Server getMaxServer() {
		if (servers == null || servers.size() == 0) return null;
		return servers.get(servers.size()-1);
	}

	public String toString() {
		Server server = getMaxServer();
		return String.format("%d:%s[sin:%d,sid:%d]", groupId,name,server == null ? -1 : server.getServer_instance(),server == null ? 0 :server.getServer_id());
	}

	public Object getServerByDbName(String db_name) {
		for (Server server : servers) {
			if (server.getSql_db().equals(db_name)) return server;
		}
		return null;
	}
	
}
