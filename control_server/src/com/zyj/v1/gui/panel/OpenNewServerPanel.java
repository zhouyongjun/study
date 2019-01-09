package com.zyj.v1.gui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;

import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.common.OperateType;
import com.zyj.v1.ssh.common.ResultShowMsg;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.ServerType;
import com.zyj.v1.ssh.online_server.UpLoadFileGroup;
import com.zyj.v1.ssh.open_new_server.NewServerManager;
import com.zyj.v1.ssh.open_new_server.NewServerParam;
import com.zyj.v1.ssh.thread.OpenNewServerThread;
import com.zyj.v1.ssh.util.Const;
import com.zyj.v1.ssh.util.SshUtil;

public class OpenNewServerPanel extends JPanel {
	private static final long serialVersionUID = 1559017982876666976L;
	public static String[] TABLE_AUTO_INCREMENT_COULMN_NAMES ={"表名字", "每次递加值", "ID初始化值"};
	public static String[] TABLE_OLD_SERVER_DB_DUMP_TABLES ={"老区导出带数据的表"};
	public static String[] TABLE_COMMON_TABLE_FILESS ={"公共DB原始数据表文件"};
	public static String[] TABLE_NEW_SERVER_DELETE_FILESS ={"新区删除的文件列表"};
	public static String[] TABLE_NEW_SERVER_PARAMS_CONFIG_COUMLN = {"新区参数", "VALUE"};
	public static String[] TABLE_NEW_SERVER_FILES_COUMLN = {"新区文件参数", "修改参数"};
	JTextPane logtextPane;
	JTextArea logtextArea;
	/*
	 * 内存数据
	 */
	NewServerManager onsMgr = NewServerManager.getInstance();
	Map<Server,List<ResultShowMsg>> server_result_msgs = new HashMap<Server,List<ResultShowMsg>>();//与服务器通信信息存储
	UpLoadFileGroup upload_files;
	OperateType  operateType;//操作类型
	private JTextField textField_new_server;
	private JTextField textField_newpath;
	private JTable table_auto_increment;
	private JTable table_old_server_db_tables;
	private JTable table_common_table_files;
	private JTable table_new_server_params;
	private JTable table_new_server_files;
	JComboBox comboBox_old_server;
	JButton button_new_server;
	private JTable table_new_sever_delete_files;
	/**
	 * Create the panel.
	 */
	public OpenNewServerPanel() {
		this.setSize(1214, 761);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setAutoscrolls(true);
		scrollPane_1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JLabel lblNewLabel = new JLabel("\u65B0\u533AIP\u5730\u5740:");
		
		textField_new_server = new JTextField();
		textField_new_server.setColumns(10);
		
		JLabel label = new JLabel("\u9009\u62E9\u8001\u533A:");
		
		comboBox_old_server = new JComboBox(OnlineServerManager.getInstance().getServersList().toArray());
		
		JLabel label_2 = new JLabel("\u65B0\u533A\u9879\u76EE\u8DEF\u5F84:");
		
		textField_newpath = new JTextField();
		textField_newpath.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		
		button_new_server = new JButton("\u65B0\u670D\u5F00\u542F");
		button_new_server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openNewServer();
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JScrollPane scrollPane_3 = new JScrollPane();
		
		JScrollPane scrollPane_4 = new JScrollPane();
		
		JScrollPane scrollPane_5 = new JScrollPane();
		
		JScrollPane scrollPane_6 = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(OpenNewServerPanel.this);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(30)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(label_2)
										.addComponent(label)
										.addComponent(lblNewLabel))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(textField_newpath, 350, 444, Short.MAX_VALUE)
										.addComponent(textField_new_server, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
										.addComponent(comboBox_old_server, 0, 444, Short.MAX_VALUE)))
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane_4, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane_6, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
										.addComponent(scrollPane_3, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane_5, GroupLayout.PREFERRED_SIZE, 403, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(147)
							.addComponent(button_new_server, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
							.addGap(219))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 570, GroupLayout.PREFERRED_SIZE)
					.addGap(60))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(25)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(textField_new_server, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_2)
								.addComponent(textField_newpath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(13)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(label)
								.addComponent(comboBox_old_server, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18, 18, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane_3, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(scrollPane_6, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
								.addComponent(scrollPane_4, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane_5, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
								.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(button_new_server, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)))
					.addGap(80))
		);
		
		table_new_sever_delete_files = new JTable();
		table_new_sever_delete_files.setModel(new DefaultTableModel(
			new Object[][] {
				{null},
			},
			TABLE_NEW_SERVER_DELETE_FILESS
		));
		scrollPane_6.setViewportView(table_new_sever_delete_files);
		
		table_new_server_files = new JTable();
		table_new_server_files.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
			},
			TABLE_NEW_SERVER_FILES_COUMLN
		));
		scrollPane_5.setViewportView(table_new_server_files);
		
		table_new_server_params = new JTable();
		table_new_server_params.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
			},
			TABLE_NEW_SERVER_PARAMS_CONFIG_COUMLN
		));
		scrollPane_4.setViewportView(table_new_server_params);
		
		table_common_table_files = new JTable();
		scrollPane_3.setViewportView(table_common_table_files);
		table_common_table_files.setModel(new DefaultTableModel(
			new Object[][] {
				{null},
			},
			TABLE_COMMON_TABLE_FILESS
		));
		
		table_old_server_db_tables = new JTable();
		table_old_server_db_tables.setModel(new DefaultTableModel(
			new Object[][] {
				{null},
			},
			TABLE_OLD_SERVER_DB_DUMP_TABLES
		));
		scrollPane_2.setViewportView(table_old_server_db_tables);
		
		table_auto_increment = new JTable();
		scrollPane.setViewportView(table_auto_increment);
		table_auto_increment.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
				{null, null, null},
			},
			TABLE_AUTO_INCREMENT_COULMN_NAMES
		));
		logtextArea = new JTextArea();
		logtextArea.setText("\u4E0E\u670D\u52A1\u5668\u901A\u4FE1\u4FE1\u606F\u63D0\u793A");
		logtextArea.setLineWrap(true);
		scrollPane_1.setColumnHeaderView(logtextArea);
		
		logtextPane = new JTextPane(){
			public boolean getScrollableTracksViewportWidth(){
			  return false;
			  }
			 public void setSize(Dimension d){
			  if(d.width<getParent().getSize().width){
			   d.width=getParent().getSize().width;
			  }
			  d.width+=100;
			  super.setSize(d);
			 }

};

		scrollPane_1.setViewportView(logtextPane);
		this.setLayout(gl_contentPane);
		init();
	
	}
	protected void clearResultDatas() {
		server_result_msgs.clear();
		logtextPane.setText("");
	}
	
	public void openNewServer() {
		
		if (OpenNewServerThread.isRun) {
			JOptionPane.showMessageDialog(this,"已经正在开启一个新区，请稍等！", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		clearResultDatas();
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
		Server oldServer = (Server)comboBox_old_server.getSelectedItem();
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
		if(!updateTableValues(table_new_server_params,onsMgr.getTable_new_server_params_config().getConfigs()).isSucc()) {
			return;
		}
			//公共的DB文件
		if(!updateTableValues(table_common_table_files,onsMgr.getTable_common_db_resource_files().getConfigs()).isSucc()) {
			return;
		}
//			//新区修改的文件
//		if(!updateTableValues(table_new_server_files,nsMgr.getTable_new_server_update_files().getConfigs()).isSucc()) {
//			return;
//		}
		if(!updateTableValues(table_new_sever_delete_files,onsMgr.getTable_new_server_delete_files().getConfigs()).isSucc()) {
			return;
		}
			//老区数据库导出文件
		if(!updateTableValues(table_old_server_db_tables,onsMgr.getTable_old_server_db_dump_tables().getConfigs()).isSucc()) {
			return;
		}
		//新区数据库表初始化
		if(!updateTableValues(table_auto_increment,onsMgr.getTable_auto_increment().getConfigs()).isSucc()) {
			return;
		}
		ExecuteResult result = onsMgr.updateParams_map(ip, path);
		if(result.isFail()) {
			JOptionPane.showMessageDialog(this,"新区参数"+result.getMsg()+"不对", "开新区", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//创建新的服务器对象
		Server newServer = createNewServer();
		//检测参数
		result = checkNewServerParam(newServer);
		if (result.isFail()) {
			return;
		}
		//开启线程
		new OpenNewServerThread(newServer, oldServer).start();
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
					JOptionPane.showMessageDialog(this,jtable.getColumnName(j)+"存在没有数据！", "开新区", JOptionPane.ERROR_MESSAGE);
					return ExecuteResult.RESULT_FAIL;
				}
				array2Val[i][j]= value;
			}
		}
		return ExecuteResult.RESULT_SUCC;
	}

	private Server createNewServer() {
		Server server = new Server();
		server.setName(onsMgr.getNewServerParamValue(NewServerParam.APP_NAME));
		server.setSsh_host(onsMgr.getNewServerParamValue(NewServerParam.APP_IP));
		server.setSsh_port(Integer.parseInt(onsMgr.getNewServerParamValue(NewServerParam.SSH_PORT)));
		server.setSsh_username(onsMgr.getNewServerParamValue(NewServerParam.SSH_USERNAME));
		server.setSsh_password(SshUtil.getStringEOR(onsMgr.getNewServerParamValue(NewServerParam.SSH_PASSWORD),Const.EOR_KEY));
		server.setSsh_remoteDir(onsMgr.getNewServerParamValue(NewServerParam.APP_PATH));
		server.setTelnet_host(onsMgr.getNewServerParamValue(NewServerParam.APP_IP));
		server.setTelnet_port(Integer.parseInt(onsMgr.getNewServerParamValue(NewServerParam.TELNET_PORT)));
		server.setSql_db(onsMgr.getNewServerParamValue(NewServerParam.DB_NAME));
		server.setSql_username(onsMgr.getNewServerParamValue(NewServerParam.SQL_USERNAME));
		server.setSql_password(SshUtil.getStringEOR(onsMgr.getNewServerParamValue(NewServerParam.SQL_PASSWORD),Const.EOR_KEY));
		server.setSql_port(Integer.parseInt(onsMgr.getNewServerParamValue(NewServerParam.SQL_PORT)));
		server.setSql_cmd_dir(onsMgr.getNewServerParamValue(NewServerParam.SQL_CMD_DIR));
		server.setServerType(ServerType.SUB_NET);
		server.setServer_instance(Integer.parseInt(onsMgr.getNewServerParamValue(NewServerParam.APP_INSTANCE).replace("0x",""),16));
		server.getAgent_online_server().init();
		return server;
	}

	public void init() {
		table_auto_increment.setModel(new DefaultTableModel
				(onsMgr.getTable_auto_increment().getConfigs(),TABLE_AUTO_INCREMENT_COULMN_NAMES));
		table_old_server_db_tables.setModel(new DefaultTableModel
				(onsMgr.getTable_old_server_db_dump_tables().getConfigs(),TABLE_OLD_SERVER_DB_DUMP_TABLES));
		table_common_table_files.setModel(new DefaultTableModel
				(onsMgr.getTable_common_db_resource_files().getConfigs(),TABLE_COMMON_TABLE_FILESS));
		table_new_server_params.setModel(new DefaultTableModel
				(onsMgr.getTable_new_server_params_config().getConfigs(),TABLE_NEW_SERVER_PARAMS_CONFIG_COUMLN));
		table_new_server_files.setModel(new DefaultTableModel
				(onsMgr.getTable_new_server_update_files().getConfigs(),TABLE_NEW_SERVER_FILES_COUMLN));
		TableColumn column= table_new_server_files.getColumn(TABLE_NEW_SERVER_FILES_COUMLN[0]);
		column.setPreferredWidth(5);
		table_new_sever_delete_files.setModel(new DefaultTableModel
				(onsMgr.getTable_new_server_delete_files().getConfigs(),TABLE_NEW_SERVER_DELETE_FILESS));
		textField_new_server.setText("");
		textField_newpath.setText("");
		comboBox_old_server.setModel(new DefaultComboBoxModel(OnlineServerManager.getInstance().getServersList().toArray()));
	}

	public void addErrorResultMsg(Server server,String msg) {
		addResultMsg(server, msg, Color.RED);
	}
	public void addSuccResultMsg(Server server,String msg) {
		addResultMsg(server, msg, new Color(0, 205,0));
	}	
	public void addNormalResultMsg(Server server,String msg) {
		addResultMsg(server, msg, Color.BLACK);
	}
	public synchronized void addResultMsg(Server server,String msg,Color color) {

		List<ResultShowMsg> list= null;
		if (server_result_msgs.containsKey(server)) {
			list = server_result_msgs.get(server);
		}
		if (list == null) {
			list = new ArrayList<ResultShowMsg>();
			list.add(new ResultShowMsg("服务器【"+server.toString()+"】:\n\t",Color.BLACK,16));
			server_result_msgs.put(server, list);
		}
		list.add(new ResultShowMsg(msg + "\n\t", color, 14));
		Document doc = logtextPane.getDocument();
		try {
			doc.remove(0,doc.getLength());
			showMsg(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public void showMsg(Document doc) {
		List<Entry<Server,List<ResultShowMsg>>> result_list = new ArrayList<Map.Entry<Server,List<ResultShowMsg>>>(server_result_msgs.entrySet());
		Collections.sort(result_list, new Comparator<Entry<Server,List<ResultShowMsg>>>() {

			@Override
			public int compare(Entry<Server, List<ResultShowMsg>> o1,
					Entry<Server, List<ResultShowMsg>> o2) {
				int value = o1.getKey().getServerType().ordinal() - o2.getKey().getServerType().ordinal();
				return value == 0 ? o1.getKey().getId() - o2.getKey().getId() : value;
			}
			
		});
		for (Entry<Server,List<ResultShowMsg>> entry :result_list) {
			List<ResultShowMsg> list = entry.getValue();
			for (int i=0;i<list.size();i++) {
				ResultShowMsg result = list.get(i);
				String msg = result.getMsg();
				if (i == 0) {
					msg = "\n"+msg;
				}
				try {
					doc.insertString(doc.getLength(), msg, result.getAttrSet());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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


	public JComboBox getComboBox_old_server() {
		return comboBox_old_server;
	}

	public JButton getButton_new_server() {
		return button_new_server;
	}

	public JTable getTable_auto_increment() {
		return table_auto_increment;
	}

	public JTable getTable_old_server_db_tables() {
		return table_old_server_db_tables;
	}

	public JTable getTable_common_table_files() {
		return table_common_table_files;
	}

	public JTable getTable_new_server_params() {
		return table_new_server_params;
	}

	public JTable getTable_new_server_files() {
		return table_new_server_files;
	}
}
