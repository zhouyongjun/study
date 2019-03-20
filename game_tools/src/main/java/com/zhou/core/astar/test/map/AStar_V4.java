package com.zhou.core.astar.test.map;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

/**
 * 支持MAP形式，一维点坐标处理寻路
 * @author zhouyongjun
 *
 */
public class AStar_V4  {
	public static int BASIC = 4;
	public static int BASIC_X = BASIC;
	public static int BASIC_Y = BASIC;
	
	Map<Integer,RectCell> cells = new HashMap<Integer, RectCell>(); 
	Map<Integer,Integer> values = new HashMap<Integer, Integer>();
	JFrame frame;
	public  int max_rows = 100;
	public  int max_columns = 100;
	public RectCell srcCell = RectCell.newInstance(0,0,0);
	public RectCell targetCell = RectCell.newInstance(0,160,160);
	public Node targetNode = new Node(null, targetCell);
	public AStar_V4() {
	}
	public AStar_V4(JFrame frame)
	{
		this.frame = frame;
	}
	
	public boolean isNeedFrame()
	{
		return frame != null;
	}
	public void init(int srcPosition,int targetPosition) {
		
	}
	public void init() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("./map/newmap_blocked.txt")));
			String[] statistics = br.readLine().split("\\|");
			max_rows = Integer.parseInt(statistics[4]);
			max_columns = Integer.parseInt(statistics[5]);
			int size = max_rows * max_columns * 100/75 + 1;
			cells = new HashMap<Integer, RectCell>(size); 
			values = new HashMap<Integer, Integer>(size);
			String line = null;
			int y=0;
			while((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] datas = line.split("\\|");
				for (int x=0;x<datas.length;x++) {
					int position = getPosition(x, y);
					try {
						cells.put(position,RectCell.newInstance(Byte.parseByte(datas[x]),y,x)); 
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					values.put(position,Integer.MAX_VALUE);
				}
				y++;
			}
			System.out.println("max_rows="+max_rows+",max_columns="+max_columns+",size="+values.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int getPosition(int x,int y){
		return y * max_columns + x;
	}
	public Node start() {
		long currtime = System.currentTimeMillis();
		Node nodeA = new Node(null, srcCell);
		Map<RectCell,Node> closed = new HashMap<>(120000);
		Map<RectCell,Node> opened = new HashMap<>(1200);
		Node node = nodeA;
		int count = 0;
		while(true)
		{
			node = nextNode(node, closed, opened);
			count++;
			if (node == null || (node.cell.x == targetCell.x && node.cell.y == targetCell.y)) break;
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
		if (from.cell.x == targetCell.x && from.cell.y == targetCell.y){
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
			if (y<0 || x<0 || y>=max_rows || x>= max_columns) {
				return null;
			}
			int position = getPosition(x, y);
			RectCell cell = cells.get(position);
			if (cell == null || cell.isFill() || closed.containsKey(cell)) return null;
			Node to = new Node(from, cell);
			
			int h = from.calculateG(to);
			int toPosition = getPosition(to.cell.x,to.cell.y);
			int d = values.get(toPosition);
			if (h >= d) return null;
			values.put(toPosition, h);
			to.calculateF(targetNode);		
			opened.put(cell, to);
			return to;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public static class Node {
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
	
	public Map<Integer, RectCell> getCells() {
		return cells;
	}
	public Map<Integer, Integer> getValues() {
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		AStar_V4 aStar = new AStar_V4();
		aStar.init();
		aStar.start();
	}
}
