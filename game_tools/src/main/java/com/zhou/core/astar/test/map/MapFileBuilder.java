package com.zhou.core.astar.test.map;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 地图文件生成
 * @author zhouyongjun
 *
 */
public class MapFileBuilder {
	public static final int BASIC = 5;
	static RectCell[][] cells;
	static int[][] values;
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./map/chengshi.txt")));
			String[] statistics = br.readLine().split("\\|");
			int max_rows = Integer.parseInt(statistics[4]);
			int max_columns = Integer.parseInt(statistics[5]);
			cells = new RectCell[max_rows][max_columns];
			values= new int[max_rows][max_columns];
			String line = null;
			int y=0;
			while((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] datas = line.split("\\|");
				for (int x=0;x<datas.length;x++) {
					try {
						cells[y][x] = new RectCell(new Rectangle((x+1)*BASIC, (y+1)*BASIC, BASIC, BASIC), Byte.parseByte(datas[x]), y, x);
					} catch (Exception e) {
						e.printStackTrace();
					}
					values[y][x] = Integer.MAX_VALUE;
				}
				y++;
			}
			System.out.println("y="+y+",x="+values[0].length);
			writeNewMap();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	private static void writeNewMap() throws Exception {
		PrintWriter pw = new PrintWriter(new File("./map/newmap_blocked.txt"));
		StringBuilder header = new StringBuilder();
		int max_rows = 200;
		int max_columns = 200;
		header.append("||||").append(max_rows).append("|").append(max_columns);
		pw.println(header.toString());
		int blocked_i = 150;
		int blocked_j = 150;
		int blocked_x2 = 1;
		int blocked_y2 = 1;
		int blocked_i2 = 3;
		int blocked_j2 = 1;
		for (int i=0;i<max_rows;i++)
		{
			RectCell[] rcs = null;
			if (i< cells.length)
			{
				rcs = cells[i];
			}
			StringBuilder line = new StringBuilder();
			for (int j=0;j<max_columns;j++)
			{
				byte type = 0;
//				if(rcs != null && j <rcs.length)
//				{
//					type = rcs[j].getType();
//				}
				if ((i == blocked_i || i == blocked_i+1) && j <= blocked_j)
				{
					type = 1;
				}
				if ((j == blocked_j || j == blocked_j+1) && i <= blocked_i)
				{
					type = 1;
				}
				/*
				if (i > 0 && i == blocked_i2  &&  i < blocked_i) {
					if (j> 0  && j < blocked_j) type = 1;
				}*/
				line.append(type).append("|");
			}
			/*if (i >= blocked_i2 && (i- blocked_i2)%2 == 1)
			{
				blocked_i2 +=2;
			}*/
			pw.println(line.deleteCharAt(line.length()-1).toString());
			if (i+1%1000 == 0)
			{
				pw.flush();
			}
		}
		pw.flush();
		pw.close();
	}
}
