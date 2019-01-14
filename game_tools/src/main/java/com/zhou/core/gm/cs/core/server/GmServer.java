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
	 * GM��̨ I/0����
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
	 *  GM��������
	 * ������֮ǰҪ���� registerGmManager ע���¹�����
	 * @param params[0] :AbstractGmManager  GM��̨�����࣬�ṩAbstractGMConsoleManager������ʵ���࣬���û����Ĭ��Ϊ DefaultGMConsoleManager����
	 * @param gmConfig GM��̨�����࣬�ṩAbstractGmConfig������ʵ���࣬���û����Ĭ��Ϊ DefaultGmConfig����
	 * @param logger
	 * @throws Throwable 
	 */
	@Override
	public void startup(Object... params) throws Throwable {
		//����
		try {
			AbstractGmManager registerGmMgr = (AbstractGmManager)params[0]; 
			if (!ToolsApp.getInstance().isConfigLoaded())
					ToolsApp.getInstance().loadConfigPropeties();
			//Gm�������ɶ���Ͷ�ȡ����
			GmConfig.load();
			int port = GmConfig.getInt(GmConfig.KEY_GM_PORT, 10001);
			//ע�������
			registerGmManager(registerGmMgr);
			//ע����־
//			GmLog.registerLogger(logger);
			
			gmMgr.startUp();
			acceptor = new NioSocketAcceptor();
			acceptor.setReuseAddress(true);
			acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new CodecFactory()));
			acceptor.getFilterChain().addLast("ipfilter", new FilterBlackIp());
			//Chained handler
			ChainedIoHandler handler = new ChainedIoHandler();//���������Ϣ
				//��������
			handler.getChain().addFirst("gmcmd", new GmIoCommand());
				//����handler
			acceptor.setHandler(handler);
			//bind
			acceptor.bind(new InetSocketAddress(port));
			//���ü��� addListener
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
	 * ע��ʵ�ʵ�GM WORLD ����
	 * GM��̨����ʱ���ʹ�øö���
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
