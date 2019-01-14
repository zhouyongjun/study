package  com.zhou.core.gm.cs.core.client.console.filter.codec;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.mina.core.buffer.IoBuffer;

import com.zhou.core.gm.cs.buffer.GmIoBuffer;
	/**
	 * 所有下发协议的父类
	 * @author zhouyongjun
	 *
	 */
public abstract class ClientAbstractGmReq{
	protected GmIoBuffer out;
	protected int protocolId;
	public ClientAbstractGmReq(int protocolId){
		this.protocolId = protocolId;
		out = new GmIoBuffer(IoBuffer.allocate(16).setAutoExpand(true));
	}
	public void encode() {
		out.putInt(protocolId);
		out.putString(getOsName());
		out.putString(getLocalIPForJava());
		body();
	}
	public String getOsName() {
		Properties properties = System.getProperties();
		return properties.getProperty("user.name");
	}
	
	public static String getLocalIPForJava(){
	    StringBuilder sb = new StringBuilder();
	    try {
	    	Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
	        while (en.hasMoreElements()) {
	            NetworkInterface intf = (NetworkInterface) en.nextElement();
	            Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
	            while (enumIpAddr.hasMoreElements()) {
	                 InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
	                 if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress() 
	                		 	&& inetAddress.isSiteLocalAddress()) {
	                	 sb.append(inetAddress.getHostAddress().toString()+",");
	                 }
	             }
	          }
	        if(sb.length() > 0) sb.deleteCharAt(sb.length()-1);
	    } catch (SocketException e) {  }
	    return sb.toString();
	}
	
	public abstract void body();

	public IoBuffer toClient() {
		/*
		 * 通过流长度来控制解析
		 */
		int limit = out.ioBuffer.position() + 4;
		IoBuffer ret = IoBuffer.allocate(limit);
		IoBuffer old = out.ioBuffer.flip().rewind();
	    ret.putInt(limit);
	    ret.put(old);
	    old.clear();
	    return ret.flip();
	}
	
	public int getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}
	public GmIoBuffer getOut() {
		return out;
	}
	public void setOut(GmIoBuffer out) {
		this.out = out;
	}
}
