package  com.zhou.core.gm.cs.buffer;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
	/**
	 * Gm数据流处理对象
	 * @author zhouyongjun
	 *
	 */
public class GmIoBuffer {
	public IoBuffer ioBuffer;
	
	public GmIoBuffer(IoBuffer buffer){
		this.ioBuffer = buffer;
	}
	
	public byte get(){
		return ioBuffer.get();
	}
	
	public byte[] getBytes(){
		byte[] vals = new byte[ioBuffer.remaining()];
		ioBuffer.get(vals);
		return vals;
	}
	
	public byte[] getBytes(byte[] vals) {
		ioBuffer.get(vals);
		return vals;
	}
	
	public int remaining() {
		return ioBuffer.remaining();
	}
	
	public short getShort() {
		return ioBuffer.getShort();
	}
	
	public int getInt() {
		return ioBuffer.getInt();
	}
	
	public float getFloat() {
		return ioBuffer.getFloat();
	}
	
	public double getDouble() {
		return ioBuffer.getDouble();
	}
	
	public long getLong() {
		return ioBuffer.getLong();
	}

	public String getString() {
		int size = getInt();
		byte[] bs = new byte[size];
		ioBuffer.get(bs);
		return new String(bs,Charset.forName("UTF-8"));
	}
	
	public void put(byte val) {
		ioBuffer.put(val);
	}
	
	public void putBytes(byte[] vals) {
		ioBuffer.put(vals);
	}
	
	public void putShort(short val) {
		ioBuffer.putShort(val);
	}
	
	public void putInt(int val) {
		ioBuffer.putInt(val);
	}
	
	public void putLong(long val) {
		ioBuffer.putLong(val);
	}
	
	public void putFloat(float val) {
		ioBuffer.putFloat(val);
	}
	
	public void putDouble(double val) {
		ioBuffer.putDouble(val);
	}
	
	public void putString(String val) {
		try {
			if (val == null) {
				val = "";
			}
			byte[] bs = val.getBytes("UTF-8");
			putInt(bs.length);
			putBytes(bs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int limit() {
		return ioBuffer.limit();
	}

	public void clear() {
		ioBuffer.clear();
	}

	public void position(int limit) {
		ioBuffer.position(limit);
	}
	
	
}
