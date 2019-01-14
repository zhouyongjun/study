// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SFSArrayLite.java

package com.zhou.util.sfs.data;

import java.util.*;

// Referenced classes of package com.smartfoxserver.v2.entities.data:
//            SFSArray, ISFSArray

public class SFSArrayLite extends SFSArray
{

    public SFSArrayLite()
    {
    }

    public static SFSArrayLite newInstance()
    {
        return new SFSArrayLite();
    }

    public Byte getByte(int index)
    {
        Integer i = super.getInt(index);
        return i == null ? null : Byte.valueOf(i.byteValue());
    }

    public Short getShort(int index)
    {
        Integer i = super.getInt(index);
        return i == null ? null : Short.valueOf(i.shortValue());
    }

    public Float getFloat(int index)
    {
        Double d = super.getDouble(index);
        return d == null ? null : Float.valueOf(d.floatValue());
    }

    public Collection getBoolArray(int key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(arr.getBool(i));

        return data;
    }

    public Collection getShortArray(int key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(Short.valueOf(arr.getInt(i).shortValue()));

        return data;
    }

    public Collection getIntArray(int key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(arr.getInt(i));

        return data;
    }

    public Collection getFloatArray(int key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(Float.valueOf(arr.getDouble(i).floatValue()));

        return data;
    }

    public Collection getDoubleArray(int key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(arr.getDouble(i));

        return data;
    }

    public Collection getUtfStringArray(int key)
    {
        ISFSArray arr = getSFSArray(key);
        if(arr == null)
            return null;
        List data = new ArrayList();
        for(int i = 0; i < arr.size(); i++)
            data.add(arr.getUtfString(i));

        return data;
    }

   /* public static volatile SFSArray newInstance()
    {
        return newInstance();
    }*/
}
