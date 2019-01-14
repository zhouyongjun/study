// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SFSArray.java

package com.zhou.util.sfs.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zhou.util.sfs.data.serialization.DefaultObjectDumpFormatter;
import com.zhou.util.sfs.data.serialization.DefaultSFSDataSerializer;
import com.zhou.util.sfs.data.serialization.ISFSDataSerializer;

// Referenced classes of package com.smartfoxserver.v2.entities.data:
//            ISFSArray, SFSDataWrapper, SFSDataType, ISFSObject

public class SFSArray
    implements ISFSArray
{

    public SFSArray()
    {
        dataHolder = new ArrayList();
        serializer = DefaultSFSDataSerializer.getInstance();
    }

    public static SFSArray newFromBinaryData(byte bytes[])
    {
        return (SFSArray)DefaultSFSDataSerializer.getInstance().binary2array(bytes);
    }

    public static SFSArray newFromResultSet(ResultSet rset)
        throws SQLException
    {
        return DefaultSFSDataSerializer.getInstance().resultSet2array(rset);
    }

    public static SFSArray newFromJsonData(String jsonStr)
    {
        return (SFSArray)DefaultSFSDataSerializer.getInstance().json2array(jsonStr);
    }

    public static SFSArray newInstance()
    {
        return new SFSArray();
    }

    public String getDump()
    {
        if(size() == 0)
            return "[ Empty SFSArray ]";
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
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Object objDump = null;
        SFSDataWrapper wrappedObject;
        for(Iterator iter = dataHolder.iterator(); iter.hasNext(); sb.append(" (").append(wrappedObject.getTypeId().name().toLowerCase()).append(") ").append(objDump).append(';'))
        {
            wrappedObject = (SFSDataWrapper)iter.next();
            if(wrappedObject.getTypeId() == SFSDataType.SFS_OBJECT)
                objDump = ((ISFSObject)wrappedObject.getObject()).getDump(false);
            else
            if(wrappedObject.getTypeId() == SFSDataType.SFS_ARRAY)
                objDump = ((ISFSArray)wrappedObject.getObject()).getDump(false);
            else
            if(wrappedObject.getTypeId() == SFSDataType.BYTE_ARRAY)
                objDump = DefaultObjectDumpFormatter.prettyPrintByteArray((byte[])wrappedObject.getObject());
            else
            if(wrappedObject.getTypeId() == SFSDataType.CLASS)
                objDump = wrappedObject.getObject().getClass().getName();
            else
                objDump = wrappedObject.getObject();
        }

        if(size() > 0)
            sb.delete(sb.length() - 1, sb.length());
        sb.append('}');
        return sb.toString();
    }

    public String getHexDump()
    {
        return ByteUtils.fullHexDump(toBinary());
    }

    public byte[] toBinary()
    {
        return serializer.array2binary(this);
    }

    public String toJson()
    {
        return DefaultSFSDataSerializer.getInstance().array2json(flatten());
    }

    public boolean isNull(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        if(wrapper == null)
            return false;
        return wrapper.getTypeId() == SFSDataType.NULL;
    }

    public SFSDataWrapper get(int index)
    {
        return (SFSDataWrapper)dataHolder.get(index);
    }

    public Boolean getBool(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Boolean)wrapper.getObject();
    }

    public Byte getByte(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Byte)wrapper.getObject();
    }

    public Integer getUnsignedByte(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : Integer.valueOf(DefaultSFSDataSerializer.getInstance().getUnsignedByte(((Byte)wrapper.getObject()).byteValue()));
    }

    public Short getShort(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Short)wrapper.getObject();
    }

    public Integer getInt(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Integer)wrapper.getObject();
    }

    public Long getLong(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Long)wrapper.getObject();
    }

    public Float getFloat(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Float)wrapper.getObject();
    }

    public Double getDouble(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Double)wrapper.getObject();
    }

    public String getUtfString(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (String)wrapper.getObject();
    }

    public Collection getBoolArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Collection)wrapper.getObject();
    }

    public byte[] getByteArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (byte[])wrapper.getObject();
    }

    public Collection getUnsignedByteArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        if(wrapper == null)
            return null;
        DefaultSFSDataSerializer serializer = DefaultSFSDataSerializer.getInstance();
        Collection intCollection = new ArrayList();
        byte abyte0[];
        int j = (abyte0 = (byte[])wrapper.getObject()).length;
        for(int i = 0; i < j; i++)
        {
            byte b = abyte0[i];
            intCollection.add(Integer.valueOf(serializer.getUnsignedByte(b)));
        }

        return intCollection;
    }

    public Collection getShortArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Collection)wrapper.getObject();
    }

    public Collection getIntArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Collection)wrapper.getObject();
    }

    public Collection getLongArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Collection)wrapper.getObject();
    }

    public Collection getFloatArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Collection)wrapper.getObject();
    }

    public Collection getDoubleArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Collection)wrapper.getObject();
    }

    public Collection getUtfStringArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (Collection)wrapper.getObject();
    }

    public ISFSArray getSFSArray(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (ISFSArray)wrapper.getObject();
    }

    public ISFSObject getSFSObject(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : (ISFSObject)wrapper.getObject();
    }

    public Object getClass(int index)
    {
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        return wrapper == null ? null : wrapper.getObject();
    }

    public void addBool(boolean value)
    {
        addObject(Boolean.valueOf(value), SFSDataType.BOOL);
    }

    public void addBoolArray(Collection value)
    {
        addObject(value, SFSDataType.BOOL_ARRAY);
    }

    public void addByte(byte value)
    {
        addObject(Byte.valueOf(value), SFSDataType.BYTE);
    }

    public void addByteArray(byte value[])
    {
        addObject(value, SFSDataType.BYTE_ARRAY);
    }

    public void addDouble(double value)
    {
        addObject(Double.valueOf(value), SFSDataType.DOUBLE);
    }

    public void addDoubleArray(Collection value)
    {
        addObject(value, SFSDataType.DOUBLE_ARRAY);
    }

    public void addFloat(float value)
    {
        addObject(Float.valueOf(value), SFSDataType.FLOAT);
    }

    public void addFloatArray(Collection value)
    {
        addObject(value, SFSDataType.FLOAT_ARRAY);
    }

    public void addInt(int value)
    {
        addObject(Integer.valueOf(value), SFSDataType.INT);
    }

    public void addIntArray(Collection value)
    {
        addObject(value, SFSDataType.INT_ARRAY);
    }

    public void addLong(long value)
    {
        addObject(Long.valueOf(value), SFSDataType.LONG);
    }

    public void addLongArray(Collection value)
    {
        addObject(value, SFSDataType.LONG_ARRAY);
    }

    public void addNull()
    {
        addObject(null, SFSDataType.NULL);
    }

    public void addSFSArray(ISFSArray value)
    {
        addObject(value, SFSDataType.SFS_ARRAY);
    }

    public void addSFSObject(ISFSObject value)
    {
        addObject(value, SFSDataType.SFS_OBJECT);
    }

    public void addShort(short value)
    {
        addObject(Short.valueOf(value), SFSDataType.SHORT);
    }

    public void addShortArray(Collection value)
    {
        addObject(value, SFSDataType.SHORT_ARRAY);
    }

    public void addUtfString(String value)
    {
        addObject(value, SFSDataType.UTF_STRING);
    }

    public void addUtfStringArray(Collection value)
    {
        addObject(value, SFSDataType.UTF_STRING_ARRAY);
    }

    public void addClass(Object o)
    {
        addObject(o, SFSDataType.CLASS);
    }

    public void add(SFSDataWrapper wrappedObject)
    {
        dataHolder.add(wrappedObject);
    }

    public boolean contains(Object obj)
    {
        if((obj instanceof ISFSArray) || (obj instanceof ISFSObject))
            throw new UnsupportedOperationException("ISFSArray and ISFSObject are not supported by this method.");
        boolean found = false;
        for(Iterator iter = dataHolder.iterator(); iter.hasNext();)
        {
            Object item = ((SFSDataWrapper)iter.next()).getObject();
            if(item.equals(obj))
            {
                found = true;
                break;
            }
        }

        return found;
    }

    public Object getElementAt(int index)
    {
        Object item = null;
        SFSDataWrapper wrapper = (SFSDataWrapper)dataHolder.get(index);
        if(wrapper == null);
        item = wrapper.getObject();
        return item;
    }

    public Iterator iterator()
    {
        return dataHolder.iterator();
    }

    public void removeElementAt(int index)
    {
        dataHolder.remove(index);
    }

    public int size()
    {
        return dataHolder.size();
    }

    public String toString()
    {
        return (new StringBuilder("[SFSArray, size: ")).append(size()).append("]").toString();
    }

    private void addObject(Object value, SFSDataType typeId)
    {
        dataHolder.add(new SFSDataWrapper(typeId, value));
    }

    private List flatten()
    {
        List list = new ArrayList();
        DefaultSFSDataSerializer.getInstance().flattenArray(list, this);
        return list;
    }

    private ISFSDataSerializer serializer;
    private List dataHolder;
}
