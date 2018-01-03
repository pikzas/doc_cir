package com.bjca.ecopyright.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RecurrenceXMl {

	/***
	 * 
	 * 使用递归方式解析XML
	 * 
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws Exception  {

		// 构建xml解析工厂
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		// 构建具体的xml解析器
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		// 获得当前xml的文档
		
		Document document = documentBuilder.parse("D:/tmp/pdfcertstamp/ybq_export.xml");
		// 获得当前文档的根元素
		document.normalize(); 
		//Element root = document.getDocumentElement();
		
		NodeList nodeList = document.getElementsByTagName("name");
		Element element = (Element) nodeList.item(0);   
		System.out.println(element.getNodeName()+" :"+element.getTextContent());
		//System.out.println(recurrence(root, "name"));
	}

	// 递归解析xml的方法
	private static String recurrence(Element element, String key) {
		// System.out.print("<" + element.getNodeName());
		String result = null;
		NodeList childNodes = element.getChildNodes();
//		// 得到当前节点上的所有属性
//		NamedNodeMap nodeMap = element.getAttributes();
//		if (null != nodeMap) {
//			for (int j = 0; j < nodeMap.getLength(); j++) {
//				Attr attr = (Attr) nodeMap.item(j);
//				// 属性的名称
//				String attrName = attr.getNodeName();
//				// 属性的值
//				String attrValue = attr.getNodeValue();
//				// System.out.print(" " + attrName + "=\"" + attrValue + "\"");
//			}
//		}
		// System.out.print(">");
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			// 得到当前节点下的节点
			short nodeType = childNode.getNodeType();
			// 当前类型是元素类型
			if (nodeType == Node.ELEMENT_NODE) {
				// 继续递归
				String str=recurrence((Element) childNode, key);
				if(!Function.isEmpty(str)){
					return str;
				}
			}
			// 当前元素是文本
			else if (nodeType == Node.TEXT_NODE) {
				// 递归出口
				if("name".equals(element.getNodeName())){
					
				}
			}
		}
		return result;
		// System.out.println("</" + element.getNodeName() + ">");
	}

}