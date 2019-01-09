package com.zyj.v1.ssh.util.xml;

import java.util.ArrayList;
import java.util.List;

public class XmlNode {
	private String elementName;//�ڵ�����
	private String text;//����
	List<XmlNode> nodesList;//�����ڵ�LIST
	List<String[]> attributesList;//�ڵ����Լ���ֵ
	
	/**
	 * �������Լ���ֵ
	 * @param attri
	 * @param value
	 */
	public void addAttribute(String attri,String value) {
		if (attributesList == null) {
			attributesList = new ArrayList<String[]>();
		}
		attributesList.add(new String[]{attri,value});
	}
	
	/**
	 * ���ӽڵ�
	 * @param node
	 */
	public void addGmXmlNode(XmlNode node) {
		if (nodesList == null) {
			nodesList = new ArrayList<XmlNode>();
		}
		nodesList.add(node);
	}
	
	
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<XmlNode> getNodesList() {
		return nodesList;
	}

	public List<String[]> getAttributesList() {
		return attributesList;
	}
}
