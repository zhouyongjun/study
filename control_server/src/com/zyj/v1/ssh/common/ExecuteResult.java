package com.zyj.v1.ssh.common;

import com.zyj.v1.ssh.Server;
	/**
	 * 执行结果类
	 * 结果如何、相关的信息报错
	 * @author zhouyongjun
	 *
	 */
public class ExecuteResult {
	public static final ExecuteResult RESULT_SUCC = new ExecuteResult(true,"成功");
	public static final ExecuteResult RESULT_FAIL = new ExecuteResult(false,"失败");
	private boolean isSucc;
	private String msg;
	private Object[] objs;
	public ExecuteResult(boolean isSucc,String msg,Object... objs){
		this.isSucc =isSucc;
		this.msg = msg;
		this.objs = objs;
	}
	public boolean isSucc() {
		return isSucc;
	}
	public boolean isFail() {
		return !isSucc;
	}
	
	public void setSucc(boolean isSucc) {
		this.isSucc = isSucc;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public static ExecuteResult createSuccResult(String msg,Object... objs) {
		return new ExecuteResult(true,msg,objs);
	}
	
	public static ExecuteResult createFailResult(String msg,Object... objs) {
		return new ExecuteResult(false,msg,objs);
	}
	@Override
	public String toString() {
		return isSucc +","+msg;
	}
	
	public static String getMsg(Server server,String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("【").append(server.toString()).append("】").append(msg);
		return sb.toString();
	}
	public Object[] getObjs() {
		return objs;
	}
	public void setObjs(Object[] objs) {
		this.objs = objs;
	}
}
