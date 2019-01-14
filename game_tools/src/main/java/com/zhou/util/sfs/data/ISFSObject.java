// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ISFSObject.java

package com.zhou.util.sfs.data;

import java.util.*;

// Referenced classes of package com.smartfoxserver.v2.entities.data:
//            SFSDataWrapper, ISFSArray

public interface ISFSObject
{

    public abstract boolean isNull(String s);

    public abstract boolean containsKey(String s);

    public abstract boolean removeElement(String s);

    public abstract Set<String> getKeys();

    public abstract int size();

    public abstract Iterator iterator();

    public abstract byte[] toBinary();

    public abstract String toJson();

    public abstract String getDump();

    public abstract String getDump(boolean flag);

    public abstract String getHexDump();

    public abstract SFSDataWrapper get(String s);

    public abstract Boolean getBool(String s);

    public abstract Byte getByte(String s);

    public abstract Integer getUnsignedByte(String s);

    public abstract Short getShort(String s);

    public abstract Integer getInt(String s);

    public abstract Long getLong(String s);

    public abstract Float getFloat(String s);

    public abstract Double getDouble(String s);

    public abstract String getUtfString(String s);

    public abstract Collection getBoolArray(String s);

    public abstract byte[] getByteArray(String s);

    public abstract Collection getUnsignedByteArray(String s);

    public abstract Collection getShortArray(String s);

    public abstract Collection getIntArray(String s);

    public abstract Collection getLongArray(String s);

    public abstract Collection getFloatArray(String s);

    public abstract Collection getDoubleArray(String s);

    public abstract Collection getUtfStringArray(String s);

    public abstract ISFSArray getSFSArray(String s);

    public abstract ISFSObject getSFSObject(String s);

    public abstract Object getClass(String s);

    public abstract void putNull(String s);

    public abstract void putBool(String s, boolean flag);

    public abstract void putByte(String s, byte byte0);

    public abstract void putShort(String s, short word0);

    public abstract void putInt(String s, int i);

    public abstract void putLong(String s, long l);

    public abstract void putFloat(String s, float f);

    public abstract void putDouble(String s, double d);

    public abstract void putUtfString(String s, String s1);

    public abstract void putBoolArray(String s, Collection collection);

    public abstract void putByteArray(String s, byte abyte0[]);

    public abstract void putShortArray(String s, Collection collection);

    public abstract void putIntArray(String s, Collection collection);

    public abstract void putLongArray(String s, Collection collection);

    public abstract void putFloatArray(String s, Collection collection);

    public abstract void putDoubleArray(String s, Collection collection);

    public abstract void putUtfStringArray(String s, Collection collection);

    public abstract void putSFSArray(String s, ISFSArray isfsarray);

    public abstract void putSFSObject(String s, ISFSObject isfsobject);

    public abstract void putClass(String s, Object obj);

    public abstract void put(String s, SFSDataWrapper sfsdatawrapper);
}
