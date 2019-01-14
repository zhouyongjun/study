package  com.zhou.core.gm.cs.core.server.console.filter.codec;

import org.apache.mina.core.buffer.IoBuffer;

import com.zhou.core.gm.cs.buffer.GmIoBuffer;
	/**
	 * 所有下发协议的父类
	 * @author zhouyongjun
	 *
	 */
public abstract class AbstractGmResp{
	public static final byte RESULT_SUCC = 0;
	public static final byte RESULT_FAIL = 1;
	protected GmIoBuffer out;
	protected int protocolId;
	protected byte result;
	protected String resultMsg = "";
	public AbstractGmResp(int protocolId){
		this.protocolId = protocolId;
		out = new GmIoBuffer(IoBuffer.allocate(16).setAutoExpand(true));
	}
	public void encode() {
		out.putInt(protocolId);
		out.put(result);
		out.putString(resultMsg);
		body();
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
	
	
	public void setResult(byte result) {
		this.result = result;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public byte getResult() {
		return result;
	}
	public String getResultMsg() {
		return resultMsg;
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
