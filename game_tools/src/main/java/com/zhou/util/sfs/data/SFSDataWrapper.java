// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SFSDataWrapper.java

package com.zhou.util.sfs.data;


// Referenced classes of package com.smartfoxserver.v2.entities.data:
//            SFSDataType

public class SFSDataWrapper
{

    public SFSDataWrapper(SFSDataType typeId, Object object)
    {
        this.typeId = typeId;
        this.object = object;
    }

    public SFSDataType getTypeId()
    {
        return typeId;
    }

    public Object getObject()
    {
        return object;
    }

    private SFSDataType typeId;
    private Object object;
}
