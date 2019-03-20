package com.zhou.core.astar.test.map;

import java.awt.Rectangle;
	/**
	 * 矩形单元
	 * @author zhouyongjun
	 *
	 */
public class RectCell {
	Rectangle rect;
	byte initType;
	byte type;//0:空，1：阻挡，2：可跳跃
	int y;
	int x;
	public RectCell(Rectangle rectangle,byte type,int y,int x) {
		this.rect = rectangle;
		this.type = type;
		this.initType = type;
		this.y = y;
		this.x = x;
	}
	
	public boolean isFill() {
		return type == 1;
	}
	
	public boolean isJump() {
		return type == 2;
	}
	
	public boolean isNull() {
		return type == 0;
	}
	public Rectangle getRect() {
		return rect;
	}
	public byte getType() {
		return type;
	}
	
	@Override
		public String toString() {
			return "Cell["+type+",("+x+","+y+")("+rect.width+","+rect.height+")]";
		}
	
	public void resest()
	{
		type = initType;
	}

	public static RectCell newInstance(int type,int x, int y) {
		RectCell cell = new RectCell(new Rectangle((y+1)*AStar_V3.BASIC, (x+1)*AStar_V3.BASIC, AStar_V3.BASIC, AStar_V3.BASIC), (byte)type, x, y);
		return cell;
	}
}
