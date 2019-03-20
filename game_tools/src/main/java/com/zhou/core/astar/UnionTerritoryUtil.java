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
 * ͬ�� ���
 * @author zhouyongjun
 *
 */
public class UnionTerritoryUtil {
	private static final Logger logger = LoggerFactory.getLogger(AStar.class);
	//�̳߳�
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
	 * ִ�б��ݻٵ� A*�㷨��ѯ
	 * @param occupyPositions
	 * @param destroyedPos
	 */
	public static void executeAstar(final List<Integer> occupyPositions/*Alliance alliance*/,final int destroyedPos)
	{
		logger.error("executeAstar destroyedPos="+destroyedPos +" is begin...");
		//ɾ�����ݻٵ�
		try {
			synchronized (occupyPositions) {//��������֤ÿ��ͬ��ͬһʱ���ֻ�ܴ���һ�����ݻٵ�
				occupyPositions.remove(Integer.valueOf(destroyedPos));
				int destroyedX = AStarConfig.getX(destroyedPos);
				int destroyedY = AStarConfig.getY(destroyedPos);
				//ɸѡ���������������ĵ�
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
				//ɸѡ���������ӵĵ㼯��
				Collection<Set<Integer>> lines = filterPositionLines(list);
				//���������·<=1���Ļ���˵���˵㱻�ݻٲ�Ӱ�����ܵ������򵽴˽���
				//			if (lines.size() <= 1) return;
				int targetPos = AStarConfig.getPosition(299, 280);
				for (Set<Integer> set : lines) {
					if (set.isEmpty())
						continue;
					int srcPos = set.iterator().next();
					try {
						AStarResult result = AStarUtil.tryToFindAWay(srcPos,targetPos, occupyPositions,true);
						///Ѱ·�ɹ�������
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
	 * ɸѡ�������ݻٵ� ���� �����ĵ㼯��
	 * @param positions
	 * @return �ܹ����ߵĵ㼯��  Set<Integer> ��λ�������ӳ��ߵĵ㣬Collection<Set<Integer>>����ʾ�ָ�ɶ����
	 */
	public static Collection<Set<Integer>> filterPositionLines(List<Integer> positions) {
		int sign = 1;//��·��ʶ
		Map<Integer,Set<Integer>> signs = new HashMap<>();
		//������ݻٵ����� �� >=7 ���㼯�ϣ�������ɸѡ���Ϳ�������
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
	 * ɸѡ ָ������Χ���ĸ�����ĵ�
	 * @param positions ���������ĵ�
	 * @param pos ָ����
	 * @param lines �������ߵĵ㼯��
	 */
	public static void filterRoundsPosition(final List<Integer> positions, Integer pos,Set<Integer> lines) {
		//��ǰ��XY����
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
			//�ݹ����
			filterRoundsPosition(positions, round, lines);
		}
	}

	private static List<Integer> getRounds(int x, int y,List<Integer> list) {
		List<Integer> rounds = new ArrayList<Integer>();
		int l_pos = AStarConfig.getPosition(x-1, y);//����
		int r_Pos =  AStarConfig.getPosition(x+1, y);//���ҷ�
		int u_Pos =  AStarConfig.getPosition(x	, y-1);//���Ϸ�
		int d_Pos =  AStarConfig.getPosition(x	, y+1);//���·�
		
		int ul_pos = AStarConfig.getPosition(x-1, y-1);//���Ϸ�
		int ur_Pos =  AStarConfig.getPosition(x+1, y-1);//���Ϸ�
		int dl_Pos =  AStarConfig.getPosition(x-1, y+1);//���·�
		int dr_Pos =  AStarConfig.getPosition(x+1, y+1);//���·�
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
