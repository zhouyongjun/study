package com.zhou.core.astar;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhouyongjun
 *
 */
public class AStarUtil {
	public static AStarResult tryToFindAWay(int srcPos,int targetPos,List<Integer> positions) throws Exception
	{
		return tryToFindAWay(srcPos, targetPos, positions, false);
	}
	
	public static AStarResult tryToFindAWay(int srcPos,int targetPos,List<Integer> positions,boolean isNeedFrame) throws Exception
	{
		return tryToFindAWay(srcPos, targetPos, positions, isNeedFrame, AStar.DEFAULT_MAX_LOOP_COUNT);
	}
	
	public static AStarResult tryToFindAWay(int srcPos,int targetPos,List<Integer> positions,boolean isNeedFrame,int max_loop_count) throws Exception
	{
		AStarBuilder builder = new AStarBuilder();
		builder.setSrcPos(srcPos).setTargetPos(targetPos).setPositions(positions)
		.setMax_rows(AStarConfig.MAP_HEIGHT).setMax_columns(AStarConfig.MAP_WIDTH)
		.setNeedFrame(isNeedFrame).setMax_loop_count(max_loop_count);
		final AStar star = builder.build();
		if (isNeedFrame)
		{
			new Thread(new Runnable() {
				@Override
				public void run() {
					star.frame.setVisible(true);
				}
			});
			return null;
		}else
		{
			return star.tryToFindAWay();	
		}
		
	}
	public static void main(String[] args) {
		List<Integer> positions = new ArrayList<Integer>();
		try {
			int row = 801;
			int column = 801;
			for (int i=0;i<=row;i++)
			{
				for (int j=0;j<column;j++)
				{
					positions.add(getPosition(j, i));
				}
			}
			int srcPos = getPosition(1, 1);
			int targetPos = getPosition(680, 680);
			tryToFindAWay(srcPos, targetPos, positions, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int getPosition(int x,int y){
		return y * AStarConfig.MAP_WIDTH + x;
	}
}
