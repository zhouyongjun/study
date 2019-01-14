package com.zhou.core.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class XmlReader {
	public static Document readDocument(String fileName)
	{
		Document doc = null;
		try {
            File file = new File(fileName);
            SAXReader reader=new SAXReader();
            //读取xml文件到Document中
            doc=reader.read(file);
		} catch (Exception e) {
            e.printStackTrace();
        }
		return doc;
	}
	
	public static Collection<Element> readElements(Element parentEl,String elemenetName)
	{
		List<Element> list = new ArrayList<>();
		try {
			for(Iterator<Element> i=parentEl.elementIterator(elemenetName);i.hasNext();){
				list.add(i.next());
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return list;
	}
	public static Element readElement(Element parentEl,String elemenetName)
	{
		return parentEl.element(elemenetName);
	}
	/**
	 * 读取当前节点下的指定Node的值 
	 * @param element
	 * @param name
	 * @return
	 */
	public static String readText(Element element,String name)
	{
		return element.elementText(name);
	}
	
	/**
	 * 取得节点的属性值
	 * @param element
	 * @param name
	 * @return
	 */
	public static String readAttribute(Element element,String name)
	{
		return element.attributeValue(name);
	}
	public static void main(String[] args) {

		Map<String,String> temp_map = new HashMap<String, String>();
		Document doc = XmlReader.readDocument(XmlReader.class.getClassLoader().getResource("newserver/new_server.xml").getPath());
		Collection<Element> collection = XmlReader.readElements(XmlReader.readElement(doc.getRootElement(), "params"), "param");
		for (Element el : collection)
		{
			String type = XmlReader.readAttribute(el, "type");
			String name = XmlReader.readAttribute(el, "name");
			System.err.println(type+":"+name);
		}
	
	}
}
