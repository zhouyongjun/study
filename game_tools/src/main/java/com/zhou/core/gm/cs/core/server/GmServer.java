package  com.zhou.core.gm.cs.core.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.handler.chain.ChainedIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.zhou.ToolsApp;
import com.zhou.ToolsApp.ConsoleSystem;
import com.zhou.ToolsApp.SystemType;
import com.zhou.core.gm.cs.core.server.console.AbstractGmManager;
import com.zhou.core.gm.cs.core.server.console.DefaultGmManager;
import com.zhou.core.gm.cs.core.server.console.GmConfig;
import com.zhou.core.gm.cs.core.server.console.GmIoCommand;
import com.zhou.core.gm.cs.core.server.console.filter.codec.CodecFactory;
import com.zhou.core.gm.cs.core.server.console.filter.cond.FilterBlackIp;
import com.zhou.core.gm.cs.log.GmLog;
	/**
	 * GM后台 I/0服务
	 * @author zhouyongjun
	 *
	 */
public final class GmServer implements ConsoleSystem{
	private static GmServer instance= new GmServer();
	NioSocketAcceptor acceptor = null;
	private AbstractGmManager gmMgr = null;
	protected boolean isStartUp;
	private GmServer() {
	}
	public static GmServer getInstance() {
		return instance;
	}
	
	/**
	 *  GM启动方法
	 * 在启动之前要调用 registerGmManager 注册下管理类
	 * @param params[0] :AbstractGmManager  GM后台管理类，提供AbstractGMConsoleManager抽象类实现类，如果没有则默认为 DefaultGMConsoleManager对象
	 * @param gmConfig GM后台配置类，提供AbstractGmConfig抽象类实现类，如果没有则默认为 DefaultGmConfig对象
	 * @param logger
	 * @throws Throwable 
	 */
	@Override
	public void startup(Object... params) throws Throwable {
		//服务
		try {
			AbstractGmManager registerGmMgr = (AbstractGmManager)params[0]; 
			if (!ToolsApp.getInstance().isConfigLoaded())
					ToolsApp.getInstance().loadConfigPropeties();
			//Gm配置生成对象和读取参数
			GmConfig.load();
			int port = GmConfig.getInt(GmConfig.KEY_GM_PORT, 10001);
			//注册管理类
			registerGmManager(registerGmMgr);
			//注册日志
//			GmLog.registerLogger(logger);
			
			gmMgr.startUp();
			acceptor = new NioSocketAcceptor();
			acceptor.setReuseAddress(true);
			acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new CodecFactory()));
			acceptor.getFilterChain().addLast("ipfilter", new FilterBlackIp());
			//Chained handler
			ChainedIoHandler handler = new ChainedIoHandler();//处理接收信息
				//设置命令
			handler.getChain().addFirst("gmcmd", new GmIoCommand());
				//设置handler
			acceptor.setHandler(handler);
			//bind
			acceptor.bind(new InetSocketAddress(port));
			//设置监听 addListener
			acceptor.addListener(gmMgr);
			isStartUp = true;
			GmLog.info(">>>>>>>>      GM CONSOLE SERVER START SUCCESSFUL[PORT:"+port+"]       <<<<<<<<<");
		} catch (IOException e) {
			GmLog.error(">>>>>>>>      GM CONSOLE SERVER START FAIL PORT["+(GmConfig.getInt(GmConfig.KEY_GM_PORT, -1))+"]       <<<<<<<<<");
			GmLog.error(e);
		}
	}
	
	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.GM_SERVER;
	}
	
	@Override
	public void shutdown(Object... params)throws Throwable {
		gmMgr.shutDown();
		acceptor.dispose();
		isStartUp = false;
	}
	
	public AbstractGmManager getGmMgr() {
		return gmMgr;
	}
	/**
	 * 注册实际的GM WORLD 对象
	 * GM后台启动时候会使用该对象
	 * @param manager
	 */
	public void registerGmManager(AbstractGmManager manager) {
		if (manager == null) {
			manager = new DefaultGmManager();
		}
		this.gmMgr = manager;
	}
	
	@Override
	public boolean isStartUp() {
		return isStartUp;
	}	
}
