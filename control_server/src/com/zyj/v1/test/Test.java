package com.zyj.v1.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Test {
	JFrame frame;
	JTextPane textPane;
	File file;
	Icon image;

	public Test() {
		frame = new JFrame("JTextPane");
		textPane = new JTextPane();
		file = new File("./classes/test/icon.gif");
		image = new ImageIcon(file.getAbsoluteFile().toString());
	}

	public void insert(String str, AttributeSet attrSet) {
		Document doc = textPane.getDocument();
		str = "\n" + str;
		try {
			doc.insertString(doc.getLength(), str, attrSet);
		} catch (BadLocationException e) {
			System.out.println("BadLocationException:   " + e);
		}
	}

	public void setDocs(String str, Color col, boolean bold, int fontSize) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, col);
		// 颜色
		if (bold == true) {
			StyleConstants.setBold(attrSet, true);
		}// 字体类型
		StyleConstants.setFontSize(attrSet, fontSize); // 字体大小
		insert(str, attrSet);
	}

	public void gui() {
		textPane.insertIcon(image);
		setDocs("第一行的文字", Color.red, false, 20);
		setDocs("第二行的文字", Color.BLACK, true, 25);
		setDocs("第三行的文字", Color.BLUE, false, 20);
		frame.getContentPane().add(textPane, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setSize(200, 300);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
//		Test test = new Test();
//		test.gui();
		try {
			Runtime.getRuntime().exec("./lib/zip.exe test.zip ./build.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}