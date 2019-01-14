package com.zhou.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @author  
 * @2012-9-16 下午4:05:34
 */
public final class ZipUtil {

  // 压缩
  public static String compress(String str) throws IOException {
    if (str == null || str.length() == 0) {
      return str;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    GZIPOutputStream gzip = new GZIPOutputStream(out);
    gzip.write(str.getBytes());
    gzip.close();
    return out.toString("UTF-8");
  }
  
  public static byte[] compressToBytes(byte[] bytes) throws IOException {
	    if (bytes == null || bytes.length == 0) {
	      return bytes;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    GZIPOutputStream gzip = new GZIPOutputStream(out);
	    gzip.write(bytes);
	    gzip.close();
	    return out.toByteArray();
	  }
  
  

  // 解压�?
  public static byte[] uncompressToBytes(byte[] bytes) throws IOException {
	    if (bytes == null || bytes.length == 0) {
	      return null;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
	    GZIPInputStream gunzip = new GZIPInputStream(in);
	    byte[] buffer = new byte[256];
	    int n;
	    while ((n = gunzip.read(buffer)) >= 0) {
	      out.write(buffer, 0, n);
	    }
	    return out.toByteArray();
	  }

  // 解压�?
  public static String uncompress(String str) throws IOException {
    if (str == null || str.length() == 0) {
      return str;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayInputStream in = new ByteArrayInputStream(str
        .getBytes("UTF-8"));
    GZIPInputStream gunzip = new GZIPInputStream(in);
    byte[] buffer = new byte[256];
    int n;
    while ((n = gunzip.read(buffer)) >= 0) {
      out.write(buffer, 0, n);
    }
    // toString()使用平台默认编码，也可以显式的指定如toString(\"GBK\")
    return out.toString();
  }

}
