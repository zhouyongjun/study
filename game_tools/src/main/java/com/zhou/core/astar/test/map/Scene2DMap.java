package com.zhou.core.astar.test.map;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Scene2DMap {
	private String sceneEn;
	private int[] base_lower_left_point;//原始的最左下角坐标
	private int[] base_top_right_point;//原始的最右上角坐标
	private int[] lower_left_point;//校正后的左下角坐标
	private int[] top_right_point;//校正后的右上角坐标
	private int row;//行数
	private int column;//列数
	private int space_size;//间隔大小(扩展后的)
	private int adjust_x;//校正X值
	private int adjust_y;//校正Y值
	RectCell[][] rects;//地图单元数组
	int rate = 6;
	public void load(File file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = br.readLine();
			decodeSceneLength(line.split("\\|"));
			List<RectCell[]> list = new ArrayList<>();
			StringBuffer sb = new StringBuffer();
			int row = 1;
			while ((line = br.readLine()) != null) {
				list.add(decodeRectangle(line.split("\\|"),row++,sb));
			}
			rects = list.toArray(new RectCell[list.size()][]);
			System.out.println("row:"+row);
			PrintWriter pw = new PrintWriter(new File("points.txt"));
			pw.println(sb.toString());
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	private RectCell[] decodeRectangle(String[] splits, int row,StringBuffer allSb) {
		int x = (int) (lower_left_point[0] + (row -1) * space_size);
		RectCell[] rects = new RectCell[splits.length];
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<rects.length;i++) {
			int y = lower_left_point[1] +  i * space_size;
			rects[i] = new RectCell(new Rectangle(x, y, space_size, space_size),Byte.parseByte(splits[i]),row,i+1);
			sb.append("(").append(x).append(",").append(y).append(")").append("|");
		}
//		System.out.println(sb.toString());
		allSb.append(sb.toString()).append("\n");
		return rects;
	}

	public float getTrans_X(float x) {
		return x * rate - adjust_x;
	}
	public float getTrans_Y(float y) {
		return y * rate - adjust_y;
	}
	
	private void decodeSceneLength(String[] datas) {
		int i= 0;
		int lower_x = Integer.parseInt(datas[i++]) * rate;
		int lower_y = Integer.parseInt(datas[i++]) * rate;
		int top_x = Integer.parseInt(datas[i++])  * rate;
		int top_y = Integer.parseInt(datas[i++])  * rate;
		base_lower_left_point = new int[] {lower_x,lower_y};
		base_top_right_point = new int[] {top_x,top_y};
		row = Integer.parseInt(datas[i++])*rate;
		column = Integer.parseInt(datas[i++])*rate;
		space_size = Integer.parseInt(datas[i++])*rate;
		adjust_x = lower_x - 25;
		adjust_y = lower_y - 25;
		lower_left_point = new int[]{lower_x- adjust_x,lower_y - adjust_y};
		top_right_point = new int[]{top_x-adjust_x ,top_y  - adjust_y};
		System.out.println("rate:("+rate+")");
		System.out.println("base_lower_left_point:("+base_lower_left_point[0]+","+base_lower_left_point[1]+")");
		System.out.println("base_top_right_point:("+base_top_right_point[0]+","+base_top_right_point[1]+")");
		System.out.println("max_width:("+row+")");
		System.out.println("max_hight:("+column+")");
		System.out.println("space_size:("+space_size+")");
		System.out.println("adjust:("+adjust_x+","+adjust_y+")");
		System.out.println("lower_left_point:("+lower_left_point[0]+","+lower_left_point[1]+")");
		System.out.println("top_right_point:("+top_right_point[0]+","+top_right_point[1]+")");
	}

	public String getSceneEn() {
		return sceneEn;
	}

	public int[] getLower_left_point() {
		return lower_left_point;
	}

	public int[] getTop_right_point() {
		return top_right_point;
	}

	public int getMax_width() {
		return row;
	}

	public int getMax_hight() {
		return column;
	}

	public int getSpace_size() {
		return space_size;
	}

	public RectCell[][] getRects() {
		return rects;
	}
	
	public RectCell getFillRectangle(float x,float y) {
		for (RectCell[] values : rects) {
			for (RectCell rect : values) {
				if (rect.getRect().contains(x, y)) {
					return rect;
				}
			}
		}
		return null;
	}
	
	public RectCell getRectCell(int row,int column) {
		if (row < 1 || row > rects.length || column < 1 || column > rects[0].length) {
			return null;
		}
		return rects[row-1][column-1];
	}


	public int[] getBase_lower_left_point() {
		return base_lower_left_point;
	}

	public int[] getBase_top_right_point() {
		return base_top_right_point;
	}

	public int getAdjust_x() {
		return adjust_x;
	}

	public int getAdjust_y() {
		return adjust_y;
	}

	public int getRate() {
		return rate;
	}
}
