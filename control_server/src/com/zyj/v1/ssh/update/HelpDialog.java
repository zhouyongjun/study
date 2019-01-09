package com.zyj.v1.ssh.update;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.zyj.v1.ssh.util.SshUtil;
import com.zyj.v2.gui.UpdateMain;

public class HelpDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			HelpDialog dialog = new HelpDialog(new JFrame(),true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the dialog.
	 */
	public HelpDialog(JFrame frame, boolean isTrue) {
		setTitle("\u5E2E\u52A9");
		setBounds(100, 100, 609, 471);
		SshUtil.SetCompomentBound(this, getWidth(), getHeight());
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 589, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(59, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
		);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setText(UpdateMain.HELP_MSG);
		scrollPane.setViewportView(textArea);
		contentPanel.setLayout(gl_contentPanel);
	}
}
