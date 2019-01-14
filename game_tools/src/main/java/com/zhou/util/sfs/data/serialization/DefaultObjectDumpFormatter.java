// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DefaultObjectDumpFormatter.java

package com.zhou.util.sfs.data.serialization;

import java.util.Arrays;

public class DefaultObjectDumpFormatter
{

    public DefaultObjectDumpFormatter()
    {
    }

    public static String prettyPrintByteArray(byte bytes[])
    {
        if(bytes == null)
            return "Null";
        else
            return String.format("Byte[%s]", new Object[] {
                Integer.valueOf(bytes.length)
            });
    }

    public static String prettyPrintDump(String rawDump)
    {
        StringBuilder buf = new StringBuilder();
        int indentPos = 0;
        for(int i = 0; i < rawDump.length(); i++)
        {
            char ch = rawDump.charAt(i);
            if(ch == '{')
            {
                indentPos++;
                buf.append("\n").append(getFormatTabs(indentPos));
            } else
            if(ch == '}')
            {
                if(--indentPos < 0)
                    throw new IllegalStateException("Argh! The indentPos is negative. TOKENS ARE NOT BALANCED!");
                buf.append("\n").append(getFormatTabs(indentPos));
            } else
            if(ch == ';')
                buf.append("\n").append(getFormatTabs(indentPos));
            else
                buf.append(ch);
        }

        if(indentPos != 0)
            throw new IllegalStateException("Argh! The indentPos is not == 0. TOKENS ARE NOT BALANCED!");
        else
            return buf.toString();
    }

    private static String getFormatTabs(int howMany)
    {
        return strFill('\t', howMany);
    }

    private static String strFill(char c, int howMany)
    {
        char chars[] = new char[howMany];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    public static final char TOKEN_INDENT_OPEN = 123;
    public static final char TOKEN_INDENT_CLOSE = 125;
    public static final char TOKEN_DIVIDER = 59;
}
