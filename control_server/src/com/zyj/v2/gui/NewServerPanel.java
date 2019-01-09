package com.zyj.v2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.zyj.v1.common.Instances;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.common.OperateType;
import com.zyj.v1.ssh.common.ResultShowMsg;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.ServerGroup;
import com.zyj.v1.ssh.online_server.ServerType;
import com.zyj.v1.ssh.online_server.UpLoadFileGroup;
import com.zyj.v1.ssh.open_new_server.NewServerParam;
import com.zyj.v1.ssh.thread.OpenNewServerThread;
import com.zyj.v1.ssh.update.CheckHeaderCellRenderer;
import com.zyj.v1.ssh.update.CheckTableModle;
import com.zyj.v1.ssh.util.Const;
import com.zyj.v1.ssh.util.SshUtil;

import java.awt.Font;

public class NewServerPanel extends JPanel implements Instances {
	private static final long serialVersionUID = 1559017982876666976L;
	public static String[] TABLE_AUTO_INCREMENT_COULMN_NAMES ={"表名字", "每次递加值", "ID初始化值"};
	public static String[] TABLE_OLD_SERVER_DB_DUMP_TABLES ={"老区导出带数据的表"};
	public static String[] TABLE_COMMON_TABLE_FILESS ={"公共DB原始数据表文件"};
	public static String[] TABLE_NEW_SERVER_DELETE_FILESS ={"新区删除的文件列表"};
	public static String[] TABLE_NEW_SERVER_PARAMS_CONFIG_COUMLN = {"新区参数", "VALUE"};
	public static String[] TABLE_NEW_SERVER_FILES_COUMLN = {"新区文件参数", "修改参数"};
	public static String[] TABLE_INSERT_SERVER_LIST ={"新区入口插入表"};
	/*
	 * 内存数据
	 */
	Map<Server,List<ResultShowMsg>> server_result_msgs = new HashMap<Server,List<ResultShowMsg>>();//与服务器通信信息存储
	UpLoadFileGroup upload_files;
	OperateType  operateType;//操作类型
	private JTextField textField_new_server;
	private JTextField textField_newpath;
//	private JTable table_common_table_files;
	private JTable table_new_server_params;
	private JComboBox<ServerGroup> comboBox_old_server;
	JButton button_new_server;
	private JScrollPane scrollPane;
	private JTable table_server_list;
	/**
	 * Create the panel.
	 */
	public NewServerPanel() {
		this.setSize(593, 493);
//		update_pannel.setBounds(0, 24, 593, 493);
		JLabel lblNewLabel = new JLabel("\u65B0\u533AIP\u5730\u5740:");
		lblNewLabel.setBounds(20, 13, 76, 30);
		
		textField_new_server = new JTextField();
		textField_new_server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		textField_new_server.setBounds(94, 10, 276, 33);
		textField_new_server.setColumns(10);
		
		JLabel label = new JLabel("\u9009\u62E9\u670D\u52A1\u5668\u7EC4:");
		label.setBounds(10, 110, 86, 33);
		comboBox_old_server = new JComboBox<ServerGroup>();
		comboBox_old_server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerGroup sg = (ServerGroup)comboBox_old_server.getSelectedItem();
				selectGroup(sg.getMaxServer());
				refreshTables();
			}
		});
		comboBox_old_server.setBounds(94, 110, 276, 33);
		
		JLabel label_2 = new JLabel("\u65B0\u533A\u9879\u76EE\u8DEF\u5F84:");
		label_2.setBounds(10, 63, 95, 37);
		
		textField_newpath = new JTextField();
		textField_newpath.setBounds(94, 63, 276, 37);
		textField_newpath.setColumns(10);
		
		button_new_server = new JButton("\u65B0\u670D\u5F00\u542F");
		button_new_server.setFont(new Font("黑体", Font.BOLD, 14));
		button_new_server.setBounds(419, 260, 112, 56);
		button_new_server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openNewServer();
			}
		});
		
//		JScrollPane scrollPane_3 = new JScrollPane();
//		scrollPane_3.setBounds(319, 100, 264, 107);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(10, 153, 360, 259);
		
		table_new_server_params = new JTable();
		table_new_server_params.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
			},
			TABLE_NEW_SERVER_PARAMS_CONFIG_COUMLN
		));
		scrollPane_4.setViewportView(table_new_server_params);
		setLayout(null);
		add(button_new_server);
		add(scrollPane_4);
		add(lblNewLabel);
		add(textField_new_server);
		add(label);
		add(comboBox_old_server);
//		add(scrollPane_3);
		add(textField_newpath);
		add(label_2);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(376, 12, 207, 131);
		add(scrollPane);
		
		table_server_list = new JTable();
		scrollPane.setViewportView(table_server_list);
		init();
	
	}
	
	private void setComboBox_old_server(List<ServerGroup> list,int selectIndex) {
		ServerGroup[] objs = list.toArray(new ServerGroup[OnlineServerManager.getInstance().getGroupsList().size()]);
		comboBox_old_server.setModel(new DefaultComboBoxModel<ServerGroup>(objs));
		comboBox_old_server.setSelectedIndex(selectIndex);		
	}
	
	protected void clearResultDatas() {
		server_result_msgs.clear();
	}
	
	public void openNewServer() {
		
		if (OpenNewServerThread.isRun) {
			JOptionPane.showMessageDialog(this,"已经正在开启一个新区，请稍等！", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//IP
		String ip=textField_new_server.getText().trim();
		if (ip.length() == 0) {
			JOptionPane.showMessageDialog(this,"新区IP地址不能为空", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//路径
		String path = textField_newpath.getText().trim();
		if (path.length() == 0) {
			JOptionPane.showMessageDialog(this,"新区项目路径不能为空", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		for (Server server : OnlineServerManager.getInstance().getServersList()) {
			if (ip.equals(server.getSsh_host()) && path.equals(server.getSsh_remoteDir())) {
				JOptionPane.showMessageDialog(this,"地址["+ip+"]上存在已开的服务器路径["+path+"]", "开新区", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		//老区
		Server oldServer = ((ServerGroup)comboBox_old_server.getSelectedItem()).getMaxServer();
		if (oldServer == null) {
			JOptionPane.showMessageDialog(this,"必须选择一个老区", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (oldServer.getServerType() != ServerType.SUB_NET) {
			JOptionPane.showMessageDialog(this,"必须选择一个老区，主服不行", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//更新表数据
			//新区参数数据
		String[][] new_param_configs = nsMgr.getTable_new_server_params_config().getConfigs();
		if(!updateTableValues(table_new_server_params,new_param_configs).isSucc()) {
			return;
		}
		;
		int new_instance = Integer.parseInt(new_param_configs[0][1].split("0x")[1],16);//实例号
		int server_id = Integer.parseInt(new_param_configs[2][1]);//服务器编号
		int telnet_port = oldServer.getTelnet_port() - oldServer.getServer_instance() + new_instance;
		path = changePath(false,server_id,path);
		textField_newpath.setText(path);
		if (onsMgr.getServerByInstance(new_instance) != null) {
			JOptionPane.showMessageDialog(this,"实例号已存在", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (onsMgr.getServerByServerId(oldServer.getGroupId(),server_id) != null) {
			JOptionPane.showMessageDialog(this,"实例号已存在", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!oldServer.getSsh_host().equals(ip)) {
			int port = onsMgr.getMaxGmPort(ip);
			new_param_configs[3][1] = String.valueOf(port > 0 ? (port+1) : OnlineServerManager.DEFAULT_GM_PORT);//数据库编号
		}
		new_param_configs[5][1] = changeDBName(server_id,new_param_configs[5][1],path);//数据库编号
		if (onsMgr.getServerGroup(oldServer.getGroupId()).getServerByDbName(new_param_configs[5][1]) != null) {
			JOptionPane.showMessageDialog(this,"数据库["+new_param_configs[5][1]+"]已存在", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		new_param_configs[4][1] = telnet_port+"";//TELNET 
			//公共的DB文件
//		if(!updateTableValues(table_common_table_files,nsMgr.getTable_common_db_resource_files().getConfigs()).isSucc()) {
//			return;
//		}
//			//新区修改的文件
//		if(!updateTableValues(table_new_server_files,nsMgr.getTable_new_server_update_files().getConfigs()).isSucc()) {
//			return;
//		}
//		if(!updateTableValues(table_new_sever_delete_files,nsMgr.getTable_new_server_delete_files().getConfigs()).isSucc()) {
//			return;
//		}
			//老区数据库导出文件
//		if(!updateTableValues(table_old_server_db_tables,nsMgr.getTable_old_server_db_dump_tables().getConfigs()).isSucc()) {
//			return;
//		}
		//新区数据库表初始化
//		if(!updateTableValues(table_auto_increment,nsMgr.getTable_auto_increment().getConfigs()).isSucc()) {
//			return;
//		}
		refreshTables();
		ExecuteResult result = nsMgr.updateParams_map(ip, path);
		if(result.isFail()) {
			JOptionPane.showMessageDialog(this,"新区参数"+result.getMsg()+"不对", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//创建新的服务器对象
		Server newServer = createNewServer();
		newServer.setGroupName(oldServer.getGroupName());
		newServer.setGroupId(oldServer.getGroupId());
		newServer.setId(oldServer.getId() + 1);
		//检测参数
		result = checkNewServerParam(newServer);
		if (result.isFail()) {
			return;
		}
		//开启线程
//		new OpenNewServerThread(newServer, oldServer).start();
		List<String> table_of_server_list = new ArrayList<String>();
		for (int i=0;i<table_server_list.getRowCount();i++) {
			if((Boolean)table_server_list.getModel().getValueAt(i, 0)) {
				table_of_server_list.add((String)table_server_list.getModel().getValueAt(i, 1));
			}
		}
		result = newServer.getAgent_new_server().open_new_server(oldServer,table_of_server_list);
//		result  = newServer.getAgent_new_server().local_update_download_files(nsMgr.getTable_new_server_update_files().getPatternMaps(),OnlineServerManager.DIR_DOWNLOAD);
		if (result.isSucc()) {
			JOptionPane.showMessageDialog(this,"新区["+newServer.getName()+"]开服成功！！！");
			setComboBox_old_server(OnlineServerManager.getInstance().getGroupsList(),comboBox_old_server.getSelectedIndex());
		}else {
			JOptionPane.showMessageDialog(this,"新区["+newServer.getName()+"]开服失败,msg["+result.getMsg()+"]", "开新区", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String changeDBName(int server_id,String string, String path) {
		String[] temps = path.split("/");
		try {
			StringBuffer sb = new StringBuffer();
			String[] db_names = string.split("_");
			for (int i=0;i<db_names.length-1;i++) {
				sb.append(db_names[i]).append("_");
			}
			return sb.append(server_id).toString();
			 
		} catch (Exception e) {
			return temps[temps.length-1];
		}
	}
	private String changePath(boolean isforced, int server_id, String path) {
		String[] temps = path.split("/");
		String[] parent_dirs = temps[temps.length-1].split("_");
		try {
			int id = Integer.parseInt(parent_dirs[0]);
			StringBuffer sb = new StringBuffer();
			for (int i=0;i<temps.length-1;i++) {
				sb.append(temps[i]).append("/");
			}
			sb.append(server_id).append("_").append(parent_dirs[1]).append("/");
			return sb.toString(); 
		} catch (Exception e) {
			if (!isforced) return path;
			StringBuffer sb = new StringBuffer();
			for (int i=0;i<temps.length-1;i++) {
				sb.append(temps[i]).append("/");
			}
			sb.append(server_id).append("_").append(parent_dirs[1]).append("/");
			return sb.toString();
		}
	}
	private ExecuteResult checkNewServerParam(Server newServer) {
		
		for (Server server : OnlineServerManager.getInstance().getServersList()) {
			if (newServer.getName().equals(server.getName())) {
				JOptionPane.showMessageDialog(this,"同服务器["+server+"]同名!", "开新区", JOptionPane.ERROR_MESSAGE);
				return ExecuteResult.RESULT_FAIL;
			}
			if (newServer.getServer_instance() == server.getServer_instance()) {
				JOptionPane.showMessageDialog(this,"同服务器["+server+"] 实例号【0x"+Integer.toHexString(server.getServer_instance())+"】同名!", "开新区", JOptionPane.ERROR_MESSAGE);
				return ExecuteResult.RESULT_FAIL;
			}
			if (newServer.getSsh_host().equals(server.getSsh_host())
					&& newServer.getSql_port() == server.getSql_port()
					&& newServer.getSql_db() == server.getSql_db()) {
				JOptionPane.showMessageDialog(this,"同服务器["+server+"] 地址、数据库端口号和数据库名字全部相同!", "开新区", JOptionPane.ERROR_MESSAGE);
				return ExecuteResult.RESULT_FAIL;
			}
		}
		//TODO 检测是否能 telnet 这个新服务器
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 * 根据jtable更新数据的数据
	 * @param jtable
	 * @param array2Val
	 */
	private ExecuteResult updateTableValues(JTable jtable,String[][] array2Val) {
		int columnCount = jtable.getColumnCount();
		int rowCount = jtable.getRowCount();
		for (int i=0;i<rowCount;i++) {
			for (int j=0;j<columnCount;j++) {
				String value = (String)jtable.getValueAt(i, j);
				if (value == null || value.length() == 0) {
					JOptionPane.showMessageDialog(this,jtable.getValueAt(i, 0)+"存在没有数据！", "开新区", JOptionPane.ERROR_MESSAGE);
					return ExecuteResult.RESULT_FAIL;
				}
				array2Val[i][j]= value;
			}
		}
		return ExecuteResult.RESULT_SUCC;
	}

	private Server createNewServer() {
		Server server = new Server();
		server.setName(nsMgr.getNewServerParamValue(NewServerParam.APP_NAME));
		server.setSsh_host(nsMgr.getNewServerParamValue(NewServerParam.APP_IP));
		server.setSsh_port(Integer.parseInt(nsMgr.getNewServerParamValue(NewServerParam.SSH_PORT)));
		server.setSsh_username(nsMgr.getNewServerParamValue(NewServerParam.SSH_USERNAME));
		server.setSsh_password(SshUtil.getStringEOR(nsMgr.getNewServerParamValue(NewServerParam.SSH_PASSWORD),Const.EOR_KEY));
		server.setSsh_remoteDir(nsMgr.getNewServerParamValue(NewServerParam.APP_PATH));
		server.setTelnet_host(nsMgr.getNewServerParamValue(NewServerParam.APP_IP));
		server.setTelnet_port(Integer.parseInt(nsMgr.getNewServerParamValue(NewServerParam.TELNET_PORT)));
		server.setSql_db(nsMgr.getNewServerParamValue(NewServerParam.DB_NAME));
		server.setSql_username(nsMgr.getNewServerParamValue(NewServerParam.SQL_USERNAME));
		server.setSql_password(SshUtil.getStringEOR(nsMgr.getNewServerParamValue(NewServerParam.SQL_PASSWORD),Const.EOR_KEY));
		server.setSql_port(Integer.parseInt(nsMgr.getNewServerParamValue(NewServerParam.SQL_PORT)));
		server.setSql_cmd_dir(nsMgr.getNewServerParamValue(NewServerParam.SQL_CMD_DIR));
		server.setServerType(ServerType.SUB_NET);
		server.setServer_instance(Integer.parseInt(nsMgr.getNewServerParamValue(NewServerParam.APP_INSTANCE).split("0x")[1],16));
		server.setGm_port(Integer.parseInt(nsMgr.getNewServerParamValue(NewServerParam.GM_CONSOLE_PORT)));
		server.setServer_id(Integer.parseInt(nsMgr.getNewServerParamValue(NewServerParam.APP_ID)));
		server.getAgent_online_server().init();
		return server;
	}
	
	public void init() {
		List<ServerGroup> list = OnlineServerManager.getInstance().getGroupsList();
		setComboBox_old_server(list,list.size() - 1);
		initTable(table_server_list, TABLE_INSERT_SERVER_LIST, OnlineServerManager.TABLE_ONLINE_SERVER_LIST);
		Server server = onsMgr.getMaxServerOfMaxGroup();
		comboBox_old_server.setModel(new DefaultComboBoxModel<ServerGroup>(onsMgr.getGroupsList().toArray(new ServerGroup[onsMgr.getGroupsList().size()])));
		comboBox_old_server.setSelectedIndex(onsMgr.getGroupsList().size()-1);
		selectGroup(server);
		refreshTables();
		
	}
	
	 /**
     * 获得数据
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Vector getData(String[][] rows){
        Vector data = new Vector();
        for (String[] str_row : rows) {
        	Vector row =new Vector();
        	row.add(true);
        	for (String str : str_row) {
        		row.add(str);	
        	}
        	data.add(row);
        }
        return data;
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initTable(JTable table,String[] columns,String[][] rows){
        Vector headerNames=new Vector();
        headerNames.add("全部");
        headerNames.addAll(Arrays.asList(columns));
        Vector data=this.getData(rows);
        CheckTableModle tableModel=new CheckTableModle(data,headerNames);
        table.setModel(tableModel);
        table.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(table));
        TableColumn first_column = table.getColumnModel().getColumn(0);
		first_column.setPreferredWidth(60);
		first_column.setMaxWidth(60);
    }

	private void refreshTables() {
		table_new_server_params.setModel(new DefaultTableModel
				(nsMgr.getTable_new_server_params_config().getConfigs(),TABLE_NEW_SERVER_PARAMS_CONFIG_COUMLN));
//		column.setPreferredWidth(5);
	}
	private void selectGroup(Server server) {
		String[][] configs =nsMgr.getTable_new_server_params_config().getConfigs();
		int i = 0;
		int server_id = server.getServer_id() + 1;
		
		configs[i++][1] =  "0x"+Integer.toHexString(server.getServer_instance() +1).toUpperCase();//实例号
		configs[i++][1] =  "";//名字
		configs[i++][1] =   server_id +"";//服务器编号
		configs[i++][1] =  server.getGm_port() + 1+"";//Gm 端口号
		configs[i++][1] =  server.getTelnet_port() + 1+"";//telnet 端口号
		configs[i++][1] =  OnlineServerManager.APP_NAME +"_"+ server_id;//数据库名字
		configs[i++][1] =  server.getSql_port()+"";//数据库端口号
		configs[i++][1] =  server.getSql_username();//sql用户名
		configs[i++][1] =  SshUtil.getStringEOR(server.getSql_password(),Const.EOR_KEY);//sql密码(加密)
		configs[i++][1] =  server.getSql_cmd_dir();//sql命令目录
		configs[i++][1] =  server.getSsh_port()+"";//ssh 端口号
		configs[i++][1] =  server.getSsh_username();//ssh用户名
		configs[i++][1] =  SshUtil.getStringEOR(server.getSsh_password(),Const.EOR_KEY);//ssh密码(加密)
		
		textField_new_server.setText(server.getSsh_host());
		textField_newpath.setText(changePath(true,server_id, server.getSsh_remoteDir()));
	}
	public Map<Server, List<ResultShowMsg>> getServer_result_msgs() {
		return server_result_msgs;
	}

	public void setServer_result_msgs(
			Map<Server, List<ResultShowMsg>> server_result_msgs) {
		this.server_result_msgs = server_result_msgs;
	}

	public UpLoadFileGroup getUpload_files() {
		return upload_files;
	}

	public void setUpload_files(UpLoadFileGroup upload_files) {
		this.upload_files = upload_files;
	}

	public OperateType getOperateType() {
		return operateType;
	}

	public void setOperateType(OperateType operateType) {
		this.operateType = operateType;
	}

	public JTextField getTextField_new_server() {
		return textField_new_server;
	}

	public JTextField getTextField_newpath() {
		return textField_newpath;
	}


	public JComboBox<ServerGroup> getComboBox_old_server() {
		return comboBox_old_server;
	}

	public JButton getButton_new_server() {
		return button_new_server;
	}

//	public JTable getTable_auto_increment() {
//		return table_auto_increment;
//	}

//	public JTable getTable_old_server_db_tables() {
//		return table_old_server_db_tables;
//	}

//	public JTable getTable_common_table_files() {
//		return table_common_table_files;
//	}

	public JTable getTable_new_server_params() {
		return table_new_server_params;
	}

//	public JTable getTable_new_server_files() {
//		return table_new_server_files;
//	}
}
