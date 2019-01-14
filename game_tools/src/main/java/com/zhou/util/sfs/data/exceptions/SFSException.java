// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SFSException.java

package com.zhou.util.sfs.data.exceptions;


// Referenced classes of package com.smartfoxserver.v2.exceptions:
//            SFSErrorData

public class SFSException extends Exception
{
    public SFSException()
    {
    }


    public SFSException(String message)
    {
        super(message);
    }

    public SFSException(Throwable t)
    {
        super(t);
    }

}
