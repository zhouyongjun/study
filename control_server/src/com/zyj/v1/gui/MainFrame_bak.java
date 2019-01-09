package com.zyj.v1.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import com.zyj.v1.SSHMain;
import com.zyj.v1.gui.panel.dialog.ServerGroupDialog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.OperateType;
import com.zyj.v1.ssh.common.ResultShowMsg;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.ServerGroup;
import com.zyj.v1.ssh.online_server.UpLoadFileGroup;
import com.zyj.v1.ssh.util.SshUtil;

import javax.swing.JCheckBox;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
	/**
	 * 主界面
	 * @author zhouyongjun
	 *
	 */
public class MainFrame_bak extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7485368895847385599L;
	OnlineServerManager sshMgr = OnlineServerManager.getInstance();
	private JPanel contentPane;
	JButton button_start;
	JButton button_stop;
	JList jlistSelectServers;//服务器选择组
	JTextPane logtextPane;
	JTextArea logtextArea;
	JRadioButton radiobutton_group;//选在服务器组按钮
	JRadioButton radiobutton_once;//选着单服
	JButton button_add_one;//增加一个
	JButton button_delete_one;//删除一个
	JButton button_add_all;//增加全部
	JButton button_delete_all;//删除全部
	JButton button_group_add;//设置分组
	JButton button_puton;//上传
	JButton button_download;//下载
	JLabel label_1;
	JList jlistOperates;//可操作列表
	JButton button_group_delete;//分组删除按钮
	JButton button_group_modify;//分组修改按钮
	
	/*
	 * 内存数据
	 */
	Map<Server,List<ResultShowMsg>> server_result_msgs = new HashMap<Server,List<ResultShowMsg>>();//与服务器通信信息存储
	List<Server> select_servers = new ArrayList<Server>();//选择执行的服务器列表
	UpLoadFileGroup upload_files;
	OperateType  operateType;//操作类型
	private JCheckBox isUploadTenetcheckBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public MainFrame_bak() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for(Server server : sshMgr.getServersList()) {
					server.getAgent_online_server().exit();
				}
			}
		});
		setTitle("\u670D\u52A1\u5668\u7EF4\u62A4\u5DE5\u5177");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1214, 784);
		SshUtil.SetCompomentBound(this, 1214, 784);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		
		label_1 = new JLabel("\u5206\u7EC4\u5217\u8868");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setAutoscrolls(true);
		button_start = new JButton("\u5F00\u670D");
		button_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (select_servers.size() == 0) {
					JOptionPane.showMessageDialog(contentPane, "开服失败，未选择服务器！");
					return;
				}
				operateType = OperateType.SERVER_MAINTAIN;
				clearResultDatas();
				sshMgr.execute_group_start_cmd(select_servers);
			}
		});
		
		button_stop= new JButton("\u505C\u670D");
		button_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (select_servers.size() == 0) {
					JOptionPane.showMessageDialog(contentPane, "停服失败，未选择服务器！");
					return;
				}
				operateType = OperateType.SERVER_MAINTAIN;
				clearResultDatas();
				sshMgr.execute_group_stop_cmd(select_servers);
				
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setAutoscrolls(true);
		radiobutton_group = new JRadioButton("\u5206\u7EC4\u9009\u9879");
		radiobutton_group.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				flushRadioButtonGroup();
			}
		});
		
		radiobutton_once = new JRadioButton("\u5355\u670D\u9009\u9879");
		radiobutton_once.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (radiobutton_once.isSelected()) {
					button_add_all.setVisible(true);
					button_add_one.setVisible(true);
					button_delete_all.setVisible(true);
					button_delete_one.setVisible(true);
					button_group_add.setVisible(false);
					button_group_delete.setVisible(false);
					button_group_modify.setVisible(false);
					label_1.setText("总服列表");
					select_servers.clear();
					jlistOperates.setListData(sshMgr.getServersList().toArray());
					jlistSelectServers.setListData(select_servers.toArray());
					clearResultDatas();
				}
			
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(radiobutton_group);
		group.add(radiobutton_once);
		
		JLabel label_2 = new JLabel("\u6267\u884C\u670D\u52A1\u5668\u5217\u8868");
		
		button_add_one = new JButton("\u6DFB\u52A0");
		button_add_one.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] objects = jlistOperates.getSelectedValues();
				if (objects == null || objects.length == 0) {
					JOptionPane.showMessageDialog(contentPane, "未选择服务器！");
					return;
				}
				for (Object obj : objects) {
					Server server = (Server)obj;
					if (select_servers.contains(server)) {
						continue;
					}
					select_servers.add((Server)obj);
				}
				jlistSelectServers.setListData(select_servers.toArray());
			
			}
		});
		
		button_delete_one = new JButton("\u5220\u9664");
		button_delete_one.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] objects = jlistSelectServers.getSelectedValues();
				if (objects == null || objects.length == 0) {
					JOptionPane.showMessageDialog(contentPane, "未选择服务器！");
					return;
				}
				for (Object obj : objects) {
					select_servers.remove(obj);
				}
				jlistSelectServers.setListData(select_servers.toArray());
			}
		});
		
		button_add_all = new JButton("\u6DFB\u52A0\u5168\u90E8");
		button_add_all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select_servers.clear();
				select_servers.addAll(sshMgr.getServersList());
				jlistSelectServers.setListData(select_servers.toArray());
			}
		});
		
		button_delete_all = new JButton("\u5220\u9664\u5168\u90E8");
		button_delete_all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select_servers.clear();
				jlistSelectServers.setListData(select_servers.toArray());
			}
		});
		
		button_group_add = new JButton("\u5206\u7EC4\u589E\u52A0");
		button_group_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ServerGroupDialog(true, new ServerGroup()).setVisible(true);
			}
		});
		
		button_puton = new JButton("\u4E0A\u4F20\u6587\u4EF6");
		button_puton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {/*
				FileDialog  dialog = new FileDialog(MainFrame.this,"选择上次文件");
//						JFileChooser.FILES_AND_DIRECTORIES);
				dialog.setVisible(true);
				System.out.println(dialog.getDirectory());
				System.out.println(dialog.getFile());
			*/
				clearResultDatas();
				JFileChooser chooser = new JFileChooser(OnlineServerManager.DIR_UPLOAD_UPDATE);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setMultiSelectionEnabled(true);
				int result = chooser.showOpenDialog(MainFrame_bak.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					selectUploadFiles(chooser.getSelectedFiles());  
				}else {
					JOptionPane.showMessageDialog(SSHMain.mainFrame, "没有选择需要更新的文件，上传取消!");
				}
				}
		});
		
		button_download = new JButton("\u4E0B\u8F7D\u6587\u4EF6");
		button_download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		button_group_delete = new JButton("\u5206\u7EC4\u5220\u9664");
		button_group_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!radiobutton_group.isSelected()) {
					JOptionPane.showMessageDialog(contentPane, "请先选择 分组 选项！");
					return;
				}
				int index = jlistOperates.getSelectedIndex();
				if (index < 0) {
					JOptionPane.showMessageDialog(contentPane, "请先选择要删除的服务器组！");
					return;
				}
				if (index == 0) {
					JOptionPane.showMessageDialog(contentPane, "所有服务器组不可删除！");
					return;
				}
				sshMgr.deleteGroup(index);
				sshMgr.unloadServerGroupXml();
				jlistOperates.setListData(sshMgr.getGroupsList().toArray());
				jlistSelectServers.setListData(new Server[0]);
			}
		});
		
		button_group_modify = new JButton("\u5206\u7EC4\u4FEE\u6539");
		button_group_modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!radiobutton_group.isSelected()) {
					return;
				}
				Object obj = jlistOperates.getSelectedValue();
				if (obj == null) {
					JOptionPane.showMessageDialog(contentPane, "请先选择要修改的服务器组！");
					return;
				}
				new ServerGroupDialog(false, (ServerGroup)obj).setVisible(true);
			}
		});
		
		isUploadTenetcheckBox = new JCheckBox("\u6267\u884C\u52A0\u8F7D\u547D\u4EE4");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(radiobutton_group)
									.addGap(23)
									.addComponent(radiobutton_once))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(10)
											.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_contentPane.createSequentialGroup()
													.addComponent(button_add_one)
													.addGap(18))
												.addGroup(gl_contentPane.createSequentialGroup()
													.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(button_group_add)
														.addComponent(button_add_all)
														.addComponent(button_group_delete)
														.addComponent(button_delete_all)
														.addComponent(button_group_modify))
													.addGap(10))))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(20)
											.addComponent(button_delete_one))))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(55)
							.addComponent(label_1)))
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(label_2)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
									.addGap(33)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(button_stop)
										.addComponent(button_start))))
							.addGap(33))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(isUploadTenetcheckBox)
							.addGap(41)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 610, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(button_puton)
							.addGap(84)
							.addComponent(button_download)))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(60)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 635, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(33)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(button_download)
										.addComponent(button_puton)
										.addComponent(isUploadTenetcheckBox))
									.addGap(33))
								.addComponent(label_2)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(19)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(radiobutton_group)
								.addComponent(radiobutton_once))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(label_1)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(24)
									.addComponent(button_add_all)
									.addGap(18)
									.addComponent(button_add_one)
									.addGap(26)
									.addComponent(button_group_add)
									.addGap(29)
									.addComponent(button_group_modify)
									.addGap(30)
									.addComponent(button_group_delete)
									.addGap(26)
									.addComponent(button_delete_all)
									.addGap(18)
									.addComponent(button_delete_one))
								.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
								.addComponent(scrollPane)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(102)
							.addComponent(button_start)
							.addGap(172)
							.addComponent(button_stop)))
					.addContainerGap(40, Short.MAX_VALUE))
		);
		
		jlistOperates = new JList();
		jlistOperates.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (radiobutton_group.isSelected()) {
					Object obj = jlistOperates.getSelectedValue();
					if (obj == null) {
//						JOptionPane.showMessageDialog(contentPane, "未选择列表行！");	
						return;
					}
					select_servers.clear();
					select_servers.addAll(((ServerGroup)obj).getServers());
					jlistSelectServers.setListData(select_servers.toArray());
				}
			}
		});
		scrollPane.setViewportView(jlistOperates);
		
		jlistSelectServers = new JList();
		scrollPane_2.setViewportView(jlistSelectServers);
		
		logtextArea = new JTextArea();
		logtextArea.setText("\u4E0E\u670D\u52A1\u5668\u901A\u4FE1\u4FE1\u606F\u63D0\u793A");
		logtextArea.setLineWrap(true);
		scrollPane_1.setColumnHeaderView(logtextArea);
		
		logtextPane = new JTextPane();
		scrollPane_1.setViewportView(logtextPane);
		contentPane.setLayout(gl_contentPane);
		init();
	}
	
	/**
	 * 选择的文件组
	 * @param selectedFiles
	 */
	public void selectUploadFiles(File[] selectedFiles) {
		if (selectedFiles == null || selectedFiles.length == 0) {
			JOptionPane.showMessageDialog(SSHMain.mainFrame, "没有选择需要更新的文件，上传取消!");
			return;
		}
		Server mainServer = sshMgr.getMainServer();
		if (mainServer == null) {
			JOptionPane.showMessageDialog(SSHMain.mainFrame, "没有选择需要更新的文件，上传取消!");
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("上传资源信息列表： ");
		for (File file : selectedFiles) {
			sb.append(file.getName()).append(",");
		}
		int select_vaule=JOptionPane.showConfirmDialog(this, sb.toString()+"是否上传？","上传",JOptionPane.YES_NO_OPTION);
		if (select_vaule != 0) {
			return;
		}
		operateType = OperateType.UPLOAD;
		upload_files = new UpLoadFileGroup();
		//不知满足则结束
		if(!upload_files.setNeedMappingFiles(OnlineServerManager.APP_JAR,SSHMain.mainFrame.getPanel_operate_online_server(),sshMgr.getMainServer(),selectedFiles)) {
			return;
		}
		sshMgr.execute_upload_resources(sshMgr.getMainServer(),select_servers,upload_files,isUploadTenetcheckBox.isSelected());
		
	}

	private void init() {
		button_add_all.setVisible(false);
		button_add_one.setVisible(false);
		button_delete_all.setVisible(false);
		button_delete_one.setVisible(false);
		radiobutton_group.setSelected(true);
	}

	protected void clearResultDatas() {
		server_result_msgs.clear();
		logtextPane.setText("");
	}
	public void addErrorResultMsg(Server server,String msg) {
		addResultMsg(server, msg, Color.RED);
	}
	public void addSuccResultMsg(Server server,String msg) {
		addResultMsg(server, msg, new Color(0, 205,0));
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
	public void addNormalResultMsg(Server server,String msg) {
		addResultMsg(server, msg, Color.BLACK);
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

	public void flushRadioButtonGroup() {
		if (radiobutton_group.isSelected()) {
			button_add_all.setVisible(false);
			button_add_one.setVisible(false);
			button_delete_all.setVisible(false);
			button_delete_one.setVisible(false);
			button_group_add.setVisible(true);
			button_group_delete.setVisible(true);
			button_group_modify.setVisible(true);
			label_1.setText("分组列表");
			select_servers.clear();
			select_servers.addAll(sshMgr.getGroupsList().get(0).getServers());
			jlistOperates.setListData(sshMgr.getGroupsList().toArray());
			jlistSelectServers.setListData(select_servers.toArray());
			clearResultDatas();
		}
	
	}

	public Map<Server, List<ResultShowMsg>> getServer_result_msgs() {
		return server_result_msgs;
	}

	public void setServer_result_msgs(
			Map<Server, List<ResultShowMsg>> server_result_msgs) {
		this.server_result_msgs = server_result_msgs;
	}

	public List<Server> getSelect_servers() {
		return select_servers;
	}

	public void setSelect_servers(List<Server> select_servers) {
		this.select_servers = select_servers;
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
}
