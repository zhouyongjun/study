package com.zhou.core.astar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.zhou.core.astar.RectCell.CellType;


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
			int column=0;
			while((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] datas = line.split("\\|");
				for (int row=0;row<datas.length;row++) {
					try {
						RectCell.newInstance(CellType.valueOf(Byte.parseByte(datas[row])), column, row,true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					values[column][row] = Integer.MAX_VALUE;
				}
				column++;
			}
			System.out.println("column="+column+",row="+values[0].length);
			writeNewMap();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	private static void writeNewMap() throws Exception {
		PrintWriter pw = new PrintWriter(new File("./map/newmap_blocked.txt"));
		StringBuilder header = new StringBuilder();
		int max_rows = 801;
		int max_columns = 801;
		header.append("||||").append(max_rows).append("|").append(max_columns);
		pw.println(header.toString());
		int blocked_i = 750;
		int blocked_j = 750;
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
