package com.zyj.v1.ssh.open_new_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zyj.v1.SSHMain;
import com.zyj.v1.common.Instances;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.SSHManager;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.ClientSSH;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.common.ServerAgent;
import com.zyj.v1.ssh.db.DBManager;
import com.zyj.v1.ssh.db.JdbcDao;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.util.SshUtil;

public class NewServerAgent extends ServerAgent implements Instances{
	public NewServerAgent(Server server) {
		super(server);
	}
	/**
	 *  *复制文件：
	 *	1）链接之前的服务器
	 *  	①删除原来的db/dump
	 *  	②创建新的db/dump
	 *      ③导出空数据的数据库结构
	 *      ④导出一些不便的表数据 
	 *      ⑤同步文件目录到新区
	 *  2)链接新区
	 *      ①如果目录不存在则创建需要的项目目录
	 *      ②修改必要的文件
	 *      ③链接上数据库
	 *      ④如果需要的数据库不存在则创建
	 *      ⑤导入数据库结构
	 *      ⑥导入需要的表数据
	 *      ⑦修改表自增长字段
	 *      
	 *  3)启动服务器，检测日志
	 *
	 * @return
	 */
	public ExecuteResult open_new_server(Server oldServer,List<String> tables_of_insert_serverlist) {
		//操作老区
		ExecuteResult result = ssh_old_server(oldServer);
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "出现错误，终止开区操作!!!");
			return result;
		}
		//操作新区
		result = ssh_new_server();
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "出现错误，终止开区操作!!!");
			return result;
		}
		//将新区插入到GM数据库中
		result = local_save();
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "出现错误，终止开区操作!!!");
//			return result;
		}
		//更新列表服
		inset_into_serverlist(tables_of_insert_serverlist);
		return result;
	}
	public ExecuteResult local_save() {
		//保存到数据库
		if(!DBManager.getInstance().getGameDao().insertServer(server)) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "插入内网数据库失败！");
			return ExecuteResult.createFailResult("先本地数据库插入新区失败!");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "插入内网数据库成功！");
		//加入内存到列表中
		OnlineServerManager.getInstance().addNewServer(server);
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "插入内网数据库成功！");
		//更新文件
		nsMgr.update();
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "更新本地XML成功！");
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().init();
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "刷新UI界面!");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private void inset_into_serverlist(List<String> tables) {
		if (tables == null || tables.size() == 0) return;
		for (String table : tables) {
			try {
				String get_max_sort_sql = String.format("select max(game_index) from %s",table);
				int sort = dbMgr.getServerListDao().getJdbcTemplate().queryForObject(get_max_sort_sql, Integer.class) + 1;
				String insert_sql = String.format(OnlineServerManager.SQL_INSERT_ONLINE_SERVER_LIST, table,server.getServer_instance(),server.getName(),sort);
				System.out.println(insert_sql);
				dbMgr.getServerListDao().getJdbcTemplate().update(insert_sql);
			} catch (Exception e) {
				AppLog.error(e);
			}
		}
		
	}
	private ExecuteResult ssh_new_server() {
		//链接
		ExecuteResult result = connServer(server);
		if (result.isFail()) {
			return result;
		}	
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "链接SSH OK！");
		//①如果目录不存在则创建需要的项目目录
		result = __execute_mkdir_dir(server, server.getSsh_remoteDir());
		if (result.isFail()) {
			return result;
		}
		//删除文件
		result = __execute_rm_dir_arrays(server, server.getSsh_remoteDir(),nsMgr.getTable_new_server_delete_files().getArrayFirstElementList());
		if (result.isFail()) {
			return result;
		}
		//修改文件
		result = _update_files(server);
		if (result.isFail()) {
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "修改指定的文件成功！");
		//修改数据库
		result = _update_db(server);
		if (result.isFail()) {
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "开新区成功!!!");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_rm_dir_arrays(Server server,String dir, List<String> arrayFirstElementList) {
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server,"删除一组文件开始");
		for (String file : nsMgr.getTable_new_server_delete_files().getArrayFirstElementList()) {
			if ( __execute_rm_dir(server, dir+file).isFail()) {
				return ExecuteResult.RESULT_FAIL;
			}
			if (file.endsWith("/")) {
				__execute_mkdir_dir(server, dir+file);
			}
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "删除一组文件成功！");
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 * 更新新区数据库
	 * @param ssh
	 * @return
	 */
	private ExecuteResult _update_db(Server server) {
		JdbcDao jdbcDao = new JdbcDao();
//		 *      ③链接上数据库
		if (__execute_sql_create_new_db(server,jdbcDao).isFail()) {
			return ExecuteResult.RESULT_FAIL;
		}
		//导入SQL文件
		__execute_sql_mysql(server,jdbcDao);
		//关闭链接
		jdbcDao.close();
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "关闭JDBC链接结束");
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "数据库操作成功！");
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 * 数据库结构和数据的导入
	 * @param server
	 * @param jdbcDao
	 * @return
	 */
	private ExecuteResult __execute_sql_mysql(Server server,JdbcDao jdbcDao) {
		ClientSSH ssh = server.getNet().getSsh_cmd();
		String dir_db_dump = server.getSsh_remoteDir()+"db/dump/";
		ExecuteResult result = ssh.execCmd(new CmdInfo(String.format("%smysql -P%s -u%s -p%s %s < %s%s",server.getSql_cmd_dir(),
				server.getSql_port(),server.getSql_username(),server.getSql_password(),server.getSql_db(),dir_db_dump,DB_FILE_NAME),"导入数据库sql"));
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "导入"+dir_db_dump+DB_FILE_NAME+".sql文件报错!");
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "导入"+dir_db_dump+DB_FILE_NAME+"文件结束");
		for (String sqlFileName : nsMgr.getTable_old_server_db_dump_tables().getArrayFirstElementList()) {
			ssh.closeConnection();
			ssh.openConnection();
			result = ssh.execCmd(new CmdInfo(String.format("%smysql -P%s -u%s -p%s %s < %s%s",server.getSql_cmd_dir(),
					server.getSql_port(),server.getSql_username(),server.getSql_password(),server.getSql_db(),dir_db_dump,sqlFileName+".sql"),"导入数据库sql"));
			if (result.isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "导入"+dir_db_dump+sqlFileName+".sql文件报错!");
				return result;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "导入"+dir_db_dump+sqlFileName+".sql文件结束");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "导入老区的SQL文件成功！");
		for (String sqlFileName : nsMgr.getTable_common_db_resource_files().getArrayFirstElementList()) {
			ssh.closeConnection();
			ssh.openConnection();
			result = ssh.execCmd(new CmdInfo(String.format("%smysql -P%s -u%s -p%s %s < %s%s",server.getSql_cmd_dir(),
					server.getSql_port(),server.getSql_username(),server.getSql_password(),server.getSql_db(),server.getSsh_remoteDir(),sqlFileName),"导入数据库sql"));
			if (result.isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "导入"+server.getSsh_remoteDir()+sqlFileName+".sql文件报错!");
				return result;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "导入"+server.getSsh_remoteDir()+sqlFileName+".sql文件结束");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "导入公共的SQL文件成功！");
		for (String[] values : nsMgr.getTable_auto_increment().getConfigs()) {
			String sql = String.format("alter table %s.%s auto_increment = %s;",server.getSql_db(),values[0],values[2]);
			if (!jdbcDao.executeCmd(sql)) {
				SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "设置表["+values[0]+"]自增长初始值["+values[2]+"]报错!");
				return ExecuteResult.createFailResult("设置表自增长初始值报错!");
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "设置表["+values[0]+"]自增长初始值["+values[2]+"]结束");
		}
		if (SSHMain.mainFrame !=null)if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "表自增长字段初始值设置成功！");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_sql_create_new_db(Server server,JdbcDao jdbcDao) {
		if(!jdbcDao.connect(server.getSsh_host(),server.getSql_port(),"mysql", server.getSql_username(), server.getSql_password())) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "JDBC链接失败!");
			return ExecuteResult.createFailResult("新区链接数据库失败");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "JDBC链接上数据库[mysql]");
		//删除数据库
		jdbcDao.deleteDatabase(server.getSql_db());
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "删除原来可能存在的数据库["+server.getSql_db()+"]");
		//创建新的数据库
		if(!jdbcDao.createDatabase(server.getSql_db())) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "创建数据库["+server.getSql_db()+"]失败!");
			return ExecuteResult.RESULT_FAIL;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "创建数据库["+server.getSql_db()+"]成功");
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 *	修改新区需要修改的文件
	 * @param ssh
	 * @return
	 */
	private ExecuteResult _update_files(Server server) {
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "修改文件开始");
		List<String> update_remotedir_fileslist = nsMgr.getTable_new_server_update_files().getArrayFirstElementList(server.getSsh_remoteDir());
		//1)下载文件
		ExecuteResult result  = sshMgr.downloadFromServer(server,update_remotedir_fileslist);
		if (result.isFail()) {
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "   下载文件失败！");
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "   下载文件 OK！");
		//本地修改文件
		result = local_update_download_files(nsMgr.getTable_new_server_update_files().getPatternMaps(),OnlineServerManager.DIR_DOWNLOAD);
		if (result.isFail()) {
			return result;
		}
		//排除重复的文件
		Map<String,String> uploadMaps = new HashMap<String,String>();
		for (String fileName : update_remotedir_fileslist) {
			uploadMaps.put(OnlineServerManager.DIR_DOWNLOAD+fileName.substring(fileName.lastIndexOf("/")+1), fileName.substring(0,fileName.lastIndexOf("/")));
		}
		//上传文件
		result = sshMgr.uploadToServer(server,uploadMaps);
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "文件修改完毕");
		return ExecuteResult.RESULT_SUCC;
	}
	
	
	
	private ExecuteResult _local_update_file(String fileName, String[][] patternValues) {
//		String finalUpdMsg = updVal.replaceAll("'","\"");
		File file = new File(fileName);
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 0) {
					for (String[] values : patternValues) {
						String pattern = values[0].replace("'","\"");
						String replace = values[1].replace("'","\"");
						replace = nsMgr.getReplaceServerParamValue(replace);
						line = line.replaceAll(pattern, replace);
					}
				}
				sb.append(line).append("\n");
			}
			br.close();
			PrintWriter pw = new PrintWriter(fileName);
			pw.println(sb.toString());
			pw.flush();
			pw.close();
			return ExecuteResult.RESULT_SUCC;
		} catch (Exception e) {
			AppLog.error(server,"_local_update_file["+fileName+"] is error..",e);
			return ExecuteResult.createFailResult("修改"+fileName+"失败！");
		}
		
	}
	
	public ExecuteResult local_update_download_files(Map<String,String[][]> patternMaps,String download_dir) {
	//修改
	ExecuteResult result = ExecuteResult.RESULT_FAIL;
	for (Entry<String,String[][]> entry : patternMaps.entrySet()) {
		String local_file = download_dir+entry.getKey().substring(entry.getKey().lastIndexOf("/")+1);
		result = _local_update_file(local_file,entry.getValue());
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "   修改本地文件["+local_file+"]失败！");
			return result;
		}
//		SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "   修改本地文件["+local_file+"]OK！");
	}
	return ExecuteResult.RESULT_SUCC;
	
}
	
//	public ExecuteResult local_update_download_files(String[][] configs,String download_dir) {
//		//修改
//		ExecuteResult result = ExecuteResult.RESULT_FAIL;
//		for (String[] vals : configs) {
//			String local_file = download_dir+vals[0].substring(vals[0].lastIndexOf("/")+1);
//			result = _local_update_file(local_file,vals[1]);
//			if (result.isFail()) {
//				SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "   修改本地文件["+local_file+"]失败！");
//				return result;
//			}
//			SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "   修改本地文件["+local_file+"]OK！");
//		}
//		return ExecuteResult.RESULT_SUCC;
//		
//	}
	 public static String gbEncoding(final String gbString) {        
	        char[] utfBytes = gbString.toCharArray();              
	        String unicodeBytes = "";               
	        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) { 
	            String hexB = Integer.toHexString(utfBytes[byteIndex]);                      
	            if (hexB.length() <= 2) {                          
	                hexB = "00" + hexB;                    
	                }                      
	            unicodeBytes = unicodeBytes + "\\u" + hexB;                  
	            }                  
	        return unicodeBytes;
	 }
	 
//	private ExecuteResult _local_update_file(String fileName, String updVal) {
//		String finalUpdMsg = updVal.replaceAll("'","\"");
//		String[] values = finalUpdMsg.split("\\%");
//		File file = new File(fileName);
//		BufferedReader br = null;
//		StringBuffer sb = new StringBuffer();
//		try {
//			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//			String line = null;
//			while ((line = br.readLine()) != null) {
//				if (!line.contains(values[0])) {
//					sb.append(line).append("\n");
//					continue;
//				}
//				for (int i=0;i<values.length;i++) {
//					NewServerParam param = NewServerParam.getVal(values[i]);
//					if (param != null) {
//						values[i] = nsMgr.getNewServerParamValue(param);
//						if (param == NewServerParam.DB_NAME) {
//							values[i] = String.format("%s?useUnicode=true&characterEncoding=utf8", values[i]);
//						}else if (param == NewServerParam.APP_INSTANCE) {
//							values[0] = "<RemoteServices "+ values[0];
//							values[values.length - 1] = values[values.length - 1] + ">";
//						}else if (param == NewServerParam.APP_NAME) {
//							values[i] = gbEncoding(values[i]);
//						}
//						else if (param == NewServerParam.SQL_PASSWORD) {
//							values[i] = SSHUtil.getStringEOR(values[i], Const.EOR_KEY);
//						}
//					}
//				}
//				for (String val : values) {
//					sb.append(val);
//				}
//				sb.append("\n");
//			}
//			br.close();
//			PrintWriter pw = new PrintWriter(fileName);
//			pw.println(sb.toString());
//			pw.flush();
//			pw.close();
//			return ExecuteResult.RESULT_SUCC;
//		} catch (Exception e) {
//			AppLog.error(server,"_local_update_file["+fileName+"] is error..",e);
//			return ExecuteResult.createFailResult("修改"+fileName+"失败！");
//		}
//		
//	}
	public ExecuteResult connServer(Server conn_server) {
		if(!conn_server.getNet().openSsh_cmd_connect()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(conn_server,"SSH链接失败。。。");
			return ExecuteResult.RESULT_FAIL;
		}
		return ExecuteResult.RESULT_SUCC;
	}
	
	public static String DB_FILE_NAME = "db.sql";
	public ExecuteResult ssh_old_server(Server oldServer) {
		if (SSHMain.mainFrame !=null) SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "老区["+oldServer+","+oldServer.getSsh_remoteDir()+"]操作开始！");
		if (connServer(oldServer).isFail()) {
			return ExecuteResult.RESULT_FAIL;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "链接成功！");
		ClientSSH ssh = oldServer.getNet().getSsh_cmd();
		//①删除原来的db/dump/
		//②创建新的db/dump/
		String dir_db_dump = oldServer.getSsh_remoteDir()+"db/dump/";
		ExecuteResult result = __execute_rm_dir(oldServer,dir_db_dump);
		if (result.isFail()) {
			return result;
		}
		result = __execute_mkdir_dir(oldServer,dir_db_dump);
		if (result.isFail()) {
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(oldServer, "删除和创建db/dump成功!");
		//③导出空数据的数据库结构
		//④导出带数据的表
		result = __execute_sql_mysqldump(oldServer,dir_db_dump);
		if (result.isFail()) {
			return result;
		}
		//链接新区，创建目录
		result = connServer(server);
		if (result.isFail()) {
			return result;
		}
		if (SSHMain.mainFrame !=null) SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "  	新区【"+server+"】链接OK！");
		result = __execute_mkdir_dir(server, server.getSsh_remoteDir());
		if (result.isFail()) {
			return result;
		}
		//⑤同步到目标新区上
		result = __execute_scp_from_old_to_new(oldServer);
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(oldServer, "操作结束!");
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, oldServer+"操作结束！");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_scp_from_old_to_new(Server oldServer) {
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "scp到新区文件开始！");
		for (String dir : nsMgr.getTable_old_server_scp_dirs_config().getArrayFirstElementList()) {
			String final_from_dir = oldServer.getSsh_remoteDir()+dir;
			ExecuteResult result = SshUtil.ssh_pass_to_other_server(server.getSsh_remoteDir(), server,oldServer,final_from_dir);
			if (result.isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(oldServer, "scp老区["+final_from_dir+"] to 新区["+server.getSsh_remoteDir()+"]失败！");
				return result;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "scp老区["+final_from_dir+"] to 新区["+server.getSsh_remoteDir()+"]结束！");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "scp到新区文件结束！");
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(oldServer, "scp资源到新区成功!");
		return null;
	}
	
	private ExecuteResult __execute_sql_mysqldump(Server oldServer, String dir_db_dump) {
		ClientSSH ssh = oldServer.getNet().getSsh_cmd();
		CmdInfo cmd= new CmdInfo(String.format("%smysqldump -P%d -u%s -p%s -d %s > %s%s",oldServer.getSql_cmd_dir(),oldServer.getSql_port(),oldServer.getSql_username(),oldServer.getSql_password()
				,oldServer.getSql_db(),dir_db_dump,DB_FILE_NAME), "导出老区原来导出数据库目录");
		ExecuteResult result = ssh.execCmd(cmd);
		if (result.isFail()) {
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(oldServer,cmd+"执行失败！");
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer,cmd+"执行 结束！");
		//导出带数据的表
		for (String table : nsMgr.getTable_old_server_db_dump_tables().getArrayFirstElementList()) {
			ssh.closeConnection();
			ssh.openConnection();
			cmd= new CmdInfo(String.format("%smysqldump -P%d -u%s -p%s %s %s > %s%s",oldServer.getSql_cmd_dir(),oldServer.getSql_port(),oldServer.getSql_username(),oldServer.getSql_password()
					,oldServer.getSql_db(),table,dir_db_dump,table+".sql"), "导出老区原来导出数据库目录");
			result = ssh.execCmd(cmd);
			if (result.isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(oldServer,cmd+"执行失败！");
				return result;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer,cmd+"执行 结束！");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(oldServer, "导出需要的数据库数据成功！");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_rm_dir(Server server,String dir) {
		CmdInfo cmd = new CmdInfo(String.format("rm -r %s",dir), String.format("删除%s目录或文件", dir));
		ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd);
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, cmd+"执行失败！");
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, cmd+"执行 OK！");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_mkdir_dir(Server server,String dir) {
		CmdInfo cmd = new CmdInfo(String.format("mkdir -p %s",dir), String.format("创建%s目录", dir));
		if (server.getNet().getSsh_cmd().execCmd(cmd).isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server,cmd+"执行失败！");
			return ExecuteResult.RESULT_FAIL;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server,cmd+"执行 OK！");
		return ExecuteResult.RESULT_SUCC;
	}
	public static void main(String[] args) {
		DBManager.getInstance().init();
		SSHManager.geteInstance().init();
//		OpenNewServerManager nsMgr = OpenNewServerManager.getInstance();
//		nsMgr.updateParams_map("192.168.8.40", "/data/admin/hbsg/4_server/");
//		Server server = OnlineServerManager.getInstance().getServer(105);
//		server.getNet().openSsh_cmd_connect();
//		ClientSSH ssh = server.getNet().getSsh_cmd();
//		ssh.execCmd(new CmdInfo("/opt/mysql/bin/mysqldump -P3306 -uroot -pmhxx@2011.new -d hbsg_2 > /data/admin/hbsg/2_server/db/dump/db.sql",""));
//		ExecuteResult result = server.getAgent_new_server().local_update_download_files(nsMgr.getTable_new_server_files_config().getConfigs(),OnlineServerManager.DIR_DOWNLOAD);
//		System.out.println(gbEncoding("测试"));
		nsMgr.updateParams_map("10.80.1.233", "E:/TEST/");
		NewServerAgent agent = new NewServerAgent(new Server());
		agent.local_update_download_files(nsMgr.getTable_new_server_update_files().getPatternMaps(),OnlineServerManager.DIR_DOWNLOAD);
	}
}
