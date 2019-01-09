package com.zyj.v1.ssh.online_server;

import java.io.File;

public class UpLoadFile {
	String path;
	File file;
	ResourceMapping mapping;
	

	public UpLoadFile(String path,File file) {
		this.path = path;
		this.file = file;
	}
	public UpLoadFile(String path,File file,ResourceMapping mapping) {
		this.path = path;
		this.file = file;
		this.mapping = mapping;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public ResourceMapping getMapping() {
		return mapping;
	}
	public void setMapping(ResourceMapping mapping) {
		this.mapping = mapping;
	}
	
}
