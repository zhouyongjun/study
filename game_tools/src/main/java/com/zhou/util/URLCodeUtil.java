package com.zhou.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class URLCodeUtil {

	 /**
     * ��������ַ�������URL����, ��ת��Ϊ%20������ʽ
     * 
     * @param input ԭ��
     * @return URL����. �������ʧ��, �򷵻�ԭ��
     */
    public static String encode(String input) {

        return encode(input,"utf-8");
    }
    
    public static String encode(String input,String charest) {
        if (input == null) {
            return "";
        }
        try {
            return URLEncoder.encode(input, charest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return input;
    }
    
    public static String decode(String output) {
    	return decode(output, "utf-8");
    }
    public static String decode(String output,String charset) {
        if (output == null) {
            return "";
        }

        try {
            return URLDecoder.decode(output, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return output;
    }
    
}
