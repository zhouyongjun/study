package com.zhou.core.astar.test.map;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

/**
 * 支持二维点坐标处理，寻路
 * @author zhouyongjun
 *
 */
public class AStar_V3  {
	RectCell[][] cells;
	int[][] values;
	JFrame frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		AStar_V3 aStar = new AStar_V3();
		aStar.init();
		aStar.start();
	}

	public static final int BASIC = 3;
	public static final int BASIC_X = BASIC;
	public static final int BASIC_Y = BASIC;
	public static final RectCell A = RectCell.newInstance(0,0,0);
	public static final RectCell B = RectCell.newInstance(0,29,17);
//	public static final RectCell B = new RectCell(new Rectangle((281)*BASIC, (281)*BASIC, BASIC, BASIC), (byte)0, 280,280);
	public static final Node NB = new Node(null, B);
	
	public AStar_V3() {
	}
	public AStar_V3(JFrame frame)
	{
		this.frame = frame;
	}
	public void test()
	{
	}
	public void init() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("./map/newmap_blocked.txt")));
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
						cells[y][x] =  RectCell.newInstance(Byte.parseByte(datas[x]),y,x); 
//								new RectCell(new Rectangle((x+1)*BASIC, (y+1)*BASIC, BASIC, BASIC), Byte.parseByte(datas[x]), y, x);
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
	public Node start() {
		long currtime = System.currentTimeMillis();
		Node nodeA = new Node(null, A);
		Map<RectCell,Node> closed = new HashMap<>(120000);
		Map<RectCell,Node> opened = new HashMap<>(1200);
		Node node = nodeA;
		int count = 0;
		while(true)
		{
			node = nextNode(node, closed, opened);
			count++;
			if (node == null || (node.cell.x == B.x && node.cell.y == B.y)) break;
		}
		StringBuilder sb = new StringBuilder();
		long time =  System.currentTimeMillis() - currtime;
		if (node == null)
		{
			sb.append("寻路失败，").append("count=").append(count).append(",耗时毫秒数：" +(time)).append(",ps:").append(count/time);
		}
		else {
			Node parent = node;
			do {
				parent.cell.type = 4;
				parent = parent.parent;
			}while(parent != null);		
			sb.append("寻路成功").append("count=").append(count).append(",耗时毫秒数：" +(time)).append(",ps:").append(count/time).append(",G:"+node.G+",H:"+node.H+","+node.F);
		}
		System.out.println(sb.toString());
		return node;
//		JOptionPane.showMessageDialog(this, sb.toString());
	}
	
	
	Node next;
	
	Node nextNode(Node from,Map<RectCell,Node> closed,Map<RectCell,Node> opened) {
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
		if (frame != null)
		{
			frame.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		return next;
		
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
			if (y<0 || x<0 || y>=cells.length || x>= cells[0].length) {
				return null;
			}
			RectCell cell = cells[y][x];
			if (cell.isFill() || closed.containsKey(cell)) return null;
			Node to = new Node(from, cell);
			
			int h = from.calculateG(to);
			int d = values[to.cell.y][to.cell.x];
			if (h >= d) return null;
			values[to.cell.y][to.cell.x] = h;
			to.calculateF(NB);		
			opened.put(cell, to);
			return to;
		} catch (Exception e) {
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
	public RectCell[][] getCells() {
		return cells;
	}
	public int[][] getValues() {
		return values;
	}
	public static class RectCell {
		Rectangle rect;
		byte initType;
		byte type;//0:空，1：阻挡，2：可跳跃
		int y;
		int x;
		public RectCell(Rectangle rectangle,byte type,int y,int x) {
			this.rect = rectangle;
			this.type = type;
			this.initType = type;
			this.y = y;
			this.x = x;
		}
		
		public boolean isFill() {
			return type == 1;
		}
		
		public boolean isJump() {
			return type == 2;
		}
		
		public boolean isNull() {
			return type == 0;
		}
		public Rectangle getRect() {
			return rect;
		}
		public byte getType() {
			return type;
		}
		
		@Override
			public String toString() {
				return "Cell["+type+",("+x+","+y+")("+rect.width+","+rect.height+")]";
			}
		
		public void resest()
		{
			type = initType;
		}

		public static RectCell newInstance(int type,int x, int y) {
			RectCell cell = new RectCell(new Rectangle((y+1)* BASIC, (x+1)* BASIC, BASIC, BASIC), (byte)type, x, y);
			return cell;
		}
	}
}
