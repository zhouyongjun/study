package com.zhou.util.sfs.data;

public class Test {
	public static void main(String[] args) {
		ISFSObject o = SFSObject.newInstance();
			o.putUtfString("t", "test-str");
			o.putInt("i", 1);
			o.putBool("b", true);
			o.putDouble("d", 0.5d);
		ISFSObject s = SFSObject.newInstance();
			s.putShort("s-s", (short) 12);
			s.putUtfString("s--s", "sfsfsd");
//			o.putSFSObject("putSFSObject", s);
		ISFSArray array = SFSArray.newInstance();
//			array.addBool(false);
//			array.addBool(true);
			array.addSFSObject(s);
			array.addSFSObject(s);
			array.addSFSObject(s);
//			o.putSFSArray("array", array);
			System.out.println(o.toJson());
			System.out.println(o.toBinary());
			System.out.println(SFSObject.newFromBinaryData(o.toBinary()).toJson());
	}
}
