package com.zyj.v1.gui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.zyj.v1.gui.panel.OpenNewServerPanel;
import com.zyj.v1.gui.panel.OperateOnlineServerPanel;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.util.SshUtil;
	/**
	 * 主界面
	 * @author zhouyongjun
	 *
	 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = -7485368895847385599L;
	OnlineServerManager sshMgr = OnlineServerManager.getInstance();
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private OperateOnlineServerPanel panel_operate_online_server;
	private OpenNewServerPanel panel_open_new_server;
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
	public MainFrame() {
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
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1198, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE))
		);
		
		panel_operate_online_server = new OperateOnlineServerPanel();
		tabbedPane.addTab("维护服务器", null, panel_operate_online_server, null);
		
		panel_open_new_server = new OpenNewServerPanel();
		tabbedPane.addTab("开新区", null, panel_open_new_server, null);
		contentPane.setLayout(gl_contentPane);
	}

	public OperateOnlineServerPanel getPanel_operate_online_server() {
		return panel_operate_online_server;
	}

	public void setPanel_operate_online_server(OperateOnlineServerPanel operate_online_server_panel) {
		this.panel_operate_online_server = operate_online_server_panel;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public OpenNewServerPanel getPanel_open_new_server() {
		return panel_open_new_server;
	}
}
