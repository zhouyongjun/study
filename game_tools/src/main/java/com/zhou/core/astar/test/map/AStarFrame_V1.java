package com.zhou.core.astar.test.map;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AStarFrame_V1 extends JFrame {
	RectCell[][] cells;
	private JPanel contentPane ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AStarFrame_V1 frame = new AStarFrame_V1();
					frame.setVisible(true);
					new Thread(new MyRunnable(frame)).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	static class MyRunnable implements Runnable {
		AStarFrame_V1 frame;
		MyRunnable(AStarFrame_V1 frame) {
			this.frame = frame;
		}
		@Override
		public void run() {
			frame.start();
		}
		
	}
	 class MyPanel extends JPanel {
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2d = (Graphics2D)g;
			for (RectCell[] row : cells) {
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
			g2d.fill(A.getRect());
			g2d.setColor(Color.red);
			g2d.fill(B.getRect());
	}
	public void paint(Graphics2D  g2d,Shape s) {
		g2d.drawRect(10, 10, 10, 10);
	}
	}
	/**
	 * Create the frame.
	 */
	public AStarFrame_V1() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 1024);
		contentPane = new MyPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		getContentPane().setLayout(null);
		contentPane.setLayout(null);
		init();
	}
	public static final int BASIC = 6;
	public static final int BASIC_X = BASIC;
	public static final int BASIC_Y = BASIC;
	public static final RectCell A = new RectCell(new Rectangle((1)*BASIC, (1)*BASIC, BASIC, BASIC), (byte)0, 0, 0);
//	public static final RectCell B = new RectCell(new Rectangle((18)*BASIC, (30)*BASIC, BASIC, BASIC), (byte)0, 29,17);
	public static final RectCell B = new RectCell(new Rectangle((65)*BASIC, (101)*BASIC, BASIC, BASIC), (byte)0, 100,64);
	private void init() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./res/worldnpc/sc/chengshi.txt")));
			String[] statistics = br.readLine().split("\\|");
			int max_rows = Integer.parseInt(statistics[4]);
			int max_columns = Integer.parseInt(statistics[5]);
			cells = new RectCell[max_rows][max_columns];
			String line = null;
			int y=0;
			while((line = br.readLine()) != null) {
				String[] datas = line.split("\\|");
				for (int x=0;x<datas.length;x++) {
					cells[y][x] = new RectCell(new Rectangle((x+1)*BASIC, (y+1)*BASIC, BASIC, BASIC), Byte.parseByte(datas[x]), y, x);
				}
				y++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void start() {
		long currtime = System.currentTimeMillis();
		Node nodeA = new Node(null, A);
		Map<RectCell,Node> closed = new HashMap<>();
		Map<RectCell,Node> opened = new HashMap<>();
		Node node = go(nodeA, closed, opened);
		Node parent = node;
		do {
			parent.cell.type = 4;
			parent = parent.parent;
		}while(parent != null);
		repaint();
		JOptionPane.showMessageDialog(this, "耗时毫秒数：" +( System.currentTimeMillis() - currtime)+",G:"+node.G+",H:"+node.H+","+node.F);
	}
	Node next;
	Node go(Node from,Map<RectCell,Node> closed,Map<RectCell,Node> opened) {
		closed.put(from.cell, from);
		opened.remove(from.cell);
		if (from.cell.x == B.x && from.cell.y == B.y) return from;
		
		next = putOpened(closed,opened,from,1,0);
		Node to = putOpened(closed,opened,from,0,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		to = putOpened(closed,opened,from,-1,0);
		if (next == null || (to != null && to.F < next.F)) next = to;
		to = putOpened(closed,opened,from,0,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		to = putOpened(closed,opened,from,1,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		to = putOpened(closed,opened,from,1,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		to = putOpened(closed,opened,from,-1,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		to = putOpened(closed,opened,from,-1,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		next = getMinOpendNode(next,opened);
		next.cell.type = 3;
		System.err.println("next go:"+next.toString());
//		repaint();
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return go(next, closed, opened);
		
	}
	
	private Node getMinOpendNode(Node next,Map<RectCell, Node> opened) {
		for (Node node : opened.values()) {
			if (next == null || node.F < next.F) next = node;
		}
		return next;
	}
	private Node putOpened(Map<RectCell,Node> closed,Map<RectCell, Node> opened, Node from,int r_upd,int c_upd) {
		//
		int x = from.cell.x + c_upd;
		int y = from.cell.y + r_upd;
		if (y<0 || x<0 || y>=cells.length || x>= cells[0].length) {
			return null;
		}
		RectCell cell = cells[y][x];
		if (cell.isFill() || closed.containsKey(cell)) return null;
		Node old = opened.get(cell);
		Node to = new Node(from, cell);
		int c_y = Math.abs(B.y-y);
		int c_x = Math.abs(B.x-x);
		to.calc(from.G+1,  c_y * c_y+ c_x * c_x );
		if (old != null && old.F>to.F) return null;
		opened.put(cell, to);
		return to;
		
	}
	class Node {
		Node parent;
		int G;//从起点，沿着产生的路径
		int H;//从网格上那个方向移动到终点的估计移动消费，曼哈顿方法:从当前格到目的格之间水平和垂直的方格的数量总和，忽略对角线方向。然后把结果乘以10。|x1-x2|+|y1-y2|(忽略障碍物)
		int F;//G+H
		RectCell cell;
		public Node(Node parent,RectCell cell) {
			this.parent = parent;
			this.cell = cell;
		}
		public void calc(int g, int h) {
			this.G = g;
			this.H = h;
			this.F = g+h;		
		}
		public String toString() {
			return "Node{"+cell.toString() + "  GHF["+G+","+H+","+F+"]}";
		}
	}
}
