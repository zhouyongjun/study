package com.zyj.v1.ssh.open_new_server;

import java.util.HashMap;
import java.util.Map;

import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.util.Const;
import com.zyj.v1.ssh.util.SshUtil;
import com.zyj.v1.ssh.util.xml.XmlDocument;
import com.zyj.v1.ssh.util.xml.XmlNode;

	/**
	 * 
	 * @author zhouyongjun
	 *�����ļ���
	 *	1������֮ǰ�ķ�����
	 *  	��ɾ��ԭ����db
	 *  	�ڴ����µ�db
	 *      �۵��������ݵ����ݿ�ṹ
	 *      �ܵ���һЩ����ı����� 
	 *      ��ͬ���ļ�Ŀ¼������
	 *  2)��������
	 *      �����Ŀ¼�������򴴽���Ҫ����ĿĿ¼
	 *      ���޸ı�Ҫ���ļ�
	 *      �����������ݿ�
	 *      �������Ҫ�����ݿⲻ�����򴴽�
	 *      �ݵ������ݿ�ṹ
	 *      �޵�����Ҫ�ı�����
	 *      ���޸ı��������ֶ�
	 *      
	 *  3)�����������������־
	 */
public class NewServerManager {
	private static NewServerManager instance = new NewServerManager();
	TableNewServerAutoIncrement table_auto_increment =new TableNewServerAutoIncrement();
	TableCommonDBResourceFiles table_common_db_resource_files =new TableCommonDBResourceFiles();
	TableNewServerUpdateFiles table_new_server_files_config =new TableNewServerUpdateFiles();
	TableNewServerParams table_new_server_params_config =new TableNewServerParams();
	TableNewServerDeleteFiles table_new_server_delete_files=new TableNewServerDeleteFiles();
	TableOldServerDBDumpTables table_old_server_db_dump_tables =new TableOldServerDBDumpTables();
	TableOldServerScpDirs table_old_server_scp_dirs_config = new TableOldServerScpDirs();
	Map<NewServerParam, String> params_map = new HashMap<NewServerParam, String>();	
	private NewServerManager() {
		
	}
	public static NewServerManager getInstance() {
		return instance;
	}
	
	public void init() {
		table_auto_increment.load();
		table_new_server_files_config.load();
		table_new_server_params_config.load();
		table_new_server_delete_files.load();
		table_common_db_resource_files.load();
		table_old_server_db_dump_tables.load();
		table_old_server_scp_dirs_config.load();
	}
	public TableNewServerAutoIncrement getTable_auto_increment() {
		return table_auto_increment;
	}
	public void setTable_auto_increment(TableNewServerAutoIncrement table_auto_increment) {
		this.table_auto_increment = table_auto_increment;
	}
	public TableCommonDBResourceFiles getTable_common_db_resource_files() {
		return table_common_db_resource_files;
	}
	public TableNewServerUpdateFiles getTable_new_server_update_files() {
		return table_new_server_files_config;
	}
	public TableNewServerParams getTable_new_server_params_config() {
		return table_new_server_params_config;
	}
	public TableOldServerDBDumpTables getTable_old_server_db_dump_tables() {
		return table_old_server_db_dump_tables;
	}
	
	public TableOldServerScpDirs getTable_old_server_scp_dirs_config() {
		return table_old_server_scp_dirs_config;
	}
	public TableNewServerDeleteFiles getTable_new_server_delete_files() {
		return table_new_server_delete_files;
	}
	/**
	 * �������в�����Ӧֵ
	 * @param ssh_ip ����ID
	 * @param ssh_dir ����·�� 
	 * @return boolean  ���ֵ�д���null ���� �մ� �����ʧ��
	 */
	public ExecuteResult updateParams_map(String ssh_ip,String server_path){
		params_map.clear();
		params_map.put(NewServerParam.APP_IP, ssh_ip);
		params_map.put(NewServerParam.APP_PATH, server_path);
		NewServerParam[] new_server_params = table_new_server_params_config.getNew_server_params();
		for (int i=0;i< new_server_params.length;i++) {
			params_map.put(new_server_params[i],table_new_server_params_config.getValue(i, 1));
		}
		for (NewServerParam param : NewServerParam.values()) {
			String val = params_map.get(param);
			if (val == null || val.length() == 0) {
				return ExecuteResult.createFailResult("["+param.getChName()+"="+val+"]");
			}
		}
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 * ��ȡ����������ֵ
	 * @param param
	 * @return
	 */
	public String getNewServerParamValue(NewServerParam param) {
		return params_map.get(param);
	}
	public Map<NewServerParam, String> getParams_map() {
		return params_map;
	}
	public void setParams_map(Map<NewServerParam, String> params_map) {
		this.params_map = params_map;
	}
	private void unload_table_new_server_auto_increment() {
		XmlDocument doc = new XmlDocument(TableNewServerAutoIncrement.FILE_NAME, "tables");
		for (String[] values : table_auto_increment.getConfigs()) {
			XmlNode node = new XmlNode();
			node.setElementName("config");
			node.addAttribute("table", values[0]);
			node.addAttribute("add_val", values[1]);
			node.addAttribute("init_val", values[2]);
			doc.addNode(node);
		}
		SshUtil.writeToXml(doc);
	}
	public void update() {
		update_table_new_server_auto_increment();
		unload_table_new_server_auto_increment();
		update_table_new_server_params();
		unload_table_new_server_params();
	}
	
	private void unload_table_new_server_params() {
		XmlDocument doc = new XmlDocument(TableNewServerParams.FILE_NAME, "tables");
		String[][] to2Arrays = table_new_server_params_config.getConfigs();
		for (int i=0;i<to2Arrays.length;i++) {
			String[] values = to2Arrays[i];
			XmlNode node = new XmlNode();
			node.setElementName("config");
			node.addAttribute("name", values[0]);
			node.addAttribute("value", values[1]);
			node.addAttribute("isSave", values[2]);
			node.addAttribute("key", table_new_server_params_config.getNew_server_params()[i].name());
			doc.addNode(node);
		}
		SshUtil.writeToXml(doc);
	}
	public void update_table_new_server_params() {
		for (String[] values : table_new_server_params_config.getConfigs()) {
			if (values[2].equals("1")) {//����
				
			}else if (values[2].equals("2")) {//��������ԭ���Ļ�����+1
				String value = values[1];
				boolean isHex = value.contains("0x");
				int int_val = 0;
				if (isHex) {
					String no_head = value.replace("0x", "");
					int_val = Integer.parseInt(no_head,16);
					int_val ++;
					String toString = Integer.toHexString(int_val);
					int size = no_head.toCharArray().length - toString.toCharArray().length; 
					if (size > 0) {
						for (int i=0;i<size;i++) {
							toString = "0"+toString;
						}
					}
					values[1] = "0x"+toString.toUpperCase();
				
				}else {
					int_val = Integer.parseInt(value);
					int_val ++;
					values[1] = int_val+"";
				}
			}else {
				values[1] = "";
			}
		}
	
	}
	public void update_table_new_server_auto_increment() {
		for (String[] values : table_auto_increment.getConfigs()) {
			int add_val  = values[1].length() == 0 ? 0 : Integer.parseInt(values[1]);
			int init_val  = values[2].length() == 0 ? 1 : Integer.parseInt(values[2]);
			if (add_val == 0) {
				continue;
			}
			init_val += add_val;
			values[2] =init_val + "";
		}
	}
	
	/**
	 * ���String��������key��ȫ��value��ֵ
	 * @param replace
	 * @return
	 */
	public String getReplaceServerParamValue(String replace) {
		for (NewServerParam param : NewServerParam.values()) {
			String toString = param.toString(); 
			if (replace.contains(toString)) {
				String value =getNewServerParamValue(param);
				switch (param) {
				case APP_NAME:
				{
					value= gbEncoding(value);
					break;
				}
				case SQL_PASSWORD:
				{
					value = SshUtil.getStringEOR(value, Const.EOR_KEY);
					break;
				}
				}
				replace = replace.replaceAll(toString, value);
			}
		}
		return replace;
	}
	 public static String gbEncoding(final String gbString) {        
	        char[] utfBytes = gbString.toCharArray();              
	        String unicodeBytes = "";               
	        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) { 
	            String hexB = Integer.toHexString(utfBytes[byteIndex]);                      
	            if (hexB.length() <= 2) {                          
	                hexB = "00" + hexB;                    
	                }                      
	            unicodeBytes = unicodeBytes + "\\\\\\\\u" + hexB;                  
	            }                  
	        return unicodeBytes;
	 }
	
}
