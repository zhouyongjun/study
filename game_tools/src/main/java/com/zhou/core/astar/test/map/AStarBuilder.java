package com.zhou.core.astar.test.map;

import java.util.List;

import javax.swing.JFrame;

public class AStarBuilder {
	int srcPos;
	int targetPost;
	JFrame frame;//是否图片显示
	List<Integer> positions;//点集合
	
	public AStar_V4 build() throws Exception
	{
		if (srcPos <= 0) 
		{
			throw new Exception("error: scrPos["+srcPos+"] <=0.");
		}
		if (positions == null || positions.isEmpty())
		{
			throw new Exception("error: positions == null or size == 0");
		}
		if (!positions.contains(srcPos))
		{
			throw new Exception("error: positions not contains srcPos");
		}
		if (!positions.contains(targetPost))
		{
			throw new Exception("error: positions not contains targetPost");
		}
		AStar_V4 aStar = new AStar_V4(frame);
//		aStar.srcCell = RectCell.newInstance(type, x, y);
		return aStar;
	}

	public AStarBuilder setSrcPos(int srcPos) {
		this.srcPos = srcPos;
		return this;
	}

	public AStarBuilder setTargetPost(int targetPost) {
		this.targetPost = targetPost;
		return this;
	}

	public AStarBuilder setFrame(JFrame frame) {
		this.frame = frame;
		return this;
	}

	public AStarBuilder setPositions(List<Integer> positions) {
		this.positions = positions;
		return this;
	}
}
