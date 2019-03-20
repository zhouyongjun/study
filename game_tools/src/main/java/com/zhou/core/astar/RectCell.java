package com.zhou.core.astar;

import java.awt.Rectangle;

public class RectCell {
	public static int BASIC = 4;
	public static int BASIC_X = BASIC;
	public static int BASIC_Y = BASIC;
	Rectangle rect;
	CellType initType;
	CellType type;//0:�գ�1���赲��2������Ծ
	int y;
	int x;
	public static enum CellType
	{
		SPACE,//�յ� 0 
		BLOCK,//�赲   1
		JUMP,//����Ծ 2
		CLOSED,//���ҹ� 
		TARGET,//Ŀ��·��
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
