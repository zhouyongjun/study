package com.zhou.core.astar.test.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.zhou.core.astar.test.map.AStar_V3.Node;
import com.zhou.core.astar.test.map.AStar_V3.RectCell;
public class AStarFrame_V3 extends JFrame {
	static AStarFrame_V3 frame ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane ;
	boolean isRuned;
	AStar_V3 aStar;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AStarFrame_V3();
					frame.setVisible(true);
//					frame.test();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	static class MyRunnable implements Runnable {
		AStarFrame_V3 frame;
		MyRunnable(AStarFrame_V3 frame) {
			this.frame = frame;
		}
		@Override
		public void run() {
			try {
				frame.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	 class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D)g;

			for (RectCell[] row : aStar.getCells()) {
				for (RectCell column : row) {
				Color color = Color.black;
				if (column.isFill()) {
					g2d.setColor(color);
					g2d.fill(column.getRect());
				}
				else if(column.isJump()){
					color = Color.green;
					g2d.setColor(color);
					g2d.fill(column.getRect());
				}else if(column.type == 3) {
					color = Color.pink;
					g2d.setColor(color);
					g2d.fill(column.getRect());
				}
				else if(column.type == 4) {
					color = Color.red;
					g2d.setColor(color);
					g2d.fill(column.getRect());
				}
				else { 
					g2d.setColor(color);
					g2d.draw(column.getRect());	
				}
			}
			}
			g2d.setColor(Color.BLUE);
			g2d.fill(AStar_V3.A.getRect());
			g2d.setColor(Color.red);
			g2d.fill(AStar_V3.B.getRect());
	}
	public void paint(Graphics2D  g2d,Shape s) {
		g2d.drawRect(10, 10, 10, 10);
	}
	}
	/**
	 * Create the frame.
	 */
	public AStarFrame_V3() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1800, 1024);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
//		this.getContentPane().setBounds(100, 100, 1024, 1024);
		getContentPane().setLayout(null);
		contentPane.setLayout(null);
		JPanel jp=new MyPanel();
		jp.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane sp=new JScrollPane( jp);
		sp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				JOptionPane.showMessageDialog(frame, "右击选择");
			}
		});
		sp.setBounds(10, 10, 1700, 970);
		this.getContentPane().add(sp);
		
		JButton btnNewButton = new JButton("寻路");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isRuned)
				{
					for (RectCell[] row : aStar.getCells())
					{
						for (RectCell column : row)
						{
							column.resest();
						}
					}
					for (int[] row : aStar.getValues())
					{
						for (int i=0;i<row.length;i++)
						{
							row[i]= Integer.MAX_VALUE;	
						}
					}
					repaint();
				}
				test();
			}
		});
		
		btnNewButton.setBounds(1720, 45, 60, 42);
		contentPane.add(btnNewButton);

		jp.setPreferredSize(new Dimension(900 * AStar_V4.BASIC,900 * AStar_V4.BASIC));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		this.setVisible(true);
		init();
	}
	
	public void test()
	{
		new Thread(new MyRunnable(frame)).start();
		this.isRuned = true;
	}
	private void init() {
		try {
			aStar = new AStar_V3(this);
			aStar.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void start() {
		long currtime = System.currentTimeMillis();
		Node node = aStar.start();
		StringBuilder sb = new StringBuilder();
		if (node == null)
		{
			sb.append("寻路失败，").append("耗时毫秒数：" +( System.currentTimeMillis() - currtime));
		}
		else {
			Node parent = node;
			do {
				parent.cell.type = 4;
				parent = parent.parent;
			}while(parent != null);		
			sb.append("寻路成功,耗时毫秒数：" +( System.currentTimeMillis() - currtime)+",G:"+node.G+",H:"+node.H+","+node.F);
		}
		repaint();
		JOptionPane.showMessageDialog(this, sb.toString());
	}
}
