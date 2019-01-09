package com.zyj.v1.gui.panel.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.zyj.v1.SSHMain;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.ServerGroup;
import com.zyj.v1.ssh.util.SshUtil;
	/**
	 * 服务器组配置界面
	 * @author zhouyongjun
	 *
	 */
public class ServerGroupDialog extends JDialog {
	private static final long serialVersionUID = -8467740536343176755L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton cancelButton;
	private JTextField text_name;
	JList jlistSelectServers;
	JList jlistOperates;
	boolean isAdd;
	ServerGroup group;
	OnlineServerManager sshMgr = OnlineServerManager.getInstance();
	List<Server> select_servers = new ArrayList<Server>();//选择执行的服务器列表
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ServerGroupDialog dialog = new ServerGroupDialog(true,new ServerGroup());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ServerGroupDialog(boolean isAdd,ServerGroup group) {
		super(SSHMain.mainFrame,true);
		this.isAdd = isAdd;
		this.group = group;
		setBounds(100, 100, 619, 476);
		SshUtil.SetCompomentBound(this, 619, 476);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			okButton = new JButton("\u786E\u5B9A");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String name = text_name.getText();
					if (name.length() == 0) {
						JOptionPane.showMessageDialog(SSHMain.mainFrame, "分组名字不能为空！");
						return;
					}
					if (sshMgr.isExistedGroupName(name,ServerGroupDialog.this.group)) {
						JOptionPane.showMessageDialog(SSHMain.mainFrame, "分组名字已经存在！");
						return;
					}
					ServerGroupDialog.this.group.setName(name);
					if (select_servers.size() == 0) {
						JOptionPane.showMessageDialog(SSHMain.mainFrame, "已选服务器列表不能为空！");
						return;
					}
					ServerGroupDialog.this.group.setServers(select_servers);
					if (ServerGroupDialog.this.isAdd) {
						sshMgr.addServerGroup(ServerGroupDialog.this.group);
					}
					sshMgr.unloadServerGroupXml();
					SSHMain.mainFrame.getPanel_operate_online_server().flushRadioButtonGroup();
					ServerGroupDialog.this.dispose();
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		{
			cancelButton = new JButton("\u9000\u51FA");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ServerGroupDialog.this.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
		}
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton button = new JButton("\u6DFB\u52A0\u5168\u90E8");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select_servers.clear();
				select_servers.addAll(sshMgr.getServersList());
				jlistSelectServers.setListData(select_servers.toArray());
			
			}
		});
		
		JButton btnNewButton = new JButton("\u6DFB\u52A0");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] objects = jlistOperates.getSelectedValues();
				if (objects == null || objects.length == 0) {
					JOptionPane.showMessageDialog(SSHMain.mainFrame, "未选着服务器！");
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
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JLabel label = new JLabel("\u540D\u5B57\uFF1A");
		
		text_name = new JTextField();
		text_name.setColumns(10);
		
		JLabel label_1 = new JLabel("\u603B\u670D\u5217\u8868");
		
		JButton button_1 = new JButton("\u5220\u9664\u5168\u90E8");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select_servers.clear();
				jlistSelectServers.setListData(select_servers.toArray());
			
			}
		});
		
		JButton button_2 = new JButton("\u5220\u9664");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] objects = jlistSelectServers.getSelectedValues();
				if (objects == null || objects.length == 0) {
					JOptionPane.showMessageDialog(SSHMain.mainFrame, "未选着服务器！");
					return;
				}
				for (Object obj : objects) {
					select_servers.remove(obj);
				}
				jlistSelectServers.setListData(select_servers.toArray());
			
			}
		});
		
		JLabel label_2 = new JLabel("\u5DF2\u9009\u5217\u8868");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(203)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(text_name, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(35)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_contentPanel.createSequentialGroup()
												.addGap(71)
												.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
													.addComponent(button_1)
													.addComponent(button))
												.addGap(62))
											.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
												.addComponent(btnNewButton)
												.addGap(74)))
										.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
											.addComponent(button_2)
											.addGap(72))))
								.addComponent(label_1))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(label_2)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(69, Short.MAX_VALUE))
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(174)
					.addComponent(okButton)
					.addGap(145)
					.addComponent(cancelButton)
					.addContainerGap(160, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label)
								.addComponent(text_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(label_2))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(label_1)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPanel.createSequentialGroup()
								.addGap(30)
								.addComponent(button)
								.addGap(18)
								.addComponent(btnNewButton)
								.addGap(84)
								.addComponent(button_1)
								.addGap(18)
								.addComponent(button_2))
							.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(scrollPane, Alignment.LEADING)
									.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
								.addGap(54)))
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
							.addGap(324)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(okButton)
								.addComponent(cancelButton))
							.addGap(34))))
		);
		
		jlistSelectServers = new JList();
		scrollPane_1.setViewportView(jlistSelectServers);
		
		jlistOperates = new JList();
		scrollPane.setViewportView(jlistOperates);
		contentPanel.setLayout(gl_contentPanel);
		init();
	}

	public void init() {
		text_name.setText(group.getName());
		jlistSelectServers.setListData(group.getServers().toArray());
		jlistOperates.setListData(sshMgr.getServersList().toArray());
		select_servers = new ArrayList<Server>(group.getServers());
	}
}
