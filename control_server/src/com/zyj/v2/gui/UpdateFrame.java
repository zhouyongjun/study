package com.zyj.v2.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;

import com.zyj.v1.common.Instances;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.online_server.ServerGroup;
import com.zyj.v1.ssh.update.CheckHeaderCellRenderer;
import com.zyj.v1.ssh.update.CheckTableModle;
import com.zyj.v1.ssh.update.HelpDialog;
import com.zyj.v1.ssh.util.SshUtil;


public class UpdateFrame extends JFrame implements Instances {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane update_pannel;
	private JTable table;
	private JButton button_update ;
	JCheckBox checkbxDir ;
	JCheckBox chckbxLoad;
	JButton button_start;
	JButton button_stop;
	private JButton btnFreeze;
	JComboBox comboBox_group;
	private JMenuBar menuBar;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateFrame frame = new UpdateFrame();
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
	public UpdateFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 609, 543);
		SshUtil.SetCompomentBound(this, getWidth(), getHeight());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		update_pannel = new JTabbedPane(JTabbedPane.TOP);
		update_pannel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (update_pannel.getSelectedIndex() == 0 && comboBox_group != null) {
					initTable((ServerGroup)comboBox_group.getSelectedItem());
				}
			}
		});
		update_pannel.setBounds(0, 24, 593, 493);
		contentPane.add(update_pannel);
		
		JPanel panel = new JPanel();
		update_pannel.addTab("维护", null, panel, null);
		
		JLabel lblNewLabel = new JLabel("\u670D\u52A1\u5668\u7EC4:");
		comboBox_group = new JComboBox();
		comboBox_group.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerGroup group = (ServerGroup)comboBox_group.getSelectedItem();
				initTable(group);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		chckbxLoad = new JCheckBox("\u662F\u5426\u670D\u52A1\u5668unzip");
		chckbxLoad.setSelected(true);
		
		button_start= new JButton("\u542F\u52A8");
		button_start.setFont(new Font("黑体", Font.BOLD, 15));
		button_start.setForeground(new Color(50, 205, 50));
		button_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		
		btnFreeze = new JButton("FREEZE");
		btnFreeze.setForeground(new Color(0, 0, 0));
		btnFreeze.setFont(new Font("黑体", Font.BOLD, 12));
		btnFreeze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				freeze();
			}
		});
		
		button_stop = new JButton("\u505C\u670D");
		button_stop.setForeground(new Color(255, 51, 0));
		button_stop.setFont(new Font("黑体", Font.BOLD, 15));
		button_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stop();
			}
		});
		
		button_update = new JButton("\u66F4\u65B0");
		button_update.setFont(new Font("黑体", Font.BOLD, 18));
		button_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update();
			}
		});
		
		checkbxDir = new JCheckBox("\u662F\u5426\u6620\u5C04\u76EE\u5F55\u7ED3\u6784");
		checkbxDir.setSelected(true);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBox_group, 0, 418, Short.MAX_VALUE))
								.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(button_stop, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
								.addComponent(button_start, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnFreeze, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(64)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxLoad, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
								.addComponent(checkbxDir, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE))
							.addGap(112)
							.addComponent(button_update, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
							.addGap(148)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel)
						.addComponent(comboBox_group, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(checkbxDir)
									.addGap(18)
									.addComponent(chckbxLoad))
								.addComponent(button_update, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE))
							.addGap(19))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnFreeze, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addGap(60)
							.addComponent(button_stop, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addGap(58)
							.addComponent(button_start, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addGap(146))))
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new NewServerPanel();
		update_pannel.addTab("开区", null, panel_1, null);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 593, 21);
		contentPane.add(menuBar);
		
		JMenu menu_file = new JMenu("\u6587\u4EF6");
		menuBar.add(menu_file);
		
		JMenuItem menuItem_help = new JMenuItem("\u5E2E\u52A9");
		menuItem_help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new HelpDialog(UpdateFrame.this,true).setVisible(true);
			}
		});
		menu_file.add(menuItem_help);
		init();
		
	}
	
	protected void freeze() {
		List<Server> selected_servers = getSelectServers();
		if (selected_servers.size() == 0) {
			JOptionPane.showMessageDialog(this, "必须选择一个服务器");
			return;
		}
		UpdateMain.freezeServers(selected_servers);		
	}

	public void stop() {
		List<Server> selected_servers = getSelectServers();
		if (selected_servers.size() == 0) {
			JOptionPane.showMessageDialog(this, "必须选择一个服务器");
			return;
		}
		UpdateMain.stopServers(selected_servers);		
	}

	public void start() {
		List<Server> selected_servers = getSelectServers();
		if (selected_servers.size() == 0) {
			JOptionPane.showMessageDialog(this, "必须选择一个服务器");
			return;
		}
		UpdateMain.startServers(selected_servers);
	}

	public void update() {
		boolean isProduceDir = checkbxDir.isSelected();
		boolean isUnzip = chckbxLoad.isSelected();
		List<Server> selected_servers = getSelectServers();
		if (selected_servers.size() == 0) {
			JOptionPane.showMessageDialog(this, "必须选择一个服务器");
			return;
		}
		if (isProduceDir) UpdateMain.updateOfMapping(isUnzip,selected_servers);
		else UpdateMain.updateOfxCopy(isUnzip,selected_servers);
		
	}

	public List<Server> getSelectServers() {
		ServerGroup group = (ServerGroup)comboBox_group.getSelectedItem();
		List<Server> selected_servers = new ArrayList<Server>();
		for (int i=0;i<table.getRowCount();i++) {
			boolean value = (Boolean)table.getModel().getValueAt(i, 0);
			if (value)selected_servers.add(group.getServers().get(i));
		}
		return selected_servers;
	}

	private void init() {
		List<ServerGroup> groups = onsMgr.getGroupsList();
		comboBox_group.setModel(new DefaultComboBoxModel(groups.toArray(new ServerGroup[groups.size()])));
		initTable(groups.get(0));		
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initTable(ServerGroup group){
        Vector headerNames=new Vector();
        headerNames.add("全部");
        headerNames.add("服务器");
        Vector data=this.getData(group);
        CheckTableModle tableModel=new CheckTableModle(data,headerNames);
        table.setModel(tableModel);
        table.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(table));
        
        TableColumn first_column = table.getColumnModel().getColumn(0);
		first_column.setPreferredWidth(80);
		first_column.setMaxWidth(80);
    }
	   
	   /**
	     * 获得数据
	     * @return
	     */
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		private Vector getData(ServerGroup group){
	        Vector data = new Vector();
	        for (Server server : group.getServers()) {
	        	String dir = server.getSimpleDir();
	        	Vector row =new Vector();
	        	row.add(false);
	        	row.add(server.getServer_instance() +":"+server.getName()+"["+dir+"]");
	        	data.add(row);
	        }
	        return data;
	    }
}
