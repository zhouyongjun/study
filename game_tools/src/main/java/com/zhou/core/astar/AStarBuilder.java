package com.zhou.core.astar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhou.core.astar.AStar.Node;
import com.zhou.core.astar.RectCell.CellType;

public class AStarBuilder {
	int srcPos = 0;
	int targetPos  = 0;
	boolean isNeedFrame;
	List<Integer> positions;//点集合
	int max_rows = 100;
	int max_columns = 100;
	int max_loop_count= AStar.DEFAULT_MAX_LOOP_COUNT;//循环次数
	AStar build() throws Exception
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
		if (!positions.contains(targetPos))
		{
			throw new Exception("error: positions not contains targetPost");
		}
		AStar aStar = new AStar();
		aStar.max_columns = max_rows;
		aStar.max_rows = max_columns;
		aStar.srcCell = RectCell.newInstance(CellType.SPACE, aStar.getX(srcPos),aStar.getY(srcPos),isNeedFrame);
		aStar.targetCell = RectCell.newInstance(CellType.SPACE, aStar.getX(targetPos),aStar.getY(targetPos),isNeedFrame);
		aStar.targetNode = new Node(null, aStar.targetCell);
		if (isNeedFrame)
		{
			aStar.frame = new AStarFrame();
			aStar.frame.setAStar(aStar);
		}
		_buildPositions(aStar);
		aStar.init();
		return aStar;
	}

	private void _buildPositions(AStar aStar) {
		Map<Integer,RectCell> cells = new HashMap<Integer, RectCell>(); 
		Map<Integer,Integer> values = new HashMap<Integer, Integer>();
		for (Integer position : positions) {
			cells.put(position,RectCell.newInstance(RectCell.CellType.SPACE,aStar.getX(position),aStar.getY(position),isNeedFrame));
			values.put(position,Integer.MAX_VALUE);
		}
		aStar.cells = cells;
		aStar.values = values;
	}


	public AStarBuilder setSrcPos(int srcPos) {
		this.srcPos = srcPos;
		return this;
	}

	public AStarBuilder setTargetPos(int targetPos) {
		this.targetPos = targetPos;
		return this;
	}

	public AStarBuilder setNeedFrame(boolean isNeedFrame) {
		this.isNeedFrame = isNeedFrame;
		return this;
	}
	
	public AStarBuilder setPositions(List<Integer> positions) {
		this.positions = positions;
		return this;
	}
	
	public AStarBuilder setMax_columns(int max_columns) {
		this.max_columns = max_columns;
		return this;
	}
	public AStarBuilder setMax_rows(int max_rows) {
		this.max_rows = max_rows;
		return this;
	}
	public AStarBuilder setMax_loop_count(int max_loop_count) {
		this.max_loop_count = max_loop_count;
		return this;
	}
}
