package com.zhou.core.xml;

import java.io.PrintWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


public final class XmlWriter{
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
			e.printStackTrace();
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
	
}
