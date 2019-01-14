// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SFSRuntimeException.java

package com.zhou.util.sfs.data.exceptions;


public class SFSRuntimeException extends RuntimeException
{

    public SFSRuntimeException()
    {
    }

    public SFSRuntimeException(String message)
    {
        super(message);
    }

    public SFSRuntimeException(Throwable t)
    {
        super(t);
    }
}
