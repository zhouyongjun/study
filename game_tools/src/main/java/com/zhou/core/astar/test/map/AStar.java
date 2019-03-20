package com.zhou.core.astar.test.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A*�㷨
 * @author zhouyongjun
 *
 */
public class AStar {
	public static final int COST_STRAIGHT = 10;//��ֱ�����ˮƽ�����ƶ���·������
	public static final int COST_DIAGONAL = 14;//б�����ƶ���·������
	Scene2DMap map;//��ͼ
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
		//���Ϸ�
		Node node = newNode(srcNode,posNode,-1,-1,COST_DIAGONAL);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//���·�
		node = newNode(srcNode,posNode,-1,1,COST_DIAGONAL);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//���Ϸ�
		node = newNode(srcNode,posNode,1,-1,COST_DIAGONAL);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//���·�
		node = newNode(srcNode,posNode,1,1,COST_DIAGONAL);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//����
		node = newNode(srcNode,posNode,-1,0,COST_STRAIGHT);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//���ҷ�
		node = newNode(srcNode,posNode,1,0,COST_STRAIGHT);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//���Ϸ�
		node = newNode(srcNode,posNode,0,-1,COST_STRAIGHT);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		//���·�
		node = newNode(srcNode,posNode,0,1,COST_STRAIGHT);
		if (node != null && posNode.x == node.x && posNode.y == node.y) {
			return node;
		}
		node = getMinFNode();
		if (node == null) {
			return null;
		}
		if (node != null) {//����˽ڵ��Լ�����
			delOpenNode(node);//�Ӵ�LISTɾ��
			addCloseNode(node);//��ӵ��ر�List
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
	 * ����һ���ڵ�
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
		if (x < 1 || x > map.getMax_width() || y < 1 || y > map.getMax_hight()) {//�Ƿ�������
			return null;
		}
		RectCell cell = map.getRectCell(x, y);
		if (cell == null || cell.isFill()) { //�赲
			return null;
		}
		//����һ���½ڵ�
		Node node = new Node(x, y, srcNode);
		//������ص�ֵ
		node.calc(g + srcNode.G,(Math.abs(x-posNode.x) + Math.abs(y - posNode.y)) * COST_STRAIGHT);
		//�ж��Ƿ�Ϊ�յ�����
		if (posNode.x == x && posNode.y == y) {
			return node;
		}
		//�鿴��ʼLIST���Ƿ����
		Node openNode = getOpenNode(x, y);
		if (openNode != null) {//����
			if (node.G < openNode.G) {
				openNode.superNode = srcNode;
				openNode.calc(node.G,node.H);
				return openNode;
			}else {
				return null;	
			}
		}
		//�鿴����LIST�Ƿ����
		if (getCloseNode(x,y) != null) {
			return null;
		}
		//��ӵ���ʼLIST
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
 * �ڵ�
 * @author zhouyongjun
 *
 */
class Node {
	Node superNode;
	int G;//����㣬���Ų�����·��
	int H;//���������Ǹ������ƶ����յ�Ĺ����ƶ����ѣ������ٷ���:�ӵ�ǰ��Ŀ�ĸ�֮��ˮƽ�ʹ�ֱ�ķ���������ܺͣ����ԶԽ��߷���Ȼ��ѽ������10��|x1-x2|+|y1-y2|(�����ϰ���)
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
