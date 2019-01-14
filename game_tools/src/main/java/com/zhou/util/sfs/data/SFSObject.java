// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SFSObject.java

package com.zhou.util.sfs.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.zhou.util.sfs.data.serialization.DefaultObjectDumpFormatter;
import com.zhou.util.sfs.data.serialization.DefaultSFSDataSerializer;
import com.zhou.util.sfs.data.serialization.ISFSDataSerializer;

// Referenced classes of package com.smartfoxserver.v2.entities.data:
//            ISFSObject, SFSDataWrapper, SFSDataType, SFSArray, 
//            ISFSArray

public class SFSObject
    implements ISFSObject
{

    public static SFSObject newFromObject(Object o)
    {
        return (SFSObject)DefaultSFSDataSerializer.getInstance().pojo2sfs(o);
    }

    public static SFSObject newFromBinaryData(byte bytes[])
    {
        return (SFSObject)DefaultSFSDataSerializer.getInstance().binary2object(bytes);
    }

    public static ISFSObject newFromJsonData(String jsonStr)
    {
        return DefaultSFSDataSerializer.getInstance().json2object(jsonStr);
    }

    public static SFSObject newFromResultSet(ResultSet rset)
        throws SQLException
    {
        return DefaultSFSDataSerializer.getInstance().resultSet2object(rset);
    }

    public static SFSObject newInstance()
    {
        return new SFSObject();
    }

    public SFSObject()
    {
        dataHolder = new ConcurrentHashMap();
        serializer = DefaultSFSDataSerializer.getInstance();
    }

    public Iterator iterator()
    {
        return dataHolder.entrySet().iterator();
    }

    public boolean containsKey(String key)
    {
        return dataHolder.containsKey(key);
    }

    public boolean removeElement(String key)
    {
        return dataHolder.remove(key) != null;
    }

    public int size()
    {
        return dataHolder.size();
    }

    public byte[] toBinary()
    {
        return serializer.object2binary(this);
    }

    public String toJson()
    {
        return serializer.object2json(flatten());
    }

    public String getDump()
    {
        if(size() == 0)
            return "[ Empty SFSObject ]";
        else
            return DefaultObjectDumpFormatter.prettyPrintDump(dump());
    }

    public String getDump(boolean noFormat)
    {
        if(!noFormat)
            return dump();
        else
            return getDump();
    }

    private String dump()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        for(Iterator iterator1 = getKeys().iterator(); iterator1.hasNext(); buffer.append(';'))
        {
            String key = (String)iterator1.next();
            SFSDataWrapper wrapper = get(key);
            buffer.append("(").append(wrapper.getTypeId().name().toLowerCase()).append(") ").append(key).append(": ");
            if(wrapper.getTypeId() == SFSDataType.SFS_OBJECT)
                buffer.append(((SFSObject)wrapper.getObject()).getDump(false));
            else
            if(wrapper.getTypeId() == SFSDataType.SFS_ARRAY)
                buffer.append(((SFSArray)wrapper.getObject()).getDump(false));
            else
            if(wrapper.getTypeId() == SFSDataType.BYTE_ARRAY)
                buffer.append(DefaultObjectDumpFormatter.prettyPrintByteArray((byte[])wrapper.getObject()));
            else
            if(wrapper.getTypeId() == SFSDataType.CLASS)
                buffer.append(wrapper.getObject().getClass().getName());
            else
                buffer.append(wrapper.getObject());
        }

        buffer.append('}');
        return buffer.toString();
    }

    public String getHexDump()
    {
        return ByteUtils.fullHexDump(toBinary());
    }

    public boolean isNull(String key)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(key);
        if(wrapper == null)
            return false;
        return wrapper.getTypeId() == SFSDataType.NULL;
    }

    public SFSDataWrapper get(String key)
    {
        return (SFSDataWrapper)dataHolder.get(key);
    }

    public Boolean getBool(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Boolean)o.getObject();
    }

    public Collection getBoolArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Collection)o.getObject();
    }

    public Byte getByte(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Byte)o.getObject();
    }

    public byte[] getByteArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (byte[])o.getObject();
    }

    public Double getDouble(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Double)o.getObject();
    }

    public Collection getDoubleArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Collection)o.getObject();
    }

    public Float getFloat(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Float)o.getObject();
    }

    public Collection getFloatArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Collection)o.getObject();
    }

    public Integer getInt(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Integer)o.getObject();
    }

    public Collection getIntArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Collection)o.getObject();
    }

    public Set getKeys()
    {
        return dataHolder.keySet();
    }

    public Long getLong(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Long)o.getObject();
    }

    public Collection getLongArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Collection)o.getObject();
    }

    public ISFSArray getSFSArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (ISFSArray)o.getObject();
    }

    public ISFSObject getSFSObject(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (ISFSObject)o.getObject();
    }

    public Short getShort(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Short)o.getObject();
    }

    public Collection getShortArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Collection)o.getObject();
    }

    public Integer getUnsignedByte(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return Integer.valueOf(DefaultSFSDataSerializer.getInstance().getUnsignedByte(((Byte)o.getObject()).byteValue()));
    }

    public Collection getUnsignedByteArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        DefaultSFSDataSerializer serializer = DefaultSFSDataSerializer.getInstance();
        Collection intCollection = new ArrayList();
        byte abyte0[];
        int j = (abyte0 = (byte[])o.getObject()).length;
        for(int i = 0; i < j; i++)
        {
            byte b = abyte0[i];
            intCollection.add(Integer.valueOf(serializer.getUnsignedByte(b)));
        }

        return intCollection;
    }

    public String getUtfString(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (String)o.getObject();
    }

    public Collection getUtfStringArray(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return (Collection)o.getObject();
    }

    public Object getClass(String key)
    {
        SFSDataWrapper o = (SFSDataWrapper)dataHolder.get(key);
        if(o == null)
            return null;
        else
            return o.getObject();
    }

    public void putBool(String key, boolean value)
    {
        putObj(key, Boolean.valueOf(value), SFSDataType.BOOL);
    }

    public void putBoolArray(String key, Collection value)
    {
        putObj(key, value, SFSDataType.BOOL_ARRAY);
    }

    public void putByte(String key, byte value)
    {
        putObj(key, Byte.valueOf(value), SFSDataType.BYTE);
    }

    public void putByteArray(String key, byte value[])
    {
        putObj(key, value, SFSDataType.BYTE_ARRAY);
    }

    public void putDouble(String key, double value)
    {
        putObj(key, Double.valueOf(value), SFSDataType.DOUBLE);
    }

    public void putDoubleArray(String key, Collection value)
    {
        putObj(key, value, SFSDataType.DOUBLE_ARRAY);
    }

    public void putFloat(String key, float value)
    {
        putObj(key, Float.valueOf(value), SFSDataType.FLOAT);
    }

    public void putFloatArray(String key, Collection value)
    {
        putObj(key, value, SFSDataType.FLOAT_ARRAY);
    }

    public void putInt(String key, int value)
    {
        putObj(key, Integer.valueOf(value), SFSDataType.INT);
    }

    public void putIntArray(String key, Collection value)
    {
        putObj(key, value, SFSDataType.INT_ARRAY);
    }

    public void putLong(String key, long value)
    {
        putObj(key, Long.valueOf(value), SFSDataType.LONG);
    }

    public void putLongArray(String key, Collection value)
    {
        putObj(key, value, SFSDataType.LONG_ARRAY);
    }

    public void putNull(String key)
    {
        dataHolder.put(key, new SFSDataWrapper(SFSDataType.NULL, null));
    }

    public void putSFSArray(String key, ISFSArray value)
    {
        putObj(key, value, SFSDataType.SFS_ARRAY);
    }

    public void putSFSObject(String key, ISFSObject value)
    {
        putObj(key, value, SFSDataType.SFS_OBJECT);
    }

    public void putShort(String key, short value)
    {
        putObj(key, Short.valueOf(value), SFSDataType.SHORT);
    }

    public void putShortArray(String key, Collection value)
    {
        putObj(key, value, SFSDataType.SHORT_ARRAY);
    }

    public void putUtfString(String key, String value)
    {
        putObj(key, value, SFSDataType.UTF_STRING);
    }

    public void putUtfStringArray(String key, Collection value)
    {
        putObj(key, value, SFSDataType.UTF_STRING_ARRAY);
    }

    public void put(String key, SFSDataWrapper wrappedObject)
    {
        putObj(key, wrappedObject, null);
    }

    public void putClass(String key, Object o)
    {
        putObj(key, o, SFSDataType.CLASS);
    }

    public String toString()
    {
        return (new StringBuilder("[SFSObject, size: ")).append(size()).append("]").toString();
    }

    private void putObj(String key, Object value, SFSDataType typeId)
    {
        if(key == null)
            throw new IllegalArgumentException("SFSObject requires a non-null key for a 'put' operation!");
        if(key.length() > 255)
            throw new IllegalArgumentException("SFSObject keys must be less than 255 characters!");
        if(value == null)
            throw new IllegalArgumentException("SFSObject requires a non-null value! If you need to add a null use the putNull() method.");
        if(value instanceof SFSDataWrapper)
            dataHolder.put(key, (SFSDataWrapper)value);
        else
            dataHolder.put(key, new SFSDataWrapper(typeId, value));
    }

    private Map flatten()
    {
        Map map = new HashMap();
        DefaultSFSDataSerializer.getInstance().flattenObject(map, this);
        return map;
    }

    private Map dataHolder;
    private ISFSDataSerializer serializer;
}
