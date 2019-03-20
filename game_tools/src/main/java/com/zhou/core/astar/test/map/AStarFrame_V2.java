package com.zhou.core.astar.test.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class AStarFrame_V2 extends JFrame {
	static AStarFrame_V2 frame ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RectCell[][] cells;
	int[][] values;
	private JPanel contentPane ;
	boolean isRuned;

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
					frame = new AStarFrame_V2();
					frame.setVisible(true);
//					frame.test();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	static class MyRunnable implements Runnable {
		AStarFrame_V2 frame;
		MyRunnable(AStarFrame_V2 frame) {
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
	public AStarFrame_V2() {
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
					for (RectCell[] cell : cells)
					{
						for (RectCell c : cell)
						{
							c.resest();
						}
					}
					for (int[] value : values)
					{
						for (int i=0;i<value.length;i++)
						{
							value[i] = Integer.MAX_VALUE;
						}
							
					}
					repaint();
				}
				test();
			}
		});
		
		btnNewButton.setBounds(1720, 45, 60, 42);
		contentPane.add(btnNewButton);

		jp.setPreferredSize(new Dimension(900 * BASIC,900 * BASIC));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		this.setVisible(true);
		init();
	}
	public static final int BASIC = 4;
	public static final int BASIC_X = BASIC;
	public static final int BASIC_Y = BASIC;
	public static final RectCell A = new RectCell(new Rectangle((1)*BASIC, (1)*BASIC, BASIC, BASIC), (byte)0, 0, 0);
//	public static final RectCell B = new RectCell(new Rectangle((18)*BASIC, (30)*BASIC, BASIC, BASIC), (byte)0, 29,17);
	public static final RectCell B = new RectCell(new Rectangle((181)*BASIC, (181)*BASIC, BASIC, BASIC), (byte)0, 180,180);
	public static final Node NB = new Node(null, B);
	
	public void test()
	{
		new Thread(new MyRunnable(frame)).start();
		this.isRuned = true;
	}
	private void init() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./map/newmap_blocked.txt")));
			String[] statistics = br.readLine().split("\\|");
			int max_rows = Integer.parseInt(statistics[4]);
			int max_columns = Integer.parseInt(statistics[5]);
			cells = new RectCell[max_rows][max_columns];
			values= new int[max_rows][max_columns];
			String line = null;
			int y=0;
			while((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] datas = line.split("\\|");
				for (int x=0;x<datas.length;x++) {
					try {
						cells[y][x] = new RectCell(new Rectangle((x+1)*BASIC, (y+1)*BASIC, BASIC, BASIC), Byte.parseByte(datas[x]), y, x);
					} catch (Exception e) {
						e.printStackTrace();
					}
					values[y][x] = Integer.MAX_VALUE;
				}
				y++;
			}
			System.out.println("y="+y+",x="+values[0].length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void start() {
		long currtime = System.currentTimeMillis();
		Node nodeA = new Node(null, A);
		Map<RectCell,Node> closed = new HashMap<>(120000);
		Map<RectCell,Node> opened = new HashMap<>(1200);
		Node node = nodeA;
		while(true)
		{
			node = go(node, closed, opened);
			if (node == null || (node.cell.x == B.x && node.cell.y == B.y)) break;
		}
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
	
	
	Node next;
	
	Node go(Node from,Map<RectCell,Node> closed,Map<RectCell,Node> opened) {
		if (from == null) return null;
		closed.put(from.cell, from);
		opened.remove(from.cell);
		if (from.cell.x == B.x && from.cell.y == B.y){
			return from;//找到目标点
		}
		//8个方向查找
		//下方
		next = putOpened(closed,opened,from,1,0);
		//右方
		Node to = putOpened(closed,opened,from,0,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//上方
		to = putOpened(closed,opened,from,-1,0);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//左方
		to = putOpened(closed,opened,from,0,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//右下方
		to = putOpened(closed,opened,from,1,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//左下方
		to = putOpened(closed,opened,from,1,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//右下方
		to = putOpened(closed,opened,from,-1,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//左上方
		to = putOpened(closed,opened,from,-1,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//筛选出最小距离F的点
		next = getMinOpendNode(next,opened);
		if (next == null){
			System.err.println("next go: is null");
			return null;//闭环中，无法找到目标
		}
		next.cell.type = 3;
		System.err.println("next go:"+next.toString());
//		repaint();
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return next/*go(next, closed, opened)*/;
		
	}
	
	private Node getMinOpendNode(Node next,Map<RectCell, Node> opened) {
		try {
			for (Node node : opened.values()) {
				if (next == null || node.F < next.F) next = node;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return next;
	}
	private Node putOpened(Map<RectCell,Node> closed,Map<RectCell, Node> opened, Node from,int r_upd,int c_upd) {
		//
		try {
			int x = from.cell.x + c_upd;
			int y = from.cell.y + r_upd;
			//不在范围内
			if (y<0 || x<0 || y>=cells.length || x>= cells[0].length) {
				return null;
			}
			RectCell cell = cells[y][x];
			//阻挡层或者 已经寻找过
			if (cell.isFill() || closed.containsKey(cell)) return null;
			Node to = new Node(from, cell);
			int f = from.calculateG(to);
			//此点的G最短记录，默认Integer最大值，MIN G
			int d = values[to.cell.y][to.cell.x];
			if (f >= d) return null;
			values[to.cell.y][to.cell.x] = f;
			to.calculateF(NB);		
			opened.put(cell, to);
			return to;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	static class Node {
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
		
		/*
		 * 计算当前节点的F值
		 */
		public void calculateF(Node target) {
			if (parent != null)
				this.G = parent.G + predict(parent);
			this.F = this.G + this.predict(target);
		}

		/*
		 * 计算当前节点的G值
		 */
		public int calculateG(Node target) {
			return this.G + predict(target);
		}

		/*
		 * 计算当前节点到目标节点的启发因子代价（H）
		 */
		private int predict(Node target) {
			int dx = Math.abs(this.cell.x - target.cell.x);
			int dy = Math.abs(this.cell.y - target.cell.y);
			int a = Math.abs(dx - dy);
			int b = Math.min(dx, dy);
			return a * a + b * b;
		}
	}
}
