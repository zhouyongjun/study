package com.zhou.core.astar.test.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A*算法
 * @author zhouyongjun
 *
 */
public class AStar {
	public static final int COST_STRAIGHT = 10;//垂直方向或水平方向移动的路径评分
	public static final int COST_DIAGONAL = 14;//斜方向移动的路径评分
	Scene2DMap map;//地图
	List<Node> openNodes;
	List<Node> closeNodes;
	Graphics2D g2D;
	public AStar(Scene2DMap map,Graphics2D g2D) {
		this.map = map;
		this.g2D = g2D;
		openNodes = new ArrayList<Node>(128);
		closeNodes = new ArrayList<Node>(128);
	}
	public Node find_path(int srcX,int srcY,int posX,int posY) {
		if (srcX < 1 || srcY > map.rects.length || srcY < 1 || srcY > map.rects[0].length) {
			return null;
		}
		if (posX < 1 || posX > map.rects.length || posY < 1 || posY > map.rects[0].length) {
			return null;
		}
		Node srcNode = new Node(srcX, srcY, null);
	    Node posNode = new Node(posX, posY, null);
	    addCloseNode(srcNode);
	    Node node = search(srcNode, posNode);
//	    while (node != null) {
//	    	RectCell cell = map.getRectCell(node.x, node.y);
//	    	g2D.setColor(Color.GREEN);
//	    	g2D.fill(cell.rect);
//	    	node = node.superNode;
//	    }
	    return node;
	}

	private Node search(Node srcNode, Node posNode) {
		//左上方
		Node node = newNode(srcNode,posNode,-1,-1,COST_DIAGONAL);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//左下方
		node = newNode(srcNode,posNode,-1,1,COST_DIAGONAL);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//右上方
		node = newNode(srcNode,posNode,1,-1,COST_DIAGONAL);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//右下方
		node = newNode(srcNode,posNode,1,1,COST_DIAGONAL);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//正左方
		node = newNode(srcNode,posNode,-1,0,COST_STRAIGHT);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//正右方
		node = newNode(srcNode,posNode,1,0,COST_STRAIGHT);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//正上方
		node = newNode(srcNode,posNode,0,-1,COST_STRAIGHT);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//正下方
		node = newNode(srcNode,posNode,0,1,COST_STRAIGHT);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		node = getMinFNode();
		if (node == null) {
			return null;
		}
		if (node != null) {//如果此节点以及存在
			delOpenNode(node);//从打开LIST删除
			addCloseNode(node);//添加到关闭List
			for (Node test : closeNodes) {
				g2D.setColor(Color.RED);
				RectCell cell = map.getRectCell(test.x, test.y);
				if (cell != null) {
					g2D.draw(cell.rect);					
				}else {
					System.out.println(" not cell node : x=" + test.x +",y="+ test.y +",G="+test.G+",H="+test.H+",F="+test.F);
				}
			}
		}
		
		return search(node, posNode);
	}

	private Node getMinFNode() {
		Collections.sort(openNodes,new Comparator<Node>() {
			@Override
			public int compare(Node arg0, Node arg1) {
				return arg0.F - arg1.F;
			}
		});
		return openNodes.size() > 0 ? openNodes.get(0) : null;
	}

	private void delOpenNode(Node node) {
		openNodes.remove(node);	
	}
	/**
	 * 创建一个节点
	 * @param srcNode
	 * @param posNode
	 * @param deviantX
	 * @param deviantY
	 * @param g
	 * @return
	 */
	private Node newNode(Node srcNode,Node posNode, int deviantX, int deviantY,int g) {
		int x = srcNode.x + deviantX;
		int y = srcNode.y + deviantY;
		if (x < 1 || x > map.getMax_width() || y < 1 || y > map.getMax_hight()) {//非法的坐标
			return null;
		}
		RectCell cell = map.getRectCell(x, y);
		if (cell == null || cell.isFill()) { //阻挡
			return null;
		}
		//创建一个新节点
		Node node = new Node(x, y, srcNode);
		//计算相关的值
		node.calc(g + srcNode.G,(Math.abs(x-posNode.x) + Math.abs(y - posNode.y)) * COST_STRAIGHT);
		//判断是否为终点坐标
		if (posNode.x == x && posNode.y == y) {
			return node;
		}
		//查看开始LIST中是否存在
		Node openNode = getOpenNode(x, y);
		if (openNode != null) {//更新
			if (node.G < openNode.G) {
				openNode.superNode = srcNode;
				openNode.calc(node.G,node.H);
				return openNode;
			}else {
				return null;	
			}
		}
		//查看结束LIST是否存在
		if (getCloseNode(x,y) != null) {
			return null;
		}
		//添加到开始LIST
		addOpenNode(node);
		return node;
	}

	private void addCloseNode(Node node) {
		closeNodes.add(node);
	}

	private void addOpenNode(Node node) {
		openNodes.add(node);
	}

	private Node getCloseNode(int x, int y) {
		for (Node node : closeNodes) {
			if (node.x == x && node.y == y) {
				return node;
			}
		}
		return null;
	}

	private Node getOpenNode(int x, int y) {
		for (Node node : openNodes) {
			if (node.x == x && node.y == y) {
				return node;
			}
		}
		return null;
	}
}
/**
 * 节点
 * @author zhouyongjun
 *
 */
class Node {
	Node superNode;
	int G;//从起点，沿着产生的路径
	int H;//从网格上那个方向移动到终点的估计移动消费，曼哈顿方法:从当前格到目的格之间水平和垂直的方格的数量总和，忽略对角线方向。然后把结果乘以10。|x1-x2|+|y1-y2|(忽略障碍物)
	int F;//G+H
	int x;
	int y;
	public Node(int x,int y,Node superNode) {
		this.x = x;
		this.y = y;
		this.superNode = superNode;
	}
	public void calc(int g, int h) {
		this.G = g;
		this.H = h;
		this.F = g+h;		
	}
}
