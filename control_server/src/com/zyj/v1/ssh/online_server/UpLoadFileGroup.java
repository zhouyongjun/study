package com.zyj.v1.ssh.online_server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zyj.v1.gui.panel.OperateOnlineServerPanel;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.db.GameDAO;
import com.zyj.v1.ssh.util.SshUtil;

public class UpLoadFileGroup {
	List<UpLoadFile> select_files = new ArrayList<UpLoadFile>();
	boolean isAppJar;
	Set<String> mk_dirs = new HashSet<String>();
	List<CmdInfo> cmd_infos = new ArrayList<CmdInfo>();
	public UpLoadFileGroup() {
		
	}
	
	public boolean setNeedMappingFiles(String app_jar,OperateOnlineServerPanel panel,Server server,File[] files) {
		for (File file : files) {
			if (file.isHidden()) {
				continue;
			}
			if (file.isDirectory()) {
				boolean isTrue = setNeedMappingFiles(app_jar,panel,server,file.listFiles());
				if (!isTrue) {
					return false;
				}
			}else {
				ResourceMapping mapping = add_mk_dir(file);
				if (mapping == null) {
					AppLog.error(file.getName() +" is not mapping...");
					if (panel !=null && server != null)
						panel.addErrorResultMsg(server, "上传终止，检测到"+file.getName()+"没有对应的映射目录，无法确认正确的目录。。。");
					continue;
				}
				String path =  mapping.getPath();
				if (path == null) {
					AppLog.error(file.getName() +" mapping fail,path : "+path);
					if (panel !=null && server != null) panel.addErrorResultMsg(server, "上传终止，检测到"+file.getName()+"没有对应的映射目录，无法确认正确的目录。。。");
					continue;
				}
				if (file.getName().toLowerCase().equals(app_jar.toLowerCase())) {
					isAppJar = true;
				}
				UpLoadFile upload =  new UpLoadFile(path,file,mapping);
				select_files.add(upload);
				addCmdInfos(mapping.getCmd_infos());
			}
		}
		return true;
	}
	
	
	public void addCmdInfos(List<CmdInfo> infos) {
		if (infos == null || infos.size() == 0) {
			return;
		}
		
		for (CmdInfo info : infos) {
			addCmdInfo(info);
		}
		Collections.sort(cmd_infos, new Comparator<CmdInfo>() {

			@Override
			public int compare(CmdInfo o1, CmdInfo o2) {
				return o2.getSort()-o1.getSort();
			}
		});
	}

	public void addCmdInfo(CmdInfo info) {
		if (isExistCmdInfo(info)) {
			return;
		}		
		cmd_infos.add(info);	
	}

	private boolean isExistCmdInfo(CmdInfo info) {
		for (CmdInfo data : cmd_infos) {
			if (data.getCmd().equals(info.getCmd())) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		String temp  = "test\\a.xml";
		temp = temp.replaceAll("\\\\", "/");
		System.out.println(temp);
	}
	/**
	 * 服务器需要生成的目录
	 * @param file
	 * @return
	 */
	private  ResourceMapping add_mk_dir(File file) {
		try {
			String temp =  file.getParent()+"\\";
			temp = temp.replace(OnlineServerManager.DIR_UPLOAD_UPDATE,"");
			String path = temp.replaceAll("\\\\","/");
			if (path.length() <= 1) {
				path= "";
			}else {
				if (path.charAt(0) == '/') {
					path = path.substring(1);
				}	
			}
//			mk_dirs.add(path);
			return SshUtil.getResourceMaping(file.getName());
		} catch (Exception e) {
			AppLog.error(e);
			return null;
		}
	}

	public List<UpLoadFile> getSelect_files() {
		return select_files;
	}

	public void setSelect_files(List<UpLoadFile> select_files) {
		this.select_files = select_files;
	}

	public Set<String> getMk_dirs() {
		return mk_dirs;
	}

	public void setMk_dirs(Set<String> mk_dirs) {
		this.mk_dirs = mk_dirs;
	}
	
	public boolean isAppJar() {
		return isAppJar;
	}

	public void setAppJar(boolean isAppJar) {
		this.isAppJar = isAppJar;
	}
	
	

	public List<CmdInfo> getCmd_infos() {
		return cmd_infos;
	}

	public void setCmd_infos(List<CmdInfo> cmd_infos) {
		this.cmd_infos = cmd_infos;
	}

	/**
	 * 复制文件到指定目录
	 * @param targetDir
	 */
	public boolean copys(String targetDir) {
		for (UpLoadFile file : select_files) {
			if (!SshUtil.copyFile(file,targetDir)) return false;			
		}
		return true;
	}
	
}
