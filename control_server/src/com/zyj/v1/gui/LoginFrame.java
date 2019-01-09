package com.zyj.v1.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.zyj.v1.SSHMain;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.SSHManager;
import com.zyj.v1.ssh.common.ClientUser;
import com.zyj.v1.ssh.db.DBManager;
import com.zyj.v1.ssh.util.SshUtil;
	/**
	 * 登陆界面
	 * @author zhouyongjun
	 *
	 */
public class LoginFrame extends JFrame {
	private static final long serialVersionUID = 2089467445938259777L;
	private JPanel contentPane;
	private JTextField field_name;
	private JPasswordField field_pwd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void login() {
		String name = field_name.getText();
		String pwd = new String(field_pwd.getPassword());
		if (name.length() == 0 || pwd.length() == 0) {
			JOptionPane.showMessageDialog(this, "请输入用户名和密码！");
			return;
		}
		Map<String,Object> map = DBManager.getInstance().getGameDao().selectUser(name,pwd);
		if (map == null) {
			JOptionPane.showMessageDialog(this, "账号密码不正确！");
			return;
		}
		SSHMain.user = new ClientUser();
		SSHMain.user.loadFromData(map);
		SSHManager.geteInstance().init();
		AppLog.info("MAIN START SUCCFUL!!!");
		SSHMain.mainFrame = new MainFrame();
		SSHMain.mainFrame.setVisible(true);
		this.dispose();
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setFont(new Font("隶书", Font.BOLD, 21));
		setTitle("\u767B\u9646\u7A97\u53E3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 290);
		SshUtil.SetCompomentBound(this, 450, 290);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("\u8D26\u53F7\uFF1A");
		
		JLabel lblNewLabel_1 = new JLabel("\u5BC6\u7801\uFF1A");
		
		field_name = new JTextField();
		field_name.setColumns(10);
		
		JButton btnNewButton = new JButton("\u767B\u9646");
		btnNewButton.setMnemonic(KeyEvent.VK_ENTER);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}

		});
		
		
		JButton btnNewButton_1 = new JButton("\u9000\u51FA");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginFrame.this.dispose();
			}
		});
		
		field_pwd = new JPasswordField();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(85, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addComponent(lblNewLabel_1)
							.addComponent(lblNewLabel))
						.addComponent(btnNewButton))
					.addGap(22)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(field_name, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
						.addComponent(field_pwd, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
							.addComponent(btnNewButton_1)))
					.addGap(105))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(47)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(field_name, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(field_pwd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1))
					.addGap(48)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton_1)
						.addComponent(btnNewButton))
					.addGap(74))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
