// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ISFSArray.java

package com.zhou.util.sfs.data;

import java.util.Collection;
import java.util.Iterator;

// Referenced classes of package com.smartfoxserver.v2.entities.data:
//            SFSDataWrapper, ISFSObject

public interface ISFSArray
{

    public abstract boolean contains(Object obj);

    public abstract Iterator<SFSDataWrapper> iterator();

    public abstract Object getElementAt(int i);

    public abstract SFSDataWrapper get(int i);

    public abstract void removeElementAt(int i);

    public abstract int size();

    public abstract byte[] toBinary();

    public abstract String toJson();

    public abstract String getHexDump();

    public abstract String getDump();

    public abstract String getDump(boolean flag);

    public abstract void addNull();

    public abstract void addBool(boolean flag);

    public abstract void addByte(byte byte0);

    public abstract void addShort(short word0);

    public abstract void addInt(int i);

    public abstract void addLong(long l);

    public abstract void addFloat(float f);

    public abstract void addDouble(double d);

    public abstract void addUtfString(String s);

    public abstract void addBoolArray(Collection collection);

    public abstract void addByteArray(byte abyte0[]);

    public abstract void addShortArray(Collection collection);

    public abstract void addIntArray(Collection collection);

    public abstract void addLongArray(Collection collection);

    public abstract void addFloatArray(Collection collection);

    public abstract void addDoubleArray(Collection collection);

    public abstract void addUtfStringArray(Collection collection);

    public abstract void addSFSArray(ISFSArray isfsarray);

    public abstract void addSFSObject(ISFSObject isfsobject);

    public abstract void addClass(Object obj);

    public abstract void add(SFSDataWrapper sfsdatawrapper);

    public abstract boolean isNull(int i);

    public abstract Boolean getBool(int i);

    public abstract Byte getByte(int i);

    public abstract Integer getUnsignedByte(int i);

    public abstract Short getShort(int i);

    public abstract Integer getInt(int i);

    public abstract Long getLong(int i);

    public abstract Float getFloat(int i);

    public abstract Double getDouble(int i);

    public abstract String getUtfString(int i);

    public abstract Collection getBoolArray(int i);

    public abstract byte[] getByteArray(int i);

    public abstract Collection getUnsignedByteArray(int i);

    public abstract Collection getShortArray(int i);

    public abstract Collection getIntArray(int i);

    public abstract Collection getLongArray(int i);

    public abstract Collection getFloatArray(int i);

    public abstract Collection getDoubleArray(int i);

    public abstract Collection getUtfStringArray(int i);

    public abstract Object getClass(int i);

    public abstract ISFSArray getSFSArray(int i);

    public abstract ISFSObject getSFSObject(int i);
}
