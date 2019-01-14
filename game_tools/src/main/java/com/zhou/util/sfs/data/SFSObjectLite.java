// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SFSObjectLite.java

package com.zhou.util.sfs.data;

import java.util.*;

// Referenced classes of package com.smartfoxserver.v2.entities.data:
//            SFSObject, ISFSArray

public final class SFSObjectLite extends SFSObject
{

    public SFSObjectLite()
    {
    }

    public static SFSObject newInstance()
    {
        return new SFSObjectLite();
    }

    public Byte getByte(String key)
    {
        Integer i = super.getInt(key);
        return i == null ? null : Byte.valueOf(i.byteValue());
    }

    public Short getShort(String key)
    {
        Integer i = super.getInt(key);
        return i == null ? null : Short.valueOf(i.shortValue());
    }

    public Float getFloat(String key)
    {
        Double d = super.getDouble(key);
        return d == null ? null : Float.valueOf(d.floatValue());
    }

    public Collection getBoolArray(String key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(arr.getBool(i));

        return data;
    }

    public Collection getShortArray(String key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(Short.valueOf(arr.getInt(i).shortValue()));

        return data;
    }

    public Collection getIntArray(String key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(arr.getInt(i));

        return data;
    }

    public Collection getFloatArray(String key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(Float.valueOf(arr.getDouble(i).floatValue()));

        return data;
    }

    public Collection getDoubleArray(String key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(arr.getDouble(i));

        return data;
    }

    public Collection getUtfStringArray(String key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(arr.getUtfString(i));

        return data;
    }
}
