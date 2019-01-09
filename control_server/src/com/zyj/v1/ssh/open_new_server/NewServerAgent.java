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
	 *  *�����ļ���
	 *	1������֮ǰ�ķ�����
	 *  	��ɾ��ԭ����db/dump
	 *  	�ڴ����µ�db/dump
	 *      �۵��������ݵ����ݿ�ṹ
	 *      �ܵ���һЩ����ı����� 
	 *      ��ͬ���ļ�Ŀ¼������
	 *  2)��������
	 *      �����Ŀ¼�������򴴽���Ҫ����ĿĿ¼
	 *      ���޸ı�Ҫ���ļ�
	 *      �����������ݿ�
	 *      �������Ҫ�����ݿⲻ�����򴴽�
	 *      �ݵ������ݿ�ṹ
	 *      �޵�����Ҫ�ı�����
	 *      ���޸ı��������ֶ�
	 *      
	 *  3)�����������������־
	 *
	 * @return
	 */
	public ExecuteResult open_new_server(Server oldServer,List<String> tables_of_insert_serverlist) {
		//��������
		ExecuteResult result = ssh_old_server(oldServer);
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "���ִ�����ֹ��������!!!");
			return result;
		}
		//��������
		result = ssh_new_server();
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "���ִ�����ֹ��������!!!");
			return result;
		}
		//���������뵽GM���ݿ���
		result = local_save();
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "���ִ�����ֹ��������!!!");
//			return result;
		}
		//�����б��
		inset_into_serverlist(tables_of_insert_serverlist);
		return result;
	}
	public ExecuteResult local_save() {
		//���浽���ݿ�
		if(!DBManager.getInstance().getGameDao().insertServer(server)) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "�����������ݿ�ʧ�ܣ�");
			return ExecuteResult.createFailResult("�ȱ������ݿ��������ʧ��!");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "�����������ݿ�ɹ���");
		//�����ڴ浽�б���
		OnlineServerManager.getInstance().addNewServer(server);
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "�����������ݿ�ɹ���");
		//�����ļ�
		nsMgr.update();
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "���±���XML�ɹ���");
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().init();
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "ˢ��UI����!");
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
		//����
		ExecuteResult result = connServer(server);
		if (result.isFail()) {
			return result;
		}	
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "����SSH OK��");
		//�����Ŀ¼�������򴴽���Ҫ����ĿĿ¼
		result = __execute_mkdir_dir(server, server.getSsh_remoteDir());
		if (result.isFail()) {
			return result;
		}
		//ɾ���ļ�
		result = __execute_rm_dir_arrays(server, server.getSsh_remoteDir(),nsMgr.getTable_new_server_delete_files().getArrayFirstElementList());
		if (result.isFail()) {
			return result;
		}
		//�޸��ļ�
		result = _update_files(server);
		if (result.isFail()) {
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "�޸�ָ�����ļ��ɹ���");
		//�޸����ݿ�
		result = _update_db(server);
		if (result.isFail()) {
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "�������ɹ�!!!");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_rm_dir_arrays(Server server,String dir, List<String> arrayFirstElementList) {
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server,"ɾ��һ���ļ���ʼ");
		for (String file : nsMgr.getTable_new_server_delete_files().getArrayFirstElementList()) {
			if ( __execute_rm_dir(server, dir+file).isFail()) {
				return ExecuteResult.RESULT_FAIL;
			}
			if (file.endsWith("/")) {
				__execute_mkdir_dir(server, dir+file);
			}
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "ɾ��һ���ļ��ɹ���");
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 * �����������ݿ�
	 * @param ssh
	 * @return
	 */
	private ExecuteResult _update_db(Server server) {
		JdbcDao jdbcDao = new JdbcDao();
//		 *      �����������ݿ�
		if (__execute_sql_create_new_db(server,jdbcDao).isFail()) {
			return ExecuteResult.RESULT_FAIL;
		}
		//����SQL�ļ�
		__execute_sql_mysql(server,jdbcDao);
		//�ر�����
		jdbcDao.close();
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "�ر�JDBC���ӽ���");
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "���ݿ�����ɹ���");
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 * ���ݿ�ṹ�����ݵĵ���
	 * @param server
	 * @param jdbcDao
	 * @return
	 */
	private ExecuteResult __execute_sql_mysql(Server server,JdbcDao jdbcDao) {
		ClientSSH ssh = server.getNet().getSsh_cmd();
		String dir_db_dump = server.getSsh_remoteDir()+"db/dump/";
		ExecuteResult result = ssh.execCmd(new CmdInfo(String.format("%smysql -P%s -u%s -p%s %s < %s%s",server.getSql_cmd_dir(),
				server.getSql_port(),server.getSql_username(),server.getSql_password(),server.getSql_db(),dir_db_dump,DB_FILE_NAME),"�������ݿ�sql"));
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "����"+dir_db_dump+DB_FILE_NAME+".sql�ļ�����!");
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "����"+dir_db_dump+DB_FILE_NAME+"�ļ�����");
		for (String sqlFileName : nsMgr.getTable_old_server_db_dump_tables().getArrayFirstElementList()) {
			ssh.closeConnection();
			ssh.openConnection();
			result = ssh.execCmd(new CmdInfo(String.format("%smysql -P%s -u%s -p%s %s < %s%s",server.getSql_cmd_dir(),
					server.getSql_port(),server.getSql_username(),server.getSql_password(),server.getSql_db(),dir_db_dump,sqlFileName+".sql"),"�������ݿ�sql"));
			if (result.isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "����"+dir_db_dump+sqlFileName+".sql�ļ�����!");
				return result;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "����"+dir_db_dump+sqlFileName+".sql�ļ�����");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "����������SQL�ļ��ɹ���");
		for (String sqlFileName : nsMgr.getTable_common_db_resource_files().getArrayFirstElementList()) {
			ssh.closeConnection();
			ssh.openConnection();
			result = ssh.execCmd(new CmdInfo(String.format("%smysql -P%s -u%s -p%s %s < %s%s",server.getSql_cmd_dir(),
					server.getSql_port(),server.getSql_username(),server.getSql_password(),server.getSql_db(),server.getSsh_remoteDir(),sqlFileName),"�������ݿ�sql"));
			if (result.isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "����"+server.getSsh_remoteDir()+sqlFileName+".sql�ļ�����!");
				return result;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "����"+server.getSsh_remoteDir()+sqlFileName+".sql�ļ�����");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "���빫����SQL�ļ��ɹ���");
		for (String[] values : nsMgr.getTable_auto_increment().getConfigs()) {
			String sql = String.format("alter table %s.%s auto_increment = %s;",server.getSql_db(),values[0],values[2]);
			if (!jdbcDao.executeCmd(sql)) {
				SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "���ñ�["+values[0]+"]��������ʼֵ["+values[2]+"]����!");
				return ExecuteResult.createFailResult("���ñ���������ʼֵ����!");
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "���ñ�["+values[0]+"]��������ʼֵ["+values[2]+"]����");
		}
		if (SSHMain.mainFrame !=null)if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, "���������ֶγ�ʼֵ���óɹ���");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_sql_create_new_db(Server server,JdbcDao jdbcDao) {
		if(!jdbcDao.connect(server.getSsh_host(),server.getSql_port(),"mysql", server.getSql_username(), server.getSql_password())) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "JDBC����ʧ��!");
			return ExecuteResult.createFailResult("�����������ݿ�ʧ��");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "JDBC���������ݿ�[mysql]");
		//ɾ�����ݿ�
		jdbcDao.deleteDatabase(server.getSql_db());
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "ɾ��ԭ�����ܴ��ڵ����ݿ�["+server.getSql_db()+"]");
		//�����µ����ݿ�
		if(!jdbcDao.createDatabase(server.getSql_db())) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "�������ݿ�["+server.getSql_db()+"]ʧ��!");
			return ExecuteResult.RESULT_FAIL;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "�������ݿ�["+server.getSql_db()+"]�ɹ�");
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 *	�޸�������Ҫ�޸ĵ��ļ�
	 * @param ssh
	 * @return
	 */
	private ExecuteResult _update_files(Server server) {
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "�޸��ļ���ʼ");
		List<String> update_remotedir_fileslist = nsMgr.getTable_new_server_update_files().getArrayFirstElementList(server.getSsh_remoteDir());
		//1)�����ļ�
		ExecuteResult result  = sshMgr.downloadFromServer(server,update_remotedir_fileslist);
		if (result.isFail()) {
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "   �����ļ�ʧ�ܣ�");
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "   �����ļ� OK��");
		//�����޸��ļ�
		result = local_update_download_files(nsMgr.getTable_new_server_update_files().getPatternMaps(),OnlineServerManager.DIR_DOWNLOAD);
		if (result.isFail()) {
			return result;
		}
		//�ų��ظ����ļ�
		Map<String,String> uploadMaps = new HashMap<String,String>();
		for (String fileName : update_remotedir_fileslist) {
			uploadMaps.put(OnlineServerManager.DIR_DOWNLOAD+fileName.substring(fileName.lastIndexOf("/")+1), fileName.substring(0,fileName.lastIndexOf("/")));
		}
		//�ϴ��ļ�
		result = sshMgr.uploadToServer(server,uploadMaps);
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "�ļ��޸����");
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
			return ExecuteResult.createFailResult("�޸�"+fileName+"ʧ�ܣ�");
		}
		
	}
	
	public ExecuteResult local_update_download_files(Map<String,String[][]> patternMaps,String download_dir) {
	//�޸�
	ExecuteResult result = ExecuteResult.RESULT_FAIL;
	for (Entry<String,String[][]> entry : patternMaps.entrySet()) {
		String local_file = download_dir+entry.getKey().substring(entry.getKey().lastIndexOf("/")+1);
		result = _local_update_file(local_file,entry.getValue());
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "   �޸ı����ļ�["+local_file+"]ʧ�ܣ�");
			return result;
		}
//		SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "   �޸ı����ļ�["+local_file+"]OK��");
	}
	return ExecuteResult.RESULT_SUCC;
	
}
	
//	public ExecuteResult local_update_download_files(String[][] configs,String download_dir) {
//		//�޸�
//		ExecuteResult result = ExecuteResult.RESULT_FAIL;
//		for (String[] vals : configs) {
//			String local_file = download_dir+vals[0].substring(vals[0].lastIndexOf("/")+1);
//			result = _local_update_file(local_file,vals[1]);
//			if (result.isFail()) {
//				SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "   �޸ı����ļ�["+local_file+"]ʧ�ܣ�");
//				return result;
//			}
//			SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "   �޸ı����ļ�["+local_file+"]OK��");
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
//			return ExecuteResult.createFailResult("�޸�"+fileName+"ʧ�ܣ�");
//		}
//		
//	}
	public ExecuteResult connServer(Server conn_server) {
		if(!conn_server.getNet().openSsh_cmd_connect()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(conn_server,"SSH����ʧ�ܡ�����");
			return ExecuteResult.RESULT_FAIL;
		}
		return ExecuteResult.RESULT_SUCC;
	}
	
	public static String DB_FILE_NAME = "db.sql";
	public ExecuteResult ssh_old_server(Server oldServer) {
		if (SSHMain.mainFrame !=null) SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "����["+oldServer+","+oldServer.getSsh_remoteDir()+"]������ʼ��");
		if (connServer(oldServer).isFail()) {
			return ExecuteResult.RESULT_FAIL;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "���ӳɹ���");
		ClientSSH ssh = oldServer.getNet().getSsh_cmd();
		//��ɾ��ԭ����db/dump/
		//�ڴ����µ�db/dump/
		String dir_db_dump = oldServer.getSsh_remoteDir()+"db/dump/";
		ExecuteResult result = __execute_rm_dir(oldServer,dir_db_dump);
		if (result.isFail()) {
			return result;
		}
		result = __execute_mkdir_dir(oldServer,dir_db_dump);
		if (result.isFail()) {
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(oldServer, "ɾ���ʹ���db/dump�ɹ�!");
		//�۵��������ݵ����ݿ�ṹ
		//�ܵ��������ݵı�
		result = __execute_sql_mysqldump(oldServer,dir_db_dump);
		if (result.isFail()) {
			return result;
		}
		//��������������Ŀ¼
		result = connServer(server);
		if (result.isFail()) {
			return result;
		}
		if (SSHMain.mainFrame !=null) SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "  	������"+server+"������OK��");
		result = __execute_mkdir_dir(server, server.getSsh_remoteDir());
		if (result.isFail()) {
			return result;
		}
		//��ͬ����Ŀ��������
		result = __execute_scp_from_old_to_new(oldServer);
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(oldServer, "��������!");
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(server, oldServer+"����������");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_scp_from_old_to_new(Server oldServer) {
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "scp�������ļ���ʼ��");
		for (String dir : nsMgr.getTable_old_server_scp_dirs_config().getArrayFirstElementList()) {
			String final_from_dir = oldServer.getSsh_remoteDir()+dir;
			ExecuteResult result = SshUtil.ssh_pass_to_other_server(server.getSsh_remoteDir(), server,oldServer,final_from_dir);
			if (result.isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(oldServer, "scp����["+final_from_dir+"] to ����["+server.getSsh_remoteDir()+"]ʧ�ܣ�");
				return result;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "scp����["+final_from_dir+"] to ����["+server.getSsh_remoteDir()+"]������");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer, "scp�������ļ�������");
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(oldServer, "scp��Դ�������ɹ�!");
		return null;
	}
	
	private ExecuteResult __execute_sql_mysqldump(Server oldServer, String dir_db_dump) {
		ClientSSH ssh = oldServer.getNet().getSsh_cmd();
		CmdInfo cmd= new CmdInfo(String.format("%smysqldump -P%d -u%s -p%s -d %s > %s%s",oldServer.getSql_cmd_dir(),oldServer.getSql_port(),oldServer.getSql_username(),oldServer.getSql_password()
				,oldServer.getSql_db(),dir_db_dump,DB_FILE_NAME), "��������ԭ���������ݿ�Ŀ¼");
		ExecuteResult result = ssh.execCmd(cmd);
		if (result.isFail()) {
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(oldServer,cmd+"ִ��ʧ�ܣ�");
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer,cmd+"ִ�� ������");
		//���������ݵı�
		for (String table : nsMgr.getTable_old_server_db_dump_tables().getArrayFirstElementList()) {
			ssh.closeConnection();
			ssh.openConnection();
			cmd= new CmdInfo(String.format("%smysqldump -P%d -u%s -p%s %s %s > %s%s",oldServer.getSql_cmd_dir(),oldServer.getSql_port(),oldServer.getSql_username(),oldServer.getSql_password()
					,oldServer.getSql_db(),table,dir_db_dump,table+".sql"), "��������ԭ���������ݿ�Ŀ¼");
			result = ssh.execCmd(cmd);
			if (result.isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(oldServer,cmd+"ִ��ʧ�ܣ�");
				return result;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(oldServer,cmd+"ִ�� ������");
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addSuccResultMsg(oldServer, "������Ҫ�����ݿ����ݳɹ���");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_rm_dir(Server server,String dir) {
		CmdInfo cmd = new CmdInfo(String.format("rm -r %s",dir), String.format("ɾ��%sĿ¼���ļ�", dir));
		ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd);
		if (result.isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, cmd+"ִ��ʧ�ܣ�");
			return result;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, cmd+"ִ�� OK��");
		return ExecuteResult.RESULT_SUCC;
	}
	
	private ExecuteResult __execute_mkdir_dir(Server server,String dir) {
		CmdInfo cmd = new CmdInfo(String.format("mkdir -p %s",dir), String.format("����%sĿ¼", dir));
		if (server.getNet().getSsh_cmd().execCmd(cmd).isFail()) {
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server,cmd+"ִ��ʧ�ܣ�");
			return ExecuteResult.RESULT_FAIL;
		}
		if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server,cmd+"ִ�� OK��");
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
//		System.out.println(gbEncoding("����"));
		nsMgr.updateParams_map("10.80.1.233", "E:/TEST/");
		NewServerAgent agent = new NewServerAgent(new Server());
		agent.local_update_download_files(nsMgr.getTable_new_server_update_files().getPatternMaps(),OnlineServerManager.DIR_DOWNLOAD);
	}
}
