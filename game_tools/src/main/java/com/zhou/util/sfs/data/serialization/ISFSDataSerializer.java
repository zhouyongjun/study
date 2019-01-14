// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ISFSDataSerializer.java

package com.zhou.util.sfs.data.serialization;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zhou.util.sfs.data.ISFSArray;
import com.zhou.util.sfs.data.ISFSObject;
import com.zhou.util.sfs.data.SFSArray;
import com.zhou.util.sfs.data.SFSObject;

public interface ISFSDataSerializer
{

    public abstract byte[] object2binary(ISFSObject isfsobject);

    public abstract byte[] array2binary(ISFSArray isfsarray);

    public abstract ISFSObject binary2object(byte abyte0[]);

    public abstract ISFSArray binary2array(byte abyte0[]);

    public abstract String object2json(Map<String, Object> map);

    public abstract String array2json(List<Object> list);

    public abstract ISFSObject json2object(String s);

    public abstract ISFSArray json2array(String s);

    public abstract ISFSObject pojo2sfs(Object obj);

    public abstract Object sfs2pojo(ISFSObject isfsobject);

    public abstract SFSObject resultSet2object(ResultSet resultset)
        throws SQLException;

    public abstract SFSArray resultSet2array(ResultSet resultset)
        throws SQLException;
}
