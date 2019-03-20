package com.zhou.core.astar;

import java.awt.Rectangle;

public class RectCell {
	public static int BASIC = 4;
	public static int BASIC_X = BASIC;
	public static int BASIC_Y = BASIC;
	Rectangle rect;
	CellType initType;
	CellType type;//0:空，1：阻挡，2：可跳跃
	int y;
	int x;
	public static enum CellType
	{
		SPACE,//空地 0 
		BLOCK,//阻挡   1
		JUMP,//可跳跃 2
		CLOSED,//已找过 
		TARGET,//目标路线
		;
		public static CellType valueOf(int index)
		{
			return values()[index];
		}
	}
	public RectCell(CellType type,int x,int y) {
		this(type, x, y, false);
	}
	public RectCell(CellType type,int x,int y,boolean isNeedFrame) {
		this.type = type;
		this.initType = type;
		this.y = y;
		this.x = x;
		if (isNeedFrame)
		{
			this.rect = new Rectangle((x+1)* BASIC, (y+1)* BASIC, BASIC, BASIC);	
		}
	}
	
	public boolean isBlock() {
		return type == CellType.BLOCK;
	}
	
	public boolean isJump() {
		return type == CellType.JUMP;
	}
	
	public boolean isSpace() {
		return type == CellType.SPACE;
	}
	public boolean isClosed() {
		return type == CellType.CLOSED;
	}
	public boolean isTarget() {
		return type == CellType.TARGET;
	}
	public Rectangle getRect() {
		return rect;
	}
	
	
	@Override
		public String toString() {
			return "Cell["+type+",("+x+","+y+")("
					+ (rect !=null ? rect.width:-1)+","
					+ (rect !=null ? rect.height:-1)+")]";
		}
	
	public void resest()
	{
		type = initType;
	}

	public static RectCell newInstance(CellType type,int x, int y,boolean isNeedFrame) {
		RectCell cell = new RectCell(type, x, y,isNeedFrame);
		return cell;
	}
}
