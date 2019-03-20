package com.zhou.core.astar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 同盟 领地
 * @author zhouyongjun
 *
 */
public class UnionTerritoryUtil {
	private static final Logger logger = LoggerFactory.getLogger(AStar.class);
	//线程池
	static ScheduledExecutorService service = Executors.newScheduledThreadPool(AStarConfig.UNION_TERRITORY_THREAD_POOL_NUM
			,new ThreadFactoryBuilder().setNameFormat("union_territory_%d").build());
	
	
	public static Future<?> scheduleAstar(final List<Integer> occupyPositions/*Alliance alliance*/,final int destroyedPos)
	{
		return service.submit(new Runnable() {
			@Override
			public void run() {
				executeAstar(occupyPositions, destroyedPos);
			}
		});
	}
	/**
	 * 执行被摧毁点 A*算法查询
	 * @param occupyPositions
	 * @param destroyedPos
	 */
	public static void executeAstar(final List<Integer> occupyPositions/*Alliance alliance*/,final int destroyedPos)
	{
		logger.error("executeAstar destroyedPos="+destroyedPos +" is begin...");
		//删除被摧毁点
		try {
			synchronized (occupyPositions) {//加锁，保证每个同盟同一时间点只能处理一个被摧毁点
				occupyPositions.remove(Integer.valueOf(destroyedPos));
				int destroyedX = AStarConfig.getX(destroyedPos);
				int destroyedY = AStarConfig.getY(destroyedPos);
				//筛选出四周满足条件的点
				List<Integer> list = new ArrayList<Integer>();
				for (int i = destroyedX - 1; i <= destroyedX + 1; i++) {
					for (int j = destroyedY - 1; j <= destroyedY + 1; j++) {
						int pos = AStarConfig.getPosition(i, j);
						if (pos == destroyedPos
								|| !occupyPositions.contains(pos))
							continue;
						list.add(pos);
					}
				}
				//筛选出符合连接的点集合
				Collection<Set<Integer>> lines = filterPositionLines(list);
				//如果最终线路<=1条的话，说明此点被摧毁不影响四周的链接则到此结束
				//			if (lines.size() <= 1) return;
				int targetPos = AStarConfig.getPosition(299, 280);
				for (Set<Integer> set : lines) {
					if (set.isEmpty())
						continue;
					int srcPos = set.iterator().next();
					try {
						AStarResult result = AStarUtil.tryToFindAWay(srcPos,targetPos, occupyPositions,true);
						///寻路成功则跳过
						if (result == null || result.isSucc())
							continue;
					} catch (Exception e) {
						logger.error("executeAstar srcPos=" + srcPos
								+ " --> targetPos=" + targetPos + " is error.",
								e);
					}
				}
			}
		} finally
		{
			logger.error("executeAstar destroyedPos="+destroyedPos +" is end...");
		}
		
	}
	/**
	 * 筛选出被被摧毁点 四周 相连的点集合
	 * @param positions
	 * @return 能够连线的点集合  Set<Integer> 表位可以连接成线的点，Collection<Set<Integer>>：表示分割成多个点
	 */
	public static Collection<Set<Integer>> filterPositionLines(List<Integer> positions) {
		int sign = 1;//线路标识
		Map<Integer,Set<Integer>> signs = new HashMap<>();
		//如果被摧毁点四周 有 >=7 个点集合，则无需筛选，就可以满足
		if (positions.size()>=7) {
			signs.put(sign, new HashSet<>(positions));
			return signs.values();
		}
	
		for (Integer pos : positions)
		{
			Set<Integer> lines = null;
			for (Set<Integer> set : signs.values())
			{
				if (set.contains(pos)) lines = set;
			}
			if (lines == null)
			{
				lines = new HashSet<>();
				signs.put(sign++, lines); 
			}
			lines.add(pos);
			filterRoundsPosition(positions,pos,lines);
		}
		StringBuilder  sb = new StringBuilder();
		for (Entry<Integer,Set<Integer>> entry : signs.entrySet())
		{
			sb.append(entry.getKey()).append(" ");
			List<Integer> l = new ArrayList<Integer>(entry.getValue());
			Collections.sort(l);
			sb.append("size=").append(l.size()).append(" ");
			for (Integer pos : l)
			{
				sb.append(pos).append("[").append(AStarConfig.getX(pos)).append(",")
				.append(AStarConfig.getY(pos)).append("],");
			}
			
			sb.append("\n");
			}
		System.out.println("\t\n");	
		System.out.println(sb.toString());	
		return signs.values();
	}
	
	/**
	 * 筛选 指定点周围的四个方向的点
	 * @param positions 符合条件的点
	 * @param pos 指定点
	 * @param lines 可以连线的点集合
	 */
	public static void filterRoundsPosition(final List<Integer> positions, Integer pos,Set<Integer> lines) {
		//当前点XY坐标
		int x  = AStarConfig.getX(pos);
		int y  = AStarConfig.getY(pos);
		List<Integer> rounds = getRounds(x,y,positions);
		for (Integer round: rounds)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(pos).append("[").append(AStarConfig.getX(pos)).append(",")
			.append(AStarConfig.getY(pos)).append("] ---> ").append(round).append("[").append(AStarConfig.getX(round)).append(",")
			.append(AStarConfig.getY(round)).append("] ").append(lines);
			System.err.println(sb.toString());
			if (lines.contains(round)) continue;
			lines.add(round);
			//递归调用
			filterRoundsPosition(positions, round, lines);
		}
	}

	private static List<Integer> getRounds(int x, int y,List<Integer> list) {
		List<Integer> rounds = new ArrayList<Integer>();
		int l_pos = AStarConfig.getPosition(x-1, y);//正左方
		int r_Pos =  AStarConfig.getPosition(x+1, y);//正右方
		int u_Pos =  AStarConfig.getPosition(x	, y-1);//正上方
		int d_Pos =  AStarConfig.getPosition(x	, y+1);//正下方
		
		int ul_pos = AStarConfig.getPosition(x-1, y-1);//左上方
		int ur_Pos =  AStarConfig.getPosition(x+1, y-1);//右上方
		int dl_Pos =  AStarConfig.getPosition(x-1, y+1);//左下方
		int dr_Pos =  AStarConfig.getPosition(x+1, y+1);//左下方
		if (list.contains(l_pos)) rounds.add(l_pos);
		if (list.contains(u_Pos)) rounds.add(u_Pos);
		if (list.contains(r_Pos)) rounds.add(r_Pos);
		if (list.contains(d_Pos)) rounds.add(d_Pos);
		if (list.contains(ul_pos)) rounds.add(ul_pos);
		if (list.contains(ur_Pos)) rounds.add(ur_Pos);
		if (list.contains(dl_Pos)) rounds.add(dl_Pos);
		if (list.contains(dr_Pos)) rounds.add(dr_Pos);
		return rounds;
	}

	public static void main(String[] args) {
		int x  = 10;
		int y  = 10; 
		List<Integer> occupyPositions  = new ArrayList<>();
		occupyPositions.add(AStarConfig.getPosition(9,9));
		occupyPositions.add(AStarConfig.getPosition(11,9));
//		occupyPositions.add(AStarConfig.getPosition(10,11));
		
		occupyPositions.add(AStarConfig.getPosition(11,10));
		occupyPositions.add(AStarConfig.getPosition(10,9));
		occupyPositions.add(AStarConfig.getPosition(9,11));
		for (int i=12;i<=300;i++)
		{
			for (int j=9;j<=300;j++)
			{
				occupyPositions.add(AStarConfig.getPosition(i,j));
			}
		}
		long time = System.currentTimeMillis();
		scheduleAstar(occupyPositions, AStarConfig.getPosition(x,y));
		System.out.println("millis:"+(System.currentTimeMillis() - time));
	}
}
