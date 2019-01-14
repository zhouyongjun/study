package com.zhou.util;

import java.io.*;

/**
 * Created by Administrator on 14-5-20.
 */
public class SerializeUtil {
    public static byte[] serialize(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream  oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	public static <T> T deSerialize(byte[] value) {

        try {
            InputStream bais = new ByteArrayInputStream(value);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
