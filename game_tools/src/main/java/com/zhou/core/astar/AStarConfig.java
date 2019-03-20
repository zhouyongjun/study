package com.zhou.core.astar;



/**
 * ∫ÛÃ®≈‰÷√
 * @author zhouyongjun
 *
 */
public final class AStarConfig {

	public static int UNION_TERRITORY_THREAD_POOL_NUM = 5;
	public static int MAP_WIDTH = 1024;
	public static int MAP_HEIGHT = 1024;
	
	public static int getPosition(int x,int y){
		return y * MAP_WIDTH + x;
	}
	
	public static int getX(int pos){
		return pos % MAP_WIDTH;
	}
	
	public static int getY(float pos){
		return getY((int)pos);
	}
	
	public static int getY(int pos){
		return pos / MAP_WIDTH;
	}
}
