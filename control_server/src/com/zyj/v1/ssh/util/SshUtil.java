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
	 * Ĭ��utf-8 XML��д
	 * @param gmXmlDoc
	 */
	public static void writeToXml(XmlDocument gmXmlDoc) {
		writeToXml(gmXmlDoc,"UTF-8");
	}
	/**
	 * ָ������ XML��д
	 * @param gmXmlDoc
	 * @param encoding
	 */
	public static void writeToXml(XmlDocument gmXmlDoc,String encoding) {
		try {
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding(encoding);
			Element rootE = doc.addElement(gmXmlDoc.getRootElementName());
			_buildElement(rootE,gmXmlDoc.getNodesList());
			//�����ʽ
			OutputFormat format = OutputFormat.createCompactFormat();
			format.setEncoding(encoding);//��������
			format.setNewlines(true);//�Ƿ���
			format.setIndent(true);//�Ƿ�����
			XMLWriter writer = new XMLWriter(new PrintWriter(gmXmlDoc.getFeleName(),encoding),format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			AppLog.error(e);
		}
		
	}
	
	/**
	 * �ݹ����
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
			if (attributesList != null) {//�������������
				for (String[] attr : attributesList) {
					element.addAttribute(attr[0], attr[1]);	
				}
			}
			if (text != null && text.length() > 0) {//�������������
				element.addText(text);
			}
			_buildElement(element, sub_nodesList);
		}
	
	}
	
	

	/**
	 * ��ȡ�ļ���Docment����
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
	 * ��ȡָ�����ֵ�List �ڵ���
	 * @param baseE
	 * @param elementName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getElements(Element e,String elementName) {
		return e.elements(elementName);
	}
	/**
	 * ��ȡָ�����ֵĵ����ڵ�
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
				if (panel != null)panel.addErrorResultMsg(server, "�ϴ���ֹ�����ӷ�����ʧ�ܣ�");
				return ExecuteResult.createFailResult("ssh cmd ����ʧ��...");
			}
			if (panel != null)panel.addNormalResultMsg(server, "��ʼ��������ϴ���Դ!");
			//ִ������
			//ɾ��ԭ��������ļ�Ŀ¼
			ClientSSH cmd = server.getNet().getSsh_cmd();
			switch (ServerType.MAIN_NET) {
			case MAIN_NET:
			{
				ExecuteResult rm_result = cmd.execCmd(new CmdInfo(String.format("rm -r %s",final_remote_dir), "ɾ����������Ŀ¼������"));
				if (!rm_result.isSucc()) {
					if (panel != null)panel.addErrorResultMsg(server, "�ϴ���ֹ��ɾ�������ϴ���Դ�ļ�ʧ�ܡ�����");
					return rm_result;
				}
				ExecuteResult mk_dir_result = cmd.execCmd(new CmdInfo(String.format("mkdir -p %s",final_remote_dir), "������������Ŀ¼"));
				if (!mk_dir_result.isSucc()) {
					if (panel != null)panel.addErrorResultMsg(server, "�ϴ���ֹ����������Ŀ¼ʧ�ܡ�����");
					return mk_dir_result;
				}
				break;
			}
			default:break;
			}
			long time = System.currentTimeMillis();
			//�ϴ��ļ����ļ�
			ExecuteResult upload_result = cmd.upload(final_remote_dir,upLoadfile);
			AppLog.info(server,"�ϴ�����ʱ�䣺"+(System.currentTimeMillis() - time));
			if(!upload_result.isSucc()) {
				if (panel != null)panel.addErrorResultMsg(server, "�ϴ���ֹ���ϴ���Դ��������");
				return ExecuteResult.createFailResult(upload_result.getMsg()+",�ϴ���Դ����ʧ��...");
			}
			if (panel != null)panel.addNormalResultMsg(server, "�ϴ���Դ�ɹ�!!");
			return ExecuteResult.createSuccResult("ִ���ϴ���Դ�ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("ִ���ϴ���Դʧ��...");
		}
	}
	
	public static ExecuteResult ssh_upload_zipfile_and_unzipfile(Server server,String final_remote_dir,UpLoadFile upLoadfile,OperateOnlineServerPanel panel,boolean isUnzip) {
		try {
			if(!server.getNet().openSsh_cmd_connect()) {
				if (panel != null)panel.addErrorResultMsg(server, "�ϴ���ֹ�����ӷ�����ʧ�ܣ�");
				return ExecuteResult.createFailResult("ssh cmd ����ʧ��...");
			}
			if (panel != null)panel.addNormalResultMsg(server, "��ʼ��������ϴ���Դ!");
			//ִ������
			//ɾ��ԭ��������ļ�Ŀ¼
			ClientSSH cmd = server.getNet().getSsh_cmd();
			switch (ServerType.MAIN_NET) {
			case MAIN_NET:
			{
				ExecuteResult rm_result = cmd.execCmd(new CmdInfo(String.format("rm -r %s",final_remote_dir), "ɾ����������Ŀ¼������"));
				if (!rm_result.isSucc()) {
					if (panel != null)panel.addErrorResultMsg(server, "�ϴ���ֹ��ɾ�������ϴ���Դ�ļ�ʧ�ܡ�����");
					return rm_result;
				}
				ExecuteResult mk_dir_result = cmd.execCmd(new CmdInfo(String.format("mkdir -p %s",final_remote_dir), "������������Ŀ¼"));
				if (!mk_dir_result.isSucc()) {
					if (panel != null)panel.addErrorResultMsg(server, "�ϴ���ֹ����������Ŀ¼ʧ�ܡ�����");
					return mk_dir_result;
				}
				break;
			}
			default:break;
			}
			long time = System.currentTimeMillis();
			//�ϴ��ļ����ļ�
			ExecuteResult upload_result = cmd.upload(final_remote_dir,upLoadfile);
			AppLog.info(server,"�ϴ�����ʱ�䣺"+(System.currentTimeMillis() - time));
			if(!upload_result.isSucc()) {
				if (panel != null)panel.addErrorResultMsg(server, "�ϴ���ֹ���ϴ���Դ��������");
				return ExecuteResult.createFailResult(upload_result.getMsg()+",�ϴ���Դ����ʧ��...");
			}
			if (panel != null)panel.addNormalResultMsg(server, "�ϴ���Դ�ɹ�!!");
			if (isUnzip) {
				ExecuteResult upzip_result = ssh_unzipfile(server, final_remote_dir, upLoadfile, panel);			
			}
			return ExecuteResult.createSuccResult("ִ���ϴ���Դ�ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("ִ���ϴ���Դʧ��...");
		}
	}
	
	
	public static ExecuteResult ssh_cmd(Server server,CmdInfo info) {
		try {
			if(!server.getNet().openSsh_cmd_connect()) {
				return ExecuteResult.createFailResult("ssh unzipfile ����ʧ��...");
			}
			ClientSSH cmd = server.getNet().getSsh_cmd();
			return cmd.execCmd(info);
		} catch (Exception e) {
			AppLog.error(server, e);
			return ExecuteResult.createFailResult("ִ������ʧ��..."+info.toString());
		}
	}
	
	public static ExecuteResult ssh_unzipfile(Server server,String final_remote_dir,UpLoadFile upLoadfile,OperateOnlineServerPanel panel) {
		try {
			if(!server.getNet().openSsh_cmd_connect()) {
				if (panel != null)panel.addErrorResultMsg(server, "���ӷ�����ʧ�ܣ�");
				return ExecuteResult.createFailResult("ssh unzipfile ����ʧ��...");
			}
			ClientSSH cmd = server.getNet().getSsh_cmd();
			long time = System.currentTimeMillis();
			ExecuteResult upzip_result = cmd.execCmd(new CmdInfo(String.format("cd " + final_remote_dir+" && unzip -o %s",final_remote_dir+upLoadfile.getFile().getName()), "unzip ѹ����"));
			if(!upzip_result.isSucc()) {
				if (panel != null)panel.addErrorResultMsg(server, "��ѹzip��ʧ�ܡ�����");
				return ExecuteResult.createFailResult(upzip_result.getMsg()+"unzip��ѹ��Դ����ʧ��...");
			}
			if (panel != null)panel.addSuccResultMsg(server, "��ѹ��������Դzip�ɹ�����ʱ��"+((System.currentTimeMillis() - time)/1000.0d)+"�룡��");			
		
			return ExecuteResult.createSuccResult("unzip �ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("unzip ʧ��...");
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
		//��ѹ��zip��
		//�ȴ��������
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
		//ɾ��ԭ����
		deleteFile(file);
			//�������µ� 
		file.mkdirs();
			//����ԭ�����ļ�
		if(panel != null && server != null)panel.addNormalResultMsg(server, "ɾ��ԭ��Ŀ¼�������µĿ�Ŀ¼��");
		if (!upload_files.copys(zip_dir)) {
			if(panel != null && server != null)panel.addErrorResultMsg(server, "�ϴ���ֹ��������"+OnlineServerManager.DIR_UPLOAD_ZIP+"�����ļ�ʧ�ܣ�");
			return ExecuteResult.createFailResult("�����ļ�ʧ��");
		}
		return null;
	}
	
	public static ExecuteResult copyFiles(Server server,OperateOnlineServerPanel panel,String src_dir,String zip_dir) {
		File file = new File(zip_dir);
		//ɾ��ԭ����
		deleteFile(file);
			//�������µ� 
		file.mkdirs();
			//����ԭ�����ļ�
		if(panel != null && server != null)panel.addNormalResultMsg(server, "ɾ��ԭ��Ŀ¼�������µĿ�Ŀ¼��");
		if (copys(src_dir,zip_dir)) {
			if(panel != null && server != null)panel.addErrorResultMsg(server, "�ϴ���ֹ��������"+OnlineServerManager.DIR_UPLOAD_ZIP+"�����ļ�ʧ�ܣ�");
			return ExecuteResult.createFailResult("�����ļ�ʧ��");
		}
		return null;
	}
	
	/**
	 * �����ļ���ָ��Ŀ¼
	 * @param targetDir
	 */
	public static boolean copys(String src_dir,String targetDir) {
		File file = new File(targetDir);
		//ɾ��ԭ����
		deleteFile(file);
			//�������µ� 
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
	//��Դӳ��·��
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
			JOptionPane.showMessageDialog(null, FILE_RESOURCE_MAPPING+" ���ش��󣬲�ѯ��ǰ��־!");
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
	 * ά������������
	 * @return
	 */
	public static ExecuteResult telnet_freeze(Server server) {
		try {
			ExecuteResult result = telnet_cmd(server,server.getAgent_online_server().getCmd_telnet_freeze());
			if (result.isSucc()) {
				result = ExecuteResult.createSuccResult("ִ��freeze�ɹ���");
			}
			server.getNet().getTelnet().disconect();
			return result;
		} catch (Exception e) {
			AppLog.error(server,"freeze is errror...",e);
			return ExecuteResult.createFailResult("ִ��freezeʧ��...");
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
				AppLog.error(server, "telnet����ʧ��...");
				return ExecuteResult.createFailResult("telnet����ʧ��...");
			}
		}
		// ִ������
		AppLog.info(server, "telnetִ��["+cmd.getShowName()+"]��ʼ��");
		ExecuteResult result = server.getNet().getTelnet().execCmd(cmd);
		if (!result.isSucc()) {
			AppLog.info(server, "telnetִ��["+cmd.getShowName()+"]ʧ�ܣ�");
			return ExecuteResult.createFailResult(result.getMsg() + "/n"+ cmd.getShowName() + "����ʧ��...");
		}
		AppLog.info(server, "telnetִ��["+cmd.getShowName()+"]�ɹ���");
		return ExecuteResult.createSuccResult("ִ�гɹ�!");
	
	}
	
	/**
	 * ������Ϸ����
	 * @return
	 */
	public static ExecuteResult start_up(Server server) {
		CmdInfo cmd = server.getAgent_online_server().getCmd_start_up();
		try {
			//�Ƿ�����
			if(connServer(server).isFail()) return ExecuteResult.createFailResult("ssh cmd ����ʧ��...");
			//ִ������
			ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd);
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+","+cmd.getShowName()+"����ʧ��...");
			}
			return ExecuteResult.createSuccResult("ִ��"+cmd+"�ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("ִ��"+cmd+"ʧ��...");
		}
	}
	
	public static ExecuteResult connServer(Server conn_server) {
		if(!conn_server.getNet().openSsh_cmd_connect()) {
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(conn_server,"SSH����ʧ�ܡ�����");
			return ExecuteResult.RESULT_FAIL;
		}
		return ExecuteResult.RESULT_SUCC;
	}
	
	/**
	 * �ر���Ϸ����
	 * @return
	 */
	public static ExecuteResult shut_up(Server server) {
		CmdInfo cmd = server.getAgent_online_server().getCmd_shut_down();
		try {
			//�Ƿ�����
			if(connServer(server).isFail()) return ExecuteResult.createFailResult("ssh ����ʧ��...");
			//ִ������
			ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd);
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+","+cmd.getShowName()+"����ʧ��...");
			}
			return ExecuteResult.createSuccResult("ִ��"+cmd+"�ɹ���");
		} catch (Exception e) {
			AppLog.error(server, "shut_up is error ....", e);
			return ExecuteResult.createFailResult("ִ��"+cmd+"ʧ��...");
		}
	}
	

	public static ExecuteResult ssh_pass_to_other_server(String target_dir,Server target_server,Server from_server,String from_dir) {
		if (connServer(from_server).isFail()) {
			return ExecuteResult.createFailResult(String.format("%s ssh cmd ����ʧ��...", from_server));
		}
	ExecuteResult result = from_server.getNet().getSsh_cmd().execCmd(new CmdInfo(
			String.format("sshpass -p '%s' scp -P %d -o StrictHostKeyChecking=no -r  %s %s@%s:%s",
					target_server.getSsh_password(),//����
					target_server.getSsh_port(),//�˿ں�
					from_dir,//����Ŀ¼
					target_server.getSsh_username(),//�˺�
					target_server.getSsh_host(),//��ַ
					target_dir),//Ŀ��Ŀ¼
			String.format("%s��%s�ַ������ļ�", from_server.toString(),target_server.toString())));
		return result;
}
}
