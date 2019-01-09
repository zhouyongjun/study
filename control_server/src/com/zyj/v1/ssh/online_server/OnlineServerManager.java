package com.zyj.v1.ssh.online_server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.Element;

import com.zyj.v1.SSHMain;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.CONF_PATH;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.db.DBManager;
import com.zyj.v1.ssh.thread.TelnetCmdThread;
import com.zyj.v1.ssh.thread.UploadThread;
import com.zyj.v1.ssh.util.SshUtil;
import com.zyj.v1.ssh.util.xml.XmlDocument;
import com.zyj.v1.ssh.util.xml.XmlNode;
	/**
	 * ���ݱ�����
	 * @author zhouyongjun
	 *
	 */
public class OnlineServerManager {
	public static final String FILE_CONFIG = CONF_PATH.PATH_ONLINE_SERVER+"ssh_config.xml";
	public static final String FILE_SERVER_GROUP = CONF_PATH.PATH_ONLINE_SERVER+"server_group.xml";
	
	
	public static String APP_NAME = null;//�ϴ�·��
	public static String APP_JAR = null;//�ϴ�·��
	public static String UNIFIED_SERVER_PATH = null;//�ϴ�·��
	public static String DIR_UPLOAD_UPDATE = "./upload/";//�ϴ�·�� 
	public static String DIR_UPLOAD_ZIP = "server\\";//�ϴ�·��
	public static String DIR_DOWNLOAD = "./download/";//����·��
	public static String SSH_HOST_COND = "111.1.17.217";
	public static CmdInfo CMD_START_UP;//��������
	public static CmdInfo CMD_TAIL_LOG;//��־����
	public static CmdInfo CMD_SHUT_DOWN;//�ط�����
	public static CmdInfo CMD_TELNET_FREEZE;//ά����������
	public static CmdInfo CMD_TELNET_ONLINEPLAYERS;//������������
	public static String TELNET_END_INFO = "please input command:";
	public static String TABLE_SERVER = "";
	public static String[][] TABLE_ONLINE_SERVER_LIST;
	public static String SQL_INSERT_ONLINE_SERVER_LIST;
	
	public static int DEFAULT_GM_PORT = 10001;
	
	public static Map<ServerState,List<CmdInfo>> LOG_CONFIG_INFOS;//��־���������Ϣ
	

	List<Server> serversList = new ArrayList<Server>(); //���зַ�����
	Server mainServer;
	List<ServerGroup> groupsList = new ArrayList<ServerGroup>();
	private static OnlineServerManager instance = new OnlineServerManager();
	private OnlineServerManager() {
		
	}
	public static OnlineServerManager getInstance() {
		return instance;
	}
	/**
	 * ���ݳ�ʼ���ܷ���
	 */
	public void init() {
		loadBasicConfig(FILE_CONFIG);
		loadServerList();
		loadServerGroupFromDB();
	}
	
	/**
	 * ִ�з�������
	 */
	public void loadServerGroupFromDB() {
		try {
			groupsList.clear();
			//Ĭ�ϰ������е�
			Map<Integer,ServerGroup> temp_groups = new HashMap<Integer, ServerGroup>();
			temp_groups.put(ServerGroup.ID_ALL, new ServerGroup(ServerGroup.ID_ALL,"���з�����",new ArrayList<Server>(serversList)));
			temp_groups.put(ServerGroup.ID_IN, new ServerGroup(ServerGroup.ID_IN,"����������"));
			temp_groups.put(ServerGroup.ID_ONLINE, new ServerGroup(ServerGroup.ID_ONLINE,"����������"));
			for (Server server : serversList) {
				int groupId = server.getGroupId();
				ServerGroup group = null;
				if (temp_groups.containsKey(groupId)) {
					group = temp_groups.get(groupId);
				}
				if (group == null) {
					group = new  ServerGroup(groupId, server.getGroupName());
					temp_groups.put(groupId, group);
				}
				group.addOne(server);
				if (groupId < ServerGroup.MAX_INNER_ID) temp_groups.get(ServerGroup.ID_IN).addOne(server);
				else temp_groups.get(ServerGroup.ID_ONLINE).addOne(server);
			}
			groupsList.addAll(temp_groups.values());
			Collections.sort(groupsList,new Comparator<ServerGroup>() {
				@Override
				public int compare(ServerGroup arg0, ServerGroup arg1) {
					return arg0.getGroupId() - arg1.getGroupId();
				}
			});
		} catch (Exception e) {
			AppLog.error(e);
		}
	}
	
	/**
	 * ִ�з�������
	 */
	public void loadServerGroupFromXml() {
		try {
			groupsList.clear();
			//Ĭ�ϰ������е�
			groupsList.add(new ServerGroup(-1,"���з�����",new ArrayList<Server>(serversList)));
			Document doc = SshUtil.load(FILE_SERVER_GROUP);
			List<Element> els = SshUtil.getElements(doc.getRootElement(), "group");
			if (els == null) {
				return;
			}
			for (Element e : els) {
				ServerGroup group = new ServerGroup();
				group.load(e);
				groupsList.add(group);
			}
		} catch (Exception e) {
			AppLog.error(e);
		}
	}
	
	/**
	 * ���ط������б�
	 */
	public void loadServerList() {
		try {
			List<Map<String,Object>> datasList = DBManager.getInstance().getGameDao().getServerList();
			List<Server> temp_list = new ArrayList<Server>(); 
			for (Map<String,Object> map : datasList) {
				Server server = new Server();
				server.loadFromData(map);
				server.getAgent_online_server().init();
				if (server.getServerType() == ServerType.MAIN_NET) {
					mainServer = server;
					continue;
				}else if (server.getServerType() != ServerType.SUB_NET) {
					continue;
				}
				temp_list.add(server);				
			}
			this.serversList = temp_list;
		} catch (Exception e) {
			AppLog.error(e);
			JOptionPane.showMessageDialog(null, "�������б���ش��󣬲�ѯ��ǰ��־!");
			System.exit(0);
		}
	}
	
	/**
	 * ����ssh_config xml��Ϣ
	 * @param fileName
	 */
	public void loadBasicConfig(String fileName) {
		try {
			Document doc = SshUtil.load(fileName);
			Element basicE = SshUtil.getElement(doc.getRootElement(), "basic");
			APP_NAME = SshUtil.getAttriValue(basicE, "app_name");
			TABLE_SERVER = SshUtil.getAttriValue(basicE, "server_name");
			DEFAULT_GM_PORT = Integer.parseInt(SshUtil.getAttriValue(basicE, "default_gm_port"));
			SQL_INSERT_ONLINE_SERVER_LIST = SshUtil.getAttriValue(basicE, "online_serverlist_db_sql");
			String[] temps = SshUtil.getAttriValue(basicE, "online_serverlist_db_table").split(";");
			TABLE_ONLINE_SERVER_LIST  = new String[temps.length][1];
			for (int i=0;i<temps.length;i++) {
				TABLE_ONLINE_SERVER_LIST[i] = new String[]{temps[i]};
			}
			
			if (APP_NAME.length() == 0) {
				JOptionPane.showMessageDialog(null, FILE_CONFIG+"�ļ�����Ҫ����app_name[��Ϸ����]");
				System.exit(0);
			}
			APP_JAR  = SshUtil.getAttriValue(basicE, "app_jar");
			if (APP_JAR.length() == 0) {
				JOptionPane.showMessageDialog(null, FILE_CONFIG+"�ļ�����Ҫ����app_jar[��Ϸjar]");
				System.exit(0);
			}
			UNIFIED_SERVER_PATH = SshUtil.getAttriValue(basicE, "unified_server_path");
			SSH_HOST_COND = SshUtil.getAttriValue(basicE,"ssh_host_cond");
			try {
				String temp_upload = SshUtil.getAttriValue(basicE, "dir_upload");
				File file_upload = new File(temp_upload);
				if (!file_upload.exists()) {
					file_upload.mkdirs();
				}
				DIR_UPLOAD_UPDATE = file_upload.getCanonicalPath()+"\\" ;
				File test_file = new File(DIR_UPLOAD_UPDATE);
				if (!test_file.exists()) {
					test_file.mkdirs();
				}
				DIR_DOWNLOAD = SshUtil.getAttriValue(basicE, "dir_download");
				File file_dir_download = new File(DIR_DOWNLOAD);
				if (!file_dir_download.exists()) {
					file_dir_download.mkdirs();
				}
				DIR_DOWNLOAD = file_dir_download.getCanonicalPath()+"\\";
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, FILE_CONFIG+"�ϴ�·���������߼�����");
				System.exit(0);
			}
			Element bashE = SshUtil.getElement(doc.getRootElement(), "bash");
			CMD_START_UP = loadCmdInfo(SshUtil.getElement(bashE,"start_up"));
			CMD_SHUT_DOWN = loadCmdInfo(SshUtil.getElement(bashE,"shut_down"));
			CMD_TAIL_LOG = loadCmdInfo(SshUtil.getElement(bashE,"tail_log"));
			Element telnetE = SshUtil.getElement(doc.getRootElement(), "telnet");
			CMD_TELNET_FREEZE = loadCmdInfo(SshUtil.getElement(telnetE,"freeze"));
			CMD_TELNET_ONLINEPLAYERS = loadCmdInfo(SshUtil.getElement(telnetE,"onlineplayer"));
			
			Element logE = SshUtil.getElement(doc.getRootElement(), "log");
			LOG_CONFIG_INFOS = new HashMap<ServerState, List<CmdInfo>>();
			LOG_CONFIG_INFOS.put(ServerState.START_UP,loadLogStateCofigInfos(logE,"startup_control"));
			LOG_CONFIG_INFOS.put(ServerState.SHUT_DOWN,loadLogStateCofigInfos(logE,"shutdown_control"));
			LOG_CONFIG_INFOS.put(ServerState.FREEZE,loadLogStateCofigInfos(logE,"freeze_control"));
		} catch (Exception e) {
			AppLog.error(e);
		}
	}
	
	private List<CmdInfo> loadLogStateCofigInfos(Element basicE, String eName) {
		List<Element> els = SshUtil.getElements(basicE, eName);
		List<CmdInfo> temp_list = new ArrayList<CmdInfo>();
		for (Element e : els) {
			CmdInfo cmd = loadCmdInfo(e);
			temp_list.add(cmd);
		}
		return temp_list;
	}
	private CmdInfo loadCmdInfo(Element basicE) {
		String value = SshUtil.getAttriValue(basicE,"value");
		String chName = SshUtil.getAttriValue(basicE,"chName");
		String return_true_msg = SshUtil.getAttriValue(basicE, "return_true_msg");
		String return_error_msg = SshUtil.getAttriValue(basicE, "return_error_msg");
		if (return_true_msg != null && return_true_msg.contains("%s")) {
			return_true_msg = String.format(return_true_msg, APP_NAME);
		}
		if (return_error_msg != null && return_error_msg.contains("%s")) {
			return_error_msg = String.format(return_error_msg, APP_NAME);
		}
		return new CmdInfo(value,chName,return_true_msg,return_error_msg);
	}
	/**
	 * ִ��һ�������ά��������������
	 * @param group
	 */
	public ExecuteResult execute_group_freeze_cmd(List<Server> select_servers) {
		for (Server server : select_servers) {
			ExecuteResult result = server.getAgent_online_server().run_thread_cmd_freeze();
			AppLog.info(ExecuteResult.getMsg(server,"execute_group_stop_cmd :" +result));
		}
		return null;
	}
	
	/**
	 * ִ��һ�������ͣ������
	 * @param group
	 */
	public ExecuteResult execute_group_stop_cmd(List<Server> select_servers) {
		for (Server server : select_servers) {
			ExecuteResult result = server.getAgent_online_server().run_thread_cmd_close();
			AppLog.info(ExecuteResult.getMsg(server,"execute_group_stop_cmd :" +result));
		}
		return null;
	}
	
	/**
	 * ִ���ϴ���Դ�������һ���߳�ִ��
	 * @param group
	 */
	public ExecuteResult execute_upload_resources(Server mainServer,List<Server> select_servers,UpLoadFileGroup upload_files,boolean isTelnetLoad) {
		new UploadThread(mainServer, select_servers, upload_files,isTelnetLoad).start();
		return null;
	}
	

	/**
	 * ִ��һ���������������
	 * @param group
	 */
	public ExecuteResult execute_group_start_cmd(List<Server> select_servers) {
		for (Server server : select_servers) {
			ExecuteResult result = server.getAgent_online_server().run_thread_cmd_start();
			AppLog.info(ExecuteResult.getMsg(server, "execute_group_start_cmd :" +result));
		}
		return null;
	}
	
	public ExecuteResult execute_telnet_cmd(Server server, List<CmdInfo> list) {
		new TelnetCmdThread(mainServer, list).start();
		return null;
	}
	
	
	public List<Server> getServersList() {
		return serversList;
	}
	public void setServersList(List<Server> serversList) {
		this.serversList = serversList;
	}
	public List<ServerGroup> getGroupsList() {
		return groupsList;
	}
	public void setGroupsList(List<ServerGroup> groupsList) {
		this.groupsList = groupsList;
	}
	
	/**
	 * ɾ��ָ����
	 * @param index
	 */
	public void deleteGroup(int index) {
		groupsList.remove(index);
	}
	/**
	 * �����Ƿ��Ѿ����� 
	 * @param name
	 * @return
	 */
	public boolean isExistedGroupName(String name,ServerGroup g) {
		for (ServerGroup group : groupsList) {
			if (group.getName().equals(name) && g != group) {
				return true;
			}
		}
		return false;
	}
	
	public void addServerGroup(ServerGroup group) {
		groupsList.add(group);
	}
	public void unloadServerGroupXml() {
		try {
			XmlDocument doc = new XmlDocument(FILE_SERVER_GROUP, "groups");
			List<ServerGroup> temp_list = new ArrayList<ServerGroup>(groupsList);
			temp_list.remove(0);
			for (ServerGroup group : temp_list) {
				XmlNode node = new XmlNode();
				node.setElementName("group");
				node.addAttribute("name", group.getName());
				node.addAttribute("value", group.saveServerListToXml());
				doc.addNode(node);
			}
			SshUtil.writeToXml(doc);
		} catch (Exception e) {
			AppLog.error(e);
		}
	}
	
	public Server getServer(int id) {
		for (Server server : serversList) {
			if (server.getId() == id) {
				return server;
			}
			
		}
		return null;
	}
		/**
		 * ��������
		 * @param mainServer
		 * @param select_servers
		 * @param upload_files
		 * @return
		 */
	public ExecuteResult upLoadDirFiles(Server mainServer,List<Server> select_servers,UpLoadFileGroup upload_files) {
		try {
			//�������ϴ��ļ�
			String main_dir = mainServer.getSsh_remoteDir()+OnlineServerManager.UNIFIED_SERVER_PATH;
			ExecuteResult result = mainServer.getAgent_online_server().ssh_upload_dirs(main_dir,upload_files);
			if (!result.isSucc()) {
				return result;
			}
			//TODO δ����
			List<String> main_dirs = getSrcScpDir(new File(DIR_UPLOAD_UPDATE),mainServer.getSsh_remoteDir()+OnlineServerManager.UNIFIED_SERVER_PATH);
			//ִ������������������ַ��ļ�
			for (Server server : select_servers) {
				ExecuteResult scp_result = mainServer.getAgent_online_server().ssh_scp_to_other_server(main_dirs,server.getSsh_remoteDir(),server);
				if (!scp_result.isSucc()) {
					return scp_result;
				}
			}
			return ExecuteResult.createSuccResult("�ַ��ɹ�");
		} catch (Exception e) {
			AppLog.error(mainServer, e);
			return ExecuteResult.createFailResult("�ַ�����");
		}
		
	}
	
	/**
	 * ��������
	 * @param mainServer
	 * @param select_servers
	 * @param upload_files
	 * @return
	 */
public ExecuteResult upLoadZip(Server mainServer,List<Server> select_servers,UpLoadFileGroup upload_files,boolean isTelnetLoad) {
	try {
		//���ɱ���Ŀ¼�ṹ
		ExecuteResult result = SshUtil.copyFiles(mainServer,SSHMain.mainFrame.getPanel_operate_online_server(),
				upload_files,OnlineServerManager.DIR_UPLOAD_ZIP);
		if (result.isFail()) return result;
		
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, "�����ļ��ɹ���");
		//����Ǹ��Ʒ�ɹ�
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, String.format("ѹ����Դ ��%d���ļ��� ��",upload_files.getSelect_files().size()));
		File file_local_upload_zip = new File(OnlineServerManager.DIR_UPLOAD_ZIP);
		if (!checkZipFile(file_local_upload_zip)) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(mainServer, "�ϴ���ֹ�����ظ����ļ�ʧ�ܣ�"+OnlineServerManager.DIR_UPLOAD_ZIP+"Ŀ¼�¿��ļ�������");
			return ExecuteResult.createFailResult("�����ļ�ʧ�ܣ�"+OnlineServerManager.DIR_UPLOAD_ZIP+"Ŀ¼�¿��ļ�������");
		}
		int select_vaule=JOptionPane.showConfirmDialog(SSHMain.mainFrame, "���ں˶�����Դ���Ƿ����ִ�У�","�˶���Դ",JOptionPane.YES_NO_OPTION);
		if (select_vaule != 0) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(mainServer, "��ѡ������ֹ�ϴ���Դ��");
			return ExecuteResult.createFailResult("ѡ�� ��ֹ�ϴ�...");
		}
		List<String> main_dir = getSrcScpDir(file_local_upload_zip,mainServer.getSsh_remoteDir()+OnlineServerManager.UNIFIED_SERVER_PATH+"/");
		String zip_file_name = OnlineServerManager.UNIFIED_SERVER_PATH+".zip";
		result = SshUtil.zip(mainServer,zip_file_name,OnlineServerManager.DIR_UPLOAD_ZIP);
		UpLoadFile zipFile = null;
		long time = 0;
		if (result.isSucc()) {
			zipFile = (UpLoadFile)result.getObjs()[0];
			time  = (Long)result.getObjs()[1];
		}
		//ѹ���Ƿ�ɹ�
		if (null == null || !zipFile.getFile().exists()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(mainServer, "�ϴ���ֹ������"+zip_file_name+"ѹ��ʧ�ܣ�");
			return ExecuteResult.createFailResult("zipʧ��");
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(mainServer, String.format("����ѹ����zip�ļ��ɹ�������ʱ��%f�롿", time/1000.0d));
		//�������ϴ��ļ�
		result = SshUtil.ssh_upload_zipfile_and_unzipfile(mainServer,mainServer.getSsh_remoteDir(),zipFile,SSHMain.mainFrame.getPanel_operate_online_server());
		if (!result.isSucc()) {
			return result;
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, "��ʼ�������������ַ���Դ��");
		long main_time = System.currentTimeMillis();
		List<CmdInfo> cmds = upload_files.getCmd_infos();
		//ִ������������������ַ��ļ�
		for (Server server : select_servers) {
			if (upload_files.isAppJar()) {//������Ϸjar
				//���������Ƿ���������
				if (server.getNet().openTelnet(false)) {
					SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "��⵽����Ϸjar���ڣ�������δ�رգ��ϴ���Դ�����������������������ϴ�ʧ�ܣ�");
					server.getNet().getTelnet().disconect();
					continue;
				}
			}
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "������Դ��ʼ��");
			time = System.currentTimeMillis();
			ExecuteResult scp_result = mainServer.getAgent_online_server().ssh_scp_to_other_server(main_dir,server.getSsh_remoteDir(),server);
			if (!scp_result.isSucc()) {
				SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "������Դʧ�ܣ�������");
				continue;
			}
			SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(server, "������Դ�ɹ�����ʱ��"+((System.currentTimeMillis() - time)/1000.0d)+"�룡��");
			if (isTelnetLoad) {
				if (cmds.size() > 0) {
					SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "����ִ��TELNET�����������"+cmds.size()+",ִ�п�ʼ!");
					new TelnetCmdThread(server, cmds).start();					
				}else {
					SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "����ִ��TELNET�����������"+cmds.size()+",����ִ��!");
				}
			}else {
				SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "��ⲻ��ִ��TELNET����!");
			}
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(mainServer, "�������з�������������ʱ��"+((System.currentTimeMillis() - main_time)/1000.0d)+"�룡��");
		return ExecuteResult.createSuccResult("�ַ��ɹ�");
	} catch (Exception e) {
		AppLog.error(mainServer, e);
		return ExecuteResult.createFailResult("�ַ�����");
	}
	
}

	private List<String> getSrcScpDir(File file, String main_remote_dir) {
		List<String> list = new ArrayList<String>();
		for (File f : file.listFiles()) {
			list.add(main_remote_dir+f.getName());
		}
		return list;
	}
	
	private boolean checkZipFile(File file) {
		boolean isTrue = false;
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				boolean subTrue = checkZipFile(f);
				isTrue = isTrue ? isTrue : subTrue;
			}
		}else {
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, "\t"+file.getPath());
			isTrue  = true;
		}
		return isTrue;
	}
	public Server getMainServer() {
		return mainServer;
	}
	public void setMainServer(Server mainServer) {
		this.mainServer = mainServer;
	}
	
	public void addNewServer(Server server) {
		serversList.add(server);
		getServerGroup(server.getGroupId()).addOne(server);
		getServerGroup(ServerGroup.ID_ALL).addOne(server);
		if (server.getGroupId() <= ServerGroup.MAX_INNER_ID) getServerGroup(ServerGroup.ID_IN).addOne(server);
		else getServerGroup(ServerGroup.ID_ONLINE).addOne(server);
	}
	public Server getMaxServerOfMaxGroup() {
		return groupsList.get(groupsList.size() -1).getMaxServer();
	}
	public ServerGroup getMaxGroup() {
		return groupsList.get(groupsList.size() -1);
	}
	public Server getServerByInstance(int new_instance) {
		for (Server server : serversList) {
			if (server.getServer_instance() == new_instance) return server;
		}
		return null;
	}
	public Server getServerByServerId(int groupId,int server_id) {
		for (Server server : getServerGroup(groupId).getServers()) {
			if (server.getServer_id() == server_id) return server;
		}
		return null;
	}
	public ServerGroup getServerGroup(int groupId) {
		for (ServerGroup sg : groupsList) {
			if (sg.getGroupId() == groupId) return sg;
		}
		return null;
	}
	public int getMaxGmPort(String ip) {
		int port = 0;
		for (Server server : serversList) {
			if (server.getSsh_host().equals(ip) && port < server.getGm_port()) port = server.getGm_port();  
		}
		return port;
	}
	
}
