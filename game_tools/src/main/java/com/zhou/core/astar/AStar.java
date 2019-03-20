package com.zhou.core.astar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhou.core.astar.RectCell.CellType;

/**
 * ֧��MAP��ʽ��һά�����괦��Ѱ·
 * @author zhouyongjun
 *
 */
public class AStar {
	private static final Logger logger = LoggerFactory.getLogger(AStar.class);
	public static final int DEFAULT_MAX_LOOP_COUNT = 5000000;
	Map<Integer,RectCell> cells = new HashMap<Integer, RectCell>(); 
	Map<Integer,Integer> values = new HashMap<Integer, Integer>();
	AStarFrame frame;
	public int max_rows = 100;
	public int max_columns = 100;
	public RectCell srcCell = RectCell.newInstance(CellType.SPACE,0,0,true);
	public RectCell targetCell = RectCell.newInstance(CellType.SPACE,160,160,true);
	public Node targetNode = new Node(null, targetCell);
	protected int max_loop_count = DEFAULT_MAX_LOOP_COUNT;
//	public Map<Integer,Integer> openedSize = new HashMap<Integer, Integer>();
	protected AStar() {
	}
	
	public boolean isNeedFrame()
	{
		return frame != null;
	}
	public void init()
	{
		
	}
	
	public void loadInitDataFromFile() {
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
						cells.put(position,RectCell.newInstance(CellType.valueOf(Byte.parseByte(datas[x])),y,x,true)); 
					} catch (Exception e) {
						logger.error("",e);
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
	public int getX(int pos){
		return pos % max_columns;
	}
	public int getY(int pos){
		return pos / max_columns;
	}
	public AStarResult tryToFindAWay() {
		logger.info("tryToFindAWay "+srcCell+" ---->>"+targetCell+" is begin.");
		long currtime = System.currentTimeMillis();
		Node nodeA = new Node(null, srcCell);
		Map<RectCell,Node> closed = new HashMap<>(120000);
		Map<RectCell,Node> opened = new HashMap<>(1200);
		Node node = nodeA;
		int count = 0;
		while(true && count < max_loop_count)
		{
			node = nextNode(node, closed, opened);
			count++;
			if (node == null || (node.cell.x == targetCell.x && node.cell.y == targetCell.y)) break;
		}
		StringBuilder sb = new StringBuilder();
		long time =  System.currentTimeMillis() - currtime;
		if (node == null)
		{
			sb.append("--> fail ::: ");
		}
		else {
			Node parent = node;
			do {
				parent.cell.type = CellType.TARGET;
				parent = parent.parent;
			}while(parent != null);		
			sb.append("::: succ ::: ");
		}
		sb.append("count=").append(count).append(",time(ms)= " +(time)).append(",c/s=").append(time == 0 ? count : count/time)
		.append(",closed size=").append(closed.size());
		AStarResult result = AStarResult.newInstance(node, count,time,closed);
		result.setMessage(sb.toString());
		System.out.println(sb.toString());
		logger.info("tryToFindAWay "+srcCell+" ---->>"+targetCell+" is end :::"+sb.toString());
//		List<Entry<Integer, Integer>> entrys = new ArrayList<Map.Entry<Integer,Integer>>(openedSize.entrySet());
//		Collections.sort(entrys,new Comparator<Entry<Integer, Integer>>() {
//
//			@Override
//			public int compare(Entry<Integer, Integer> o1,
//					Entry<Integer, Integer> o2) {
//				return o1.getValue() - o2.getValue();
//			}
//		});
//		for (Entry<Integer, Integer> entry : entrys)
//		{
//			System.out.println(entry.getKey() +"\t"+entry.getValue());
//		}
		return result;
	}
	
	
	Node next;
	
	Node nextNode(Node from,Map<RectCell,Node> closed,Map<RectCell,Node> opened) {
		if (from == null) return null;
		closed.put(from.cell, from);
		opened.remove(from.cell);
		if (from.cell.x == targetCell.x && from.cell.y == targetCell.y){
			return from;//�ҵ�Ŀ���
		}
		//8���������
		//�·�
		next = putOpened(closed,opened,from,1,0);
		//�ҷ�
		Node to = putOpened(closed,opened,from,0,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//�Ϸ�
		to = putOpened(closed,opened,from,-1,0);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//��
		to = putOpened(closed,opened,from,0,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//���·�
		to = putOpened(closed,opened,from,1,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//���·�
		to = putOpened(closed,opened,from,1,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//���·�
		to = putOpened(closed,opened,from,-1,1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//���Ϸ�
		to = putOpened(closed,opened,from,-1,-1);
		if (next == null || (to != null && to.F < next.F)) next = to;
		//ɸѡ����С����F�ĵ�
		next = getMinOpendNode(next,opened);
		if (next == null){
			System.err.println("next go: is null");
			return null;//�ջ��У��޷��ҵ�Ŀ��
		}
		next.cell.type = CellType.CLOSED;
//		System.err.println("next go:"+next.toString());
//		if (frame != null)
//		{
//			frame.repaint();
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}			
//		}
		return next;
		
	}
	
	private Node getMinOpendNode(Node next,Map<RectCell, Node> opened) {
		try {
			int size = opened.values().size();
			int num = 1;
//			if (openedSize.containsKey(size)) num += openedSize.get(size);
//			openedSize.put(size, num);
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
			if (cell == null || cell.isBlock() || closed.containsKey(cell)) return null;
			Node to = new Node(from, cell);
			
			int h = from.calculateG(to);
			int toPosition = getPosition(to.cell.x,to.cell.y);
			int d = values.containsKey(toPosition) ? values.get(toPosition) : 0;
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
		int G;//����㣬���Ų�����·��
		int H;//���������Ǹ������ƶ����յ�Ĺ����ƶ����ѣ������ٷ���:�ӵ�ǰ��Ŀ�ĸ�֮��ˮƽ�ʹ�ֱ�ķ���������ܺͣ����ԶԽ��߷���Ȼ��ѽ������10��|x1-x2|+|y1-y2|(�����ϰ���)
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
		 * ���㵱ǰ�ڵ��Fֵ
		 */
		public void calculateF(Node target) {
			if (parent != null)
				this.G = parent.G + predict(parent);
			this.F = this.G + this.predict(target);
		}

		/*
		 * ���㵱ǰ�ڵ��Gֵ
		 */
		public int calculateG(Node target) {
			return this.G + predict(target);
		}

		/*
		 * ���㵱ǰ�ڵ㵽Ŀ��ڵ���������Ӵ��ۣ�H��
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

	public static void main(String[] args) {
		AStar aStar = new AStar();
		aStar.loadInitDataFromFile();
		aStar.tryToFindAWay();
	}
}
