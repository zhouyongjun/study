package com.zyj.v1.ssh.util.xml;

import java.util.ArrayList;
import java.util.List;


	/**
	 * XML文件生成
	 * @author zhouyongjun
	 *
	 */
public class XmlDocument {
	private String fileName;//文件名字
	private String rootElementName;//根节点名字
	private List<XmlNode> nodesList;
	public XmlDocument(String fileName,String rootElementName) {
		this.fileName = fileName;
		this.rootElementName = rootElementName;
	}
	public String getFeleName() {
		return fileName;
	}
	public void setFileName(String feleName) {
		this.fileName = feleName;
	}
	public String getRootElementName() {
		return rootElementName;
	}
	public void setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
	}
	public List<XmlNode> getNodesList() {
		return nodesList;
	}
	public void addNode(XmlNode node) {
		if (nodesList == null) {
			nodesList =  new ArrayList<XmlNode>();
		}
		nodesList.add(node);
	}
	
}
