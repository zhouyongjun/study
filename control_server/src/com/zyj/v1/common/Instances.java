package com.zyj.v1.common;

import com.zyj.v1.ssh.SSHManager;
import com.zyj.v1.ssh.db.DBManager;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.open_new_server.NewServerManager;

public interface Instances {
	SSHManager sshMgr = SSHManager.geteInstance();
	NewServerManager nsMgr = NewServerManager.getInstance();
	OnlineServerManager onsMgr = OnlineServerManager.getInstance();
	DBManager dbMgr = DBManager.getInstance();
}
