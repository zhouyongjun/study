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
	 * 数据保存类
	 * @author zhouyongjun
	 *
	 */
public class OnlineServerManager {
	public static final String FILE_CONFIG = CONF_PATH.PATH_ONLINE_SERVER+"ssh_config.xml";
	public static final String FILE_SERVER_GROUP = CONF_PATH.PATH_ONLINE_SERVER+"server_group.xml";
	
	
	public static String APP_NAME = null;//上传路径
	public static String APP_JAR = null;//上传路径
	public static String UNIFIED_SERVER_PATH = null;//上传路径
	public static String DIR_UPLOAD_UPDATE = "./upload/";//上传路径 
	public static String DIR_UPLOAD_ZIP = "server\\";//上传路径
	public static String DIR_DOWNLOAD = "./download/";//下载路径
	public static String SSH_HOST_COND = "111.1.17.217";
	public static CmdInfo CMD_START_UP;//开服命令
	public static CmdInfo CMD_TAIL_LOG;//日志命令
	public static CmdInfo CMD_SHUT_DOWN;//关服命令
	public static CmdInfo CMD_TELNET_FREEZE;//维护保存命令
	public static CmdInfo CMD_TELNET_ONLINEPLAYERS;//在线人数命令
	public static String TELNET_END_INFO = "please input command:";
	public static String TABLE_SERVER = "";
	public static String[][] TABLE_ONLINE_SERVER_LIST;
	public static String SQL_INSERT_ONLINE_SERVER_LIST;
	
	public static int DEFAULT_GM_PORT = 10001;
	
	public static Map<ServerState,List<CmdInfo>> LOG_CONFIG_INFOS;//日志检测配置信息
	

	List<Server> serversList = new ArrayList<Server>(); //所有分服务器
	Server mainServer;
	List<ServerGroup> groupsList = new ArrayList<ServerGroup>();
	private static OnlineServerManager instance = new OnlineServerManager();
	private OnlineServerManager() {
		
	}
	public static OnlineServerManager getInstance() {
		return instance;
	}
	/**
	 * 数据初始化总方法
	 */
	public void init() {
		loadBasicConfig(FILE_CONFIG);
		loadServerList();
		loadServerGroupFromDB();
	}
	
	/**
	 * 执行服务器组
	 */
	public void loadServerGroupFromDB() {
		try {
			groupsList.clear();
			//默认包括所有的
			Map<Integer,ServerGroup> temp_groups = new HashMap<Integer, ServerGroup>();
			temp_groups.put(ServerGroup.ID_ALL, new ServerGroup(ServerGroup.ID_ALL,"所有服务器",new ArrayList<Server>(serversList)));
			temp_groups.put(ServerGroup.ID_IN, new ServerGroup(ServerGroup.ID_IN,"内网服务器"));
			temp_groups.put(ServerGroup.ID_ONLINE, new ServerGroup(ServerGroup.ID_ONLINE,"现网服务器"));
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
	 * 执行服务器组
	 */
	public void loadServerGroupFromXml() {
		try {
			groupsList.clear();
			//默认包括所有的
			groupsList.add(new ServerGroup(-1,"所有服务器",new ArrayList<Server>(serversList)));
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
	 * 加载服务器列表
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
			JOptionPane.showMessageDialog(null, "服务器列表加载错误，查询当前日志!");
			System.exit(0);
		}
	}
	
	/**
	 * 加载ssh_config xml信息
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
				JOptionPane.showMessageDialog(null, FILE_CONFIG+"文件内需要输入app_name[游戏名称]");
				System.exit(0);
			}
			APP_JAR  = SshUtil.getAttriValue(basicE, "app_jar");
			if (APP_JAR.length() == 0) {
				JOptionPane.showMessageDialog(null, FILE_CONFIG+"文件内需要输入app_jar[游戏jar]");
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
				JOptionPane.showMessageDialog(null, FILE_CONFIG+"上传路径和下载逻辑错误！");
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
	 * 执行一组服务器维护保存数据命令
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
	 * 执行一组服务器停服命令
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
	 * 执行上传资源命令，开启一个线程执行
	 * @param group
	 */
	public ExecuteResult execute_upload_resources(Server mainServer,List<Server> select_servers,UpLoadFileGroup upload_files,boolean isTelnetLoad) {
		new UploadThread(mainServer, select_servers, upload_files,isTelnetLoad).start();
		return null;
	}
	

	/**
	 * 执行一组服务器开服命令
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
	 * 删除指定组
	 * @param index
	 */
	public void deleteGroup(int index) {
		groupsList.remove(index);
	}
	/**
	 * 名字是否已经存在 
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
		 * 主服操作
		 * @param mainServer
		 * @param select_servers
		 * @param upload_files
		 * @return
		 */
	public ExecuteResult upLoadDirFiles(Server mainServer,List<Server> select_servers,UpLoadFileGroup upload_files) {
		try {
			//向主服上传文件
			String main_dir = mainServer.getSsh_remoteDir()+OnlineServerManager.UNIFIED_SERVER_PATH;
			ExecuteResult result = mainServer.getAgent_online_server().ssh_upload_dirs(main_dir,upload_files);
			if (!result.isSucc()) {
				return result;
			}
			//TODO 未测试
			List<String> main_dirs = getSrcScpDir(new File(DIR_UPLOAD_UPDATE),mainServer.getSsh_remoteDir()+OnlineServerManager.UNIFIED_SERVER_PATH);
			//执行主服向各个服务器分发文件
			for (Server server : select_servers) {
				ExecuteResult scp_result = mainServer.getAgent_online_server().ssh_scp_to_other_server(main_dirs,server.getSsh_remoteDir(),server);
				if (!scp_result.isSucc()) {
					return scp_result;
				}
			}
			return ExecuteResult.createSuccResult("分发成功");
		} catch (Exception e) {
			AppLog.error(mainServer, e);
			return ExecuteResult.createFailResult("分发报错");
		}
		
	}
	
	/**
	 * 主服操作
	 * @param mainServer
	 * @param select_servers
	 * @param upload_files
	 * @return
	 */
public ExecuteResult upLoadZip(Server mainServer,List<Server> select_servers,UpLoadFileGroup upload_files,boolean isTelnetLoad) {
	try {
		//生成本地目录结构
		ExecuteResult result = SshUtil.copyFiles(mainServer,SSHMain.mainFrame.getPanel_operate_online_server(),
				upload_files,OnlineServerManager.DIR_UPLOAD_ZIP);
		if (result.isFail()) return result;
		
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, "复制文件成功！");
		//检测是复制否成功
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, String.format("压缩资源 【%d个文件】 ：",upload_files.getSelect_files().size()));
		File file_local_upload_zip = new File(OnlineServerManager.DIR_UPLOAD_ZIP);
		if (!checkZipFile(file_local_upload_zip)) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(mainServer, "上传终止，本地复制文件失败，"+OnlineServerManager.DIR_UPLOAD_ZIP+"目录下空文件。。。");
			return ExecuteResult.createFailResult("复制文件失败，"+OnlineServerManager.DIR_UPLOAD_ZIP+"目录下空文件。。。");
		}
		int select_vaule=JOptionPane.showConfirmDialog(SSHMain.mainFrame, "请在核对下资源，是否继续执行？","核对资源",JOptionPane.YES_NO_OPTION);
		if (select_vaule != 0) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(mainServer, "您选择了终止上传资源！");
			return ExecuteResult.createFailResult("选择 终止上传...");
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
		//压缩是否成功
		if (null == null || !zipFile.getFile().exists()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(mainServer, "上传终止，本地"+zip_file_name+"压缩失败！");
			return ExecuteResult.createFailResult("zip失败");
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(mainServer, String.format("本地压缩成zip文件成功！【耗时：%f秒】", time/1000.0d));
		//向主服上传文件
		result = SshUtil.ssh_upload_zipfile_and_unzipfile(mainServer,mainServer.getSsh_remoteDir(),zipFile,SSHMain.mainFrame.getPanel_operate_online_server());
		if (!result.isSucc()) {
			return result;
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, "开始向其他服务器分发资源！");
		long main_time = System.currentTimeMillis();
		List<CmdInfo> cmds = upload_files.getCmd_infos();
		//执行主服向各个服务器分发文件
		for (Server server : select_servers) {
			if (upload_files.isAppJar()) {//包括游戏jar
				//检测服务器是否能连接上
				if (server.getNet().openTelnet(false)) {
					SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "检测到有游戏jar存在，服务器未关闭，上传资源跳过本服务器，本服务器上传失败！");
					server.getNet().getTelnet().disconect();
					continue;
				}
			}
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "更新资源开始！");
			time = System.currentTimeMillis();
			ExecuteResult scp_result = mainServer.getAgent_online_server().ssh_scp_to_other_server(main_dir,server.getSsh_remoteDir(),server);
			if (!scp_result.isSucc()) {
				SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "更新资源失败，报错！！");
				continue;
			}
			SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(server, "更新资源成功，耗时："+((System.currentTimeMillis() - time)/1000.0d)+"秒！！");
			if (isTelnetLoad) {
				if (cmds.size() > 0) {
					SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "检测绑定执行TELNET命令，命令数："+cmds.size()+",执行开始!");
					new TelnetCmdThread(server, cmds).start();					
				}else {
					SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "检测绑定执行TELNET命令，命令数："+cmds.size()+",无需执行!");
				}
			}else {
				SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "检测不绑定执行TELNET命令!");
			}
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(mainServer, "更新所有服务器结束，耗时："+((System.currentTimeMillis() - main_time)/1000.0d)+"秒！！");
		return ExecuteResult.createSuccResult("分发成功");
	} catch (Exception e) {
		AppLog.error(mainServer, e);
		return ExecuteResult.createFailResult("分发报错");
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
