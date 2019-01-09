package com.zyj.v1.ssh.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.zyj.v1.SSHMain;
import com.zyj.v1.gui.panel.OperateOnlineServerPanel;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.CONF_PATH;
import com.zyj.v1.ssh.common.ClientSSH;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.db.GameDAO;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.ResourceMapping;
import com.zyj.v1.ssh.online_server.ServerType;
import com.zyj.v1.ssh.online_server.UpLoadFile;
import com.zyj.v1.ssh.online_server.UpLoadFileGroup;
import com.zyj.v1.ssh.util.xml.XmlDocument;
import com.zyj.v1.ssh.util.xml.XmlNode;


public class SshUtil {
	/**
	 * 默认utf-8 XML书写
	 * @param gmXmlDoc
	 */
	public static void writeToXml(XmlDocument gmXmlDoc) {
		writeToXml(gmXmlDoc,"UTF-8");
	}
	/**
	 * 指定编码 XML书写
	 * @param gmXmlDoc
	 * @param encoding
	 */
	public static void writeToXml(XmlDocument gmXmlDoc,String encoding) {
		try {
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding(encoding);
			Element rootE = doc.addElement(gmXmlDoc.getRootElementName());
			_buildElement(rootE,gmXmlDoc.getNodesList());
			//输出格式
			OutputFormat format = OutputFormat.createCompactFormat();
			format.setEncoding(encoding);//编码设置
			format.setNewlines(true);//是否换行
			format.setIndent(true);//是否缩进
			XMLWriter writer = new XMLWriter(new PrintWriter(gmXmlDoc.getFeleName(),encoding),format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			AppLog.error(e);
		}
		
	}
	
	/**
	 * 递归调用
	 * @param superElement
	 * @param nodesList
	 */
	private static void _buildElement(Element superElement, List<XmlNode> nodesList) {
		if (nodesList == null || nodesList.size() == 0) {
			return;
		}

		for (XmlNode gmNode : nodesList) {
			Element element = superElement.addElement(gmNode.getElementName());
			List<XmlNode> sub_nodesList = gmNode.getNodesList();
			List<String[]> attributesList = gmNode.getAttributesList();
			String text = gmNode.getText();
			if (attributesList != null) {//存在属性情况下
				for (String[] attr : attributesList) {
					element.addAttribute(attr[0], attr[1]);	
				}
			}
			if (text != null && text.length() > 0) {//存在内容情况下
				element.addText(text);
			}
			_buildElement(element, sub_nodesList);
		}
	
	}
	
	

	/**
	 * 获取文件的Docment对象
	 * @param fileName
	 * @return
	 */
	public static Document load(String fileName) {
		try {
		File file = new File(fileName);
		SAXReader reader = new SAXReader();
			return reader.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取指定名字的List 节点组
	 * @param baseE
	 * @param elementName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getElements(Element e,String elementName) {
		return e.elements(elementName);
	}
	/**
	 * 获取指定名字的单个节点
	 * @param baseE
	 * @param elementName
	 * @return
	 */
	public static Element getElement(Element e,String elementName) {
		return e.element(elementName);
	}

	public static String getAttriValue(Element e, String attrName) {
		return e.attributeValue(attrName);
	}

	public static void SetCompomentBound(Component component,int width,int hight) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int HIGHT = screenSize.height;
		int WIDTH = screenSize.width;
		component.setBounds(new Rectangle((WIDTH - width) / 2, (HIGHT - hight) / 2, width,hight));
	}
	
	public static String getStringEOR(String str,byte key) {
		byte[] bytes = str.getBytes();
		byte[] final_bytes = new byte[bytes.length];
		for(int i=0;i<bytes.length;i++) {
			final_bytes[i] = SshUtil.getByteEOR(bytes[i], key);
		}
		return new String(final_bytes);
	}
	
	public static byte getByteEOR(byte base,byte key) {
//		System.out.println("\tbyteEOR\t base="+base+",key="+key+",val="+(byte) (base^key));
		return (byte) (base^key);
	}
	
	public static short getShortEOR(short base,short key) {
//		System.out.println("\tshortEOR\t base="+base+",key="+key+",val="+(short) (base^key));
		return (short) (base^key);
	}
	public static int getIntEOR(int base,int key) {
//		System.out.println("\tintEOR\t base="+base+",key="+key+",val="+(base^key));
		return  (base^key);
	}
	public static long getLongEOR(long base,long key) {
		return base^key;
	}
	
	
	public static ExecuteResult ssh_upload_zipfile_and_unzipfile(Server server,String final_remote_dir,UpLoadFile upLoadfile,OperateOnlineServerPanel panel) {
		return ssh_upload_zipfile_and_unzipfile(server, final_remote_dir, upLoadfile, panel, true);
	}
	
	public static ExecuteResult ssh_upload_zipfile(Server server,String final_remote_dir,UpLoadFile upLoadfile,OperateOnlineServerPanel panel) {
		try {
			if(!server.getNet().openSsh_cmd_connect()) {
				if (panel != null)panel.addErrorResultMsg(server, "上传终止，链接服务器失败！");
				return ExecuteResult.createFailResult("ssh cmd 连接失败...");
			}
			if (panel != null)panel.addNormalResultMsg(server, "开始向服务器上传资源!");
			//执行命令
			//删除原来的相关文件目录
			ClientSSH cmd = server.getNet().getSsh_cmd();
			switch (ServerType.MAIN_NET) {
			case MAIN_NET:
			{
				ExecuteResult rm_result = cmd.execCmd(new CmdInfo(String.format("rm -r %s",final_remote_dir), "删除主服更新目录下内容"));
				if (!rm_result.isSucc()) {
					if (panel != null)panel.addErrorResultMsg(server, "上传终止，删除主服上次资源文件失败。。。");
					return rm_result;
				}
				ExecuteResult mk_dir_result = cmd.execCmd(new CmdInfo(String.format("mkdir -p %s",final_remote_dir), "创建主服更新目录"));
				if (!mk_dir_result.isSucc()) {
					if (panel != null)panel.addErrorResultMsg(server, "上传终止，创建主服目录失败。。。");
					return mk_dir_result;
				}
				break;
			}
			default:break;
			}
			long time = System.currentTimeMillis();
			//上传文件组文件
			ExecuteResult upload_result = cmd.upload(final_remote_dir,upLoadfile);
			AppLog.info(server,"上传花费时间："+(System.currentTimeMillis() - time));
			if(!upload_result.isSucc()) {
				if (panel != null)panel.addErrorResultMsg(server, "上传终止，上传资源报错。。。");
				return ExecuteResult.createFailResult(upload_result.getMsg()+",上传资源可能失败...");
			}
			if (panel != null)panel.addNormalResultMsg(server, "上传资源成功!!");
			return ExecuteResult.createSuccResult("执行上传资源成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("执行上传资源失败...");
		}
	}
	
	public static ExecuteResult ssh_upload_zipfile_and_unzipfile(Server server,String final_remote_dir,UpLoadFile upLoadfile,OperateOnlineServerPanel panel,boolean isUnzip) {
		try {
			if(!server.getNet().openSsh_cmd_connect()) {
				if (panel != null)panel.addErrorResultMsg(server, "上传终止，链接服务器失败！");
				return ExecuteResult.createFailResult("ssh cmd 连接失败...");
			}
			if (panel != null)panel.addNormalResultMsg(server, "开始向服务器上传资源!");
			//执行命令
			//删除原来的相关文件目录
			ClientSSH cmd = server.getNet().getSsh_cmd();
			switch (ServerType.MAIN_NET) {
			case MAIN_NET:
			{
				ExecuteResult rm_result = cmd.execCmd(new CmdInfo(String.format("rm -r %s",final_remote_dir), "删除主服更新目录下内容"));
				if (!rm_result.isSucc()) {
					if (panel != null)panel.addErrorResultMsg(server, "上传终止，删除主服上次资源文件失败。。。");
					return rm_result;
				}
				ExecuteResult mk_dir_result = cmd.execCmd(new CmdInfo(String.format("mkdir -p %s",final_remote_dir), "创建主服更新目录"));
				if (!mk_dir_result.isSucc()) {
					if (panel != null)panel.addErrorResultMsg(server, "上传终止，创建主服目录失败。。。");
					return mk_dir_result;
				}
				break;
			}
			default:break;
			}
			long time = System.currentTimeMillis();
			//上传文件组文件
			ExecuteResult upload_result = cmd.upload(final_remote_dir,upLoadfile);
			AppLog.info(server,"上传花费时间："+(System.currentTimeMillis() - time));
			if(!upload_result.isSucc()) {
				if (panel != null)panel.addErrorResultMsg(server, "上传终止，上传资源报错。。。");
				return ExecuteResult.createFailResult(upload_result.getMsg()+",上传资源可能失败...");
			}
			if (panel != null)panel.addNormalResultMsg(server, "上传资源成功!!");
			if (isUnzip) {
				ExecuteResult upzip_result = ssh_unzipfile(server, final_remote_dir, upLoadfile, panel);			
			}
			return ExecuteResult.createSuccResult("执行上传资源成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("执行上传资源失败...");
		}
	}
	
	
	public static ExecuteResult ssh_cmd(Server server,CmdInfo info) {
		try {
			if(!server.getNet().openSsh_cmd_connect()) {
				return ExecuteResult.createFailResult("ssh unzipfile 连接失败...");
			}
			ClientSSH cmd = server.getNet().getSsh_cmd();
			return cmd.execCmd(info);
		} catch (Exception e) {
			AppLog.error(server, e);
			return ExecuteResult.createFailResult("执行命令失败..."+info.toString());
		}
	}
	
	public static ExecuteResult ssh_unzipfile(Server server,String final_remote_dir,UpLoadFile upLoadfile,OperateOnlineServerPanel panel) {
		try {
			if(!server.getNet().openSsh_cmd_connect()) {
				if (panel != null)panel.addErrorResultMsg(server, "链接服务器失败！");
				return ExecuteResult.createFailResult("ssh unzipfile 连接失败...");
			}
			ClientSSH cmd = server.getNet().getSsh_cmd();
			long time = System.currentTimeMillis();
			ExecuteResult upzip_result = cmd.execCmd(new CmdInfo(String.format("cd " + final_remote_dir+" && unzip -o %s",final_remote_dir+upLoadfile.getFile().getName()), "unzip 压缩包"));
			if(!upzip_result.isSucc()) {
				if (panel != null)panel.addErrorResultMsg(server, "解压zip包失败。。。");
				return ExecuteResult.createFailResult(upzip_result.getMsg()+"unzip解压资源可能失败...");
			}
			if (panel != null)panel.addSuccResultMsg(server, "解压服务器资源zip成功，耗时："+((System.currentTimeMillis() - time)/1000.0d)+"秒！！");			
		
			return ExecuteResult.createSuccResult("unzip 成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("unzip 失败...");
		}
	}
	
	public static List<String> getUpdateDirFileNames(String zip_dir) {
		List<String> list = new ArrayList<String>();
		File file_zip_dir = new File(zip_dir);
		if (file_zip_dir.isDirectory()) {
			for (File sub_file : file_zip_dir.listFiles()) {
				if (sub_file.getName().equals("zip.exe")) continue;
				list.add(sub_file.getName());
			}
		}else {
			list.add(zip_dir);
		}
		return list;
	}
	public static ExecuteResult zip(Server server,String zip_file_name,String zip_dir) {
		//打压成zip包
		//等待命令结束
		try {
			StringBuffer sb = new StringBuffer();
			for (String dir : getUpdateDirFileNames(zip_dir)) {
				sb.append(dir).append(" ");
			}
//			File file_zip_dir = new File(zip_dir);
//			if (file_zip_dir.isDirectory()) {
//				for (File sub_file : file_zip_dir.listFiles()) {
//					if (sub_file.getName().equals("zip.exe")) continue;
//					sb.append(sub_file.getName()).append(" ");
//				}
//			}else {
//				sb.append(zip_dir);
//			}
			long time = System.currentTimeMillis();
			Process process = Runtime.getRuntime().exec(String.format("cmd.exe /C cd %s && zip.exe %s -r %s",OnlineServerManager.DIR_UPLOAD_ZIP,zip_file_name,sb.toString()));
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				AppLog.info(server, "ZIP LINE : "+line);
			}
			process.waitFor();
			time = System.currentTimeMillis() - time;
			return ExecuteResult.createSuccResult(null,new UpLoadFile("", new File(OnlineServerManager.DIR_UPLOAD_ZIP+zip_file_name)),time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExecuteResult.createFailResult(null);
	}
	
	public static ExecuteResult copyFiles(Server server,OperateOnlineServerPanel panel,UpLoadFileGroup upload_files,String zip_dir) {
		File file = new File(OnlineServerManager.DIR_UPLOAD_ZIP);
		//删除原来的
		deleteFile(file);
			//生成最新的 
		file.mkdirs();
			//拷贝原来的文件
		if(panel != null && server != null)panel.addNormalResultMsg(server, "删除原来目录并生成新的空目录！");
		if (!upload_files.copys(zip_dir)) {
			if(panel != null && server != null)panel.addErrorResultMsg(server, "上传终止，本地向"+OnlineServerManager.DIR_UPLOAD_ZIP+"复制文件失败！");
			return ExecuteResult.createFailResult("复制文件失败");
		}
		return null;
	}
	
	public static ExecuteResult copyFiles(Server server,OperateOnlineServerPanel panel,String src_dir,String zip_dir) {
		File file = new File(zip_dir);
		//删除原来的
		deleteFile(file);
			//生成最新的 
		file.mkdirs();
			//拷贝原来的文件
		if(panel != null && server != null)panel.addNormalResultMsg(server, "删除原来目录并生成新的空目录！");
		if (copys(src_dir,zip_dir)) {
			if(panel != null && server != null)panel.addErrorResultMsg(server, "上传终止，本地向"+OnlineServerManager.DIR_UPLOAD_ZIP+"复制文件失败！");
			return ExecuteResult.createFailResult("复制文件失败");
		}
		return null;
	}
	
	/**
	 * 复制文件到指定目录
	 * @param targetDir
	 */
	public static boolean copys(String src_dir,String targetDir) {
		File file = new File(targetDir);
		//删除原来的
		deleteFile(file);
			//生成最新的 
		file.mkdirs();
		String sys_path = System.getProperty("user.dir")+"\\"+targetDir.substring(0,targetDir.length()-1);
		src_dir = "upload";
		try {
			String cmd = String.format("cmd /c xCopy \"%s\" \"%s\" /S/Y",src_dir,sys_path);
			AppLog.info(cmd +" begin...");
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				AppLog.info(null, "  xCopy LINE : "+line);
			}
			process.waitFor();
			AppLog.info(cmd +" end...");
		} catch (Exception e) {
			AppLog.error(src_dir +" xcopy to dir["+targetDir+"] is error...",e);
			return false;
		}			
		return true;
	}
	
	
	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			for(File f : file.listFiles()) {
				deleteFile(f);
			}
			file.delete();
		}else {
			file.delete();
		}
	}
	//资源映射路径
	public static final String FILE_RESOURCE_MAPPING = CONF_PATH.PATH_ONLINE_SERVER+"resource_mapping.xml";
	public static Map<String,ResourceMapping> resource_mappings = new HashMap<String, ResourceMapping>();
	public static void loadResourceMapping() {
		try {
			
			Document doc = SshUtil.load(FILE_RESOURCE_MAPPING);
			List<Element> els = SshUtil.getElements(doc.getRootElement(), "mapping");
			if (els == null) {
				return;
			}
			Map<String,ResourceMapping> temp_map = new HashMap<String, ResourceMapping>();
			for (Element e : els) {
				ResourceMapping mapping = new ResourceMapping();
				mapping.load(e);
				temp_map.put(mapping.getName(), mapping);
			}
			resource_mappings = temp_map;
		} catch (Exception e) {
			AppLog.error(e);
			JOptionPane.showMessageDialog(null, FILE_RESOURCE_MAPPING+" 加载错误，查询当前日志!");
			System.exit(0);
		}
	}
	public static ResourceMapping getResourceMaping(String name) {
		return resource_mappings.get(name);
	}
	public static boolean copyFile(UpLoadFile file, String targetDir) {
		try {
			String sys_path = System.getProperty("user.dir")+"\\"+targetDir;
			File f_path = new File(sys_path+file.getPath());
			f_path.mkdirs();
			String cmd = String.format("cmd /c copy %s %s",file.getFile().getPath(),f_path.getPath());
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			AppLog.info(cmd);
		} catch (Exception e) {
			AppLog.error(file.getFile() +" copy to dir["+targetDir+"] is error...",e);
			return false;
		}			
		return true;
	}

	/**
	 * 维护服务器操作
	 * @return
	 */
	public static ExecuteResult telnet_freeze(Server server) {
		try {
			ExecuteResult result = telnet_cmd(server,server.getAgent_online_server().getCmd_telnet_freeze());
			if (result.isSucc()) {
				result = ExecuteResult.createSuccResult("执行freeze成功！");
			}
			server.getNet().getTelnet().disconect();
			return result;
		} catch (Exception e) {
			AppLog.error(server,"freeze is errror...",e);
			return ExecuteResult.createFailResult("执行freeze失败...");
		}
	}
	/**
	 * 
	 * @param server
	 * @param cmd
	 * @return
	 */
	public static ExecuteResult telnet_cmd(Server server,CmdInfo cmd) {
		if (!server.getNet().isConnectTelnet()) {
			if (!server.getNet().openTelnet()) {
				AppLog.error(server, "telnet链接失败...");
				return ExecuteResult.createFailResult("telnet链接失败...");
			}
		}
		// 执行命令
		AppLog.info(server, "telnet执行["+cmd.getShowName()+"]开始！");
		ExecuteResult result = server.getNet().getTelnet().execCmd(cmd);
		if (!result.isSucc()) {
			AppLog.info(server, "telnet执行["+cmd.getShowName()+"]失败！");
			return ExecuteResult.createFailResult(result.getMsg() + "/n"+ cmd.getShowName() + "可能失败...");
		}
		AppLog.info(server, "telnet执行["+cmd.getShowName()+"]成功！");
		return ExecuteResult.createSuccResult("执行成功!");
	
	}
	
	/**
	 * 启动游戏方法
	 * @return
	 */
	public static ExecuteResult start_up(Server server) {
		CmdInfo cmd = server.getAgent_online_server().getCmd_start_up();
		try {
			//是否连接
			if(connServer(server).isFail()) return ExecuteResult.createFailResult("ssh cmd 连接失败...");
			//执行命令
			ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd);
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+","+cmd.getShowName()+"可能失败...");
			}
			return ExecuteResult.createSuccResult("执行"+cmd+"成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("执行"+cmd+"失败...");
		}
	}
	
	public static ExecuteResult connServer(Server conn_server) {
		if(!conn_server.getNet().openSsh_cmd_connect()) {
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(conn_server,"SSH链接失败。。。");
			return ExecuteResult.RESULT_FAIL;
		}
		return ExecuteResult.RESULT_SUCC;
	}
	
	/**
	 * 关闭游戏方法
	 * @return
	 */
	public static ExecuteResult shut_up(Server server) {
		CmdInfo cmd = server.getAgent_online_server().getCmd_shut_down();
		try {
			//是否连接
			if(connServer(server).isFail()) return ExecuteResult.createFailResult("ssh 连接失败...");
			//执行命令
			ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd);
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+","+cmd.getShowName()+"可能失败...");
			}
			return ExecuteResult.createSuccResult("执行"+cmd+"成功！");
		} catch (Exception e) {
			AppLog.error(server, "shut_up is error ....", e);
			return ExecuteResult.createFailResult("执行"+cmd+"失败...");
		}
	}
	

	public static ExecuteResult ssh_pass_to_other_server(String target_dir,Server target_server,Server from_server,String from_dir) {
		if (connServer(from_server).isFail()) {
			return ExecuteResult.createFailResult(String.format("%s ssh cmd 连接失败...", from_server));
		}
	ExecuteResult result = from_server.getNet().getSsh_cmd().execCmd(new CmdInfo(
			String.format("sshpass -p '%s' scp -P %d -o StrictHostKeyChecking=no -r  %s %s@%s:%s",
					target_server.getSsh_password(),//密码
					target_server.getSsh_port(),//端口号
					from_dir,//本服目录
					target_server.getSsh_username(),//账号
					target_server.getSsh_host(),//地址
					target_dir),//目标目录
			String.format("%s向%s分发更新文件", from_server.toString(),target_server.toString())));
		return result;
}
}
