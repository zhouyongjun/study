package com.zyj.v1.ssh.db;

//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.SSHManager;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.ServerType;
	/**
	 * 数据库连接执行类
	 * @author zhouyongjun
	 *
	 */
public class ServerListDAO extends NamedParameterJdbcDaoSupport{
	 private SimpleJdbcInsert simpleJdbcInsert_role;
	private ServerListDAO() {
	}

	//取得服务器列表
	public List<Map<String,Object>> getServerList() {
		String sql = String.format("select * from %s where %s >=%d order by id ", OnlineServerManager.TABLE_SERVER,DaoData.SERVER_SERVER_TYPE,ServerType.MAIN_NET.ordinal());
		AppLog.info(sql);
		return getJdbcTemplate().queryForList(sql);
	}

	public Map<String,Object> selectUser(String name, String pwd) {
		try {
			String sql = String.format("select * from %s where %s='%s' and %s='%s' ", DaoData.TABLE_GM_USER,DaoData.GM_USER_NAME,name,DaoData.GM_USER_PASSWROD,pwd);
			AppLog.info(sql);
			return getJdbcTemplate().queryForMap(sql);
		} catch (Exception e) {
			AppLog.error(e);
			return null;
		} 
	}
	
	public static void main(String[] args) {
		DBManager.getInstance().init();
		SSHManager.geteInstance().init();
		Server server = OnlineServerManager.getInstance().getMaxServerOfMaxGroup().clone();
		server.setId(server.getId() + 1);
		server.setName("test_"+server.getServer_id());
		DBManager.getInstance().getServerListDao().insertServer(server);
	}
	public boolean insertServer(Server server) {
		try {
			if (this.simpleJdbcInsert_role == null) {
				simpleJdbcInsert_role = new SimpleJdbcInsert(getDataSource())
				.withTableName(OnlineServerManager.TABLE_SERVER).usingGeneratedKeyColumns(DaoData.SERVER_SID);
			}
			String sql = new StringBuffer().append("insert into ").append(OnlineServerManager.TABLE_SERVER).append("(").append(DaoData.SERVER_ID).append(") value(")
					.append(server.getId()).append(");").toString();
			getJdbcTemplate().update(sql);
			Map<String, Object> map = new HashMap<String, Object>();
			server.saveToData(map);
			update(OnlineServerManager.TABLE_SERVER, map,DaoData.SERVER_ID);
			return true;
		} catch (Exception e) {
			AppLog.error(server,e);
			return false;
		}
	}
	
	/**
	 * 更新 update table set parmas where=?;
	 * 
	 * @param table
	 * @param params 
	 * @param where 数据必须在 params参数MAP中，必须要包括where条件的key和value,否则无法读取导数据
	 */
	public void update(String table, Map<String, Object> params, String... wheres) {
		StringBuffer sqlbuff = new StringBuffer(256);
		sqlbuff.append("update ").append(table).append(" set ");
		Set<String> conds = new HashSet<String>(Arrays.asList(wheres));
		for (String key : params.keySet()) {
			if (!conds.contains(key)) sqlbuff.append(key).append("=:").append(key).append(",");
		}
		sqlbuff.deleteCharAt(sqlbuff.length() - 1);
		if (conds.size() > 0) {
			String andStr = " and ";
			sqlbuff.append(" where ");
			for (String where : conds) {
				sqlbuff.append(where).append("=:").append(where).append(andStr);
			}	
			sqlbuff.delete(sqlbuff.lastIndexOf(andStr), sqlbuff.length());
		}
		getNamedParameterJdbcTemplate().update(sqlbuff.toString(), params);
	}
}
