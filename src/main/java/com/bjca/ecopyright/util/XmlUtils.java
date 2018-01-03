package com.bjca.ecopyright.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class XmlUtils {

	// /**
	// * @description 将xml字符串转换成map
	// * @param xml
	// * @return Map
	// */
	// public static Map readStringXmlOut(String xml) {
	// Map map = new HashMap();
	// Document doc = null;
	// try {
	// doc = DocumentHelper.parseText(xml);
	//
	// Element rootElt = doc.getRootElement(); // 获取根节点
	//
	// Iterator iter = rootElt.elementIterator();
	//
	// // 遍历head节点
	//
	// while (iter.hasNext()) {
	// Element recordEle = (Element) iter.next();
	// map.put(recordEle.getName(), recordEle.getText());
	//
	// }
	// } catch (DocumentException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return map;
	// }
	//
	// /**
	// * 从xml中获取指定标签的值（只适用于相同标签只出现一次，且深度为二）
	// *
	// * @param xml
	// * @param key
	// * @return
	// */
	// public static String readStringXmlOut(String xml, String key) {
	//
	// Document doc = null;
	// try {
	// xml = xml.trim();
	// doc = DocumentHelper.parseText(xml);
	//
	// Element rootElt = doc.getRootElement(); // 获取根节点
	//
	// Iterator iter = rootElt.elementIterator();
	// // 遍历head节点
	//
	// while (iter.hasNext()) {
	// Element recordEle = (Element) iter.next();
	// if (key.equals(eachElement(recordEle,key))) {
	// return recordEle.getText();
	// }
	// // map.put(recordEle.getName(), recordEle.getText());
	// }
	// } catch (DocumentException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	public static String getValue(String path, String key) {
		try {
			// 构建xml解析工厂
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			// 构建具体的xml解析器
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			// 获得当前xml的文档
			Document document = documentBuilder.parse(path);
			// 获得当前文档的根元素
			document.normalize();
			// Element root = document.getDocumentElement();
			NodeList nodeList = document.getElementsByTagName(key);
			Element element = (Element) nodeList.item(0);
			return element.getTextContent();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;

	}

	public static void main(String argv[]) {
		String jsonpath = "D:/tmp/pdfcertstamp/ybq_export.txt";
		String parseJson = Function.readTxtFile(jsonpath);
		JSONObject dataJson = new JSONObject(parseJson);
		JSONArray data = dataJson.getJSONArray("message");
		// System.out.println(data);
		JSONObject info = data.getJSONObject(0);
		String strDate = info.get("signDate") + "";
		System.out.println(MyDate.get(strDate, "yyyy-MM-dd hh:mm:ss.S"));

		// "res/";
	}

}
