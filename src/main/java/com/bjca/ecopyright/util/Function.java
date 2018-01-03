package com.bjca.ecopyright.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.bjca.ecopyright.statuscode.AdminRoleStatusEnum;
import com.bjca.ecopyright.statuscode.SoftWareStatusEnum;
import com.bjca.ecopyright.statuscode.SoftwareOperationEnum;
import com.bjca.ecopyright.statuscode.StorageStatusEnum;
import com.bjca.ecopyright.system.service.OperationLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 工具类
 */
public class Function {
	public static Log log = LogFactory.getLog(Function.class);
	public static ApplicationContext webcontext;
	public static Properties prop = null;

	public static void initPop() {
		try {
			prop = new Properties();// 属性集合对象
			InputStream in = Function.class.getResourceAsStream("/function.properties");
			prop.load(in);// 将属性文件流装载到Properties对象中
			in.close();// 关闭流
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取当前操作系统
	 * 
	 * @return
	 */
	public static String optionSystem() {
		String sys = "";
		if (System.getProperties().get("os.name").toString().toLowerCase().contains("windows")) {
			sys = "WINDOWS";
		} else {
			sys = "LINUX";
		}
		return sys;

	}
	
	/**
	 * 获取配置文件中流水号合法长度
	 * @return
	 */
	public static int getLeagalLengthOfSerialNum(){
		int ret = -1;
		try{
			String num = prop.getProperty("serialNumLength");
			ret = Integer.parseInt(num);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 获取设置memcached中数据存活的时间
	 * @return
	 */
	public static int getMemcachedExpireTime(){
		int ret = -1;
		try{
			String num = prop.getProperty("memcachedExpireTime");
			ret = Integer.parseInt(num);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 获取上传文件的缓存地址
	 * @return
	 */
	public static String getTempFileDir(){
		String path = "";
		try{
			path = prop.getProperty("tempFileDir");
		}catch(Exception e){
			e.printStackTrace();
		}
		return path;
	}
	
	/**
	 * 获取xml对应key的值
	 * 
	 * @param path
	 * @param key
	 * @return
	 */
	public static String getXMLValue(String path, String key) {
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
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 功能：Java读取txt文件的内容 步骤：
	 * 
	 * 1：先获得文件句柄 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取 3：读取到输入流后，需要读取生成字节流
	 * 4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
	 * 
	 * @param filePath
	 */
	public static String readTxtFile(String filePath) {
		String result = "";
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// System.out.println(lineTxt);
					result += lineTxt;
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return result;
	}

	public static String MD5(String s) {
		String result = "";
		try {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

			byte[] btInput = s.getBytes();

			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			// 使用指定的字节更新摘要
			mdInst.update(btInput);

			// 获得密文
			byte[] md = mdInst.digest();

			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			result = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static boolean isEmpty(String s) {
		return s == null || s.equals("");
	}

	public static boolean isEmpty(Collection coll) {
		return CollectionUtils.isEmpty(coll);
	}

	public static boolean isEmpty(Map map) {
		return CollectionUtils.isEmpty(map);
	}

	public static boolean isEmpty(String[] strs) {
		return strs == null || strs.length <= 0;
	}

	public static int parseInt(String s, int defaultValue) {
		if (s == null || "".equals(s)) {
			return defaultValue;
		}

		int nValue = defaultValue;
		try {
			nValue = Integer.parseInt(s);
		} catch (Exception e) {
		}
		return nValue;
	}

	/**
	 * 
	 * 
	 * @param pattern
	 *            返回符合传入格式的当前的日期的字符串
	 * @return String
	 */
	public static String getDateStringByPattern(String pattern) {
		Date date = new Date();
		return getDateStringByPattern(date, pattern);
	}

	/**
	 * 返回符合传入格式的传入日期的字符串
	 * 
	 * @param date
	 *            ( java.core.Date )
	 * @param pattern
	 *            日期格式为"yyyy-MM-dd" 或者"yyyy-MM-dd HH:mm:ss"或者"HH:mm:ss"
	 * @return String
	 */
	public static String getDateStringByPattern(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		String str = sf.format(date);

		return str;
	}

	/**
	 * 把当前时间转化成sql 的 Timestamp类型
	 * 
	 * @ return 返回Timestamp类型的时间
	 */
	public static Timestamp getDateTime() {
		java.util.Date date = new java.util.Date();
		return (new java.sql.Timestamp(date.getTime()));
	}

	/**
	 * the following const is to define date format.
	 */

	public static java.util.Date parseDate(String strDate, String pattern) {
		if (isEmpty(strDate)) {
			return null;
		}
		java.text.SimpleDateFormat fmtDate = null;
		java.text.ParsePosition pos = new java.text.ParsePosition(0);
		fmtDate = new java.text.SimpleDateFormat(pattern);

		java.util.Date dtRet = null;
		try {
			return dtRet = fmtDate.parse(strDate, pos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtRet;

	}

	/**
	 * 得到当前工程的根目录
	 * 
	 * @return
	 */
	private static String _appPath = null;

	public static final String getAppPath() {
		if (isEmpty(_appPath)) {

			String path = Function.class.getResource("").toString();

			if (System.getProperties().get("os.name").toString().toLowerCase().contains("windows")) {
				path = path.substring(6, path.indexOf("WEB-INF"));
			} else {
				path = path.substring(5, path.indexOf("WEB-INF"));
			}

			_appPath = path;
		}
		return _appPath;
	}

	public static String forMatDate(String l, String pattern) {
		try {
			return getDateStringByPattern(new Date(Long.valueOf(l)), pattern);
		} catch (Exception e) {
			return getDateStringByPattern(pattern);
		}
	}







	/**
	 * 消息提示
	 * 
	 * @param context
	 */
	public static void setTip(String context) {
		tipConfig(null, context);
	}

	private static void tipConfig(String key, String context) {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		if (key == null) {
			key = "tip";
		}
		session.setAttribute(key, context);
	}




	/**
	 * map转成json
	 * 
	 * @param map
	 * @return
	 */
	public static String MaptoJSON(Map map) {
		ObjectMapper mapper = new ObjectMapper(); // 专成json
		try {
			return mapper.writeValueAsString(map).toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";

	}

	/**
	 * 获取注解对象
	 * 
	 * @return
	 */
	private static ApplicationContext initWebApplicationContext() {

		if (webcontext == null) {
			webcontext = new ClassPathXmlApplicationContext(new String[] { "spring-dataSource.xml", "app-ibatis.xml" });
		}
		return webcontext;
	}



	/**
	 * 根据枚举类型和名称 获取中文描述
	 * 
	 * @param type
	 * @param statusName
	 * @return
	 */
	public static String getEnumName(String type, String statusName) {

		String result = "";

		switch (type) {
		case "SoftWareStatusEnum":
			result = SoftWareStatusEnum.getStatusName(statusName);
			break;
		case "StorageStatusEnum":
			result = StorageStatusEnum.getStatusName(statusName);
			break;
		case "AdminRoleStatusEnum":
			result = AdminRoleStatusEnum.getStatusName(statusName);
			break;
		case "SoftwareOperationEnum":
			result = SoftwareOperationEnum.getStatusName(statusName);
			break;
		default:
			break;
		}

		return result;

	}

	/**
	 * 根据枚举类型和名称 获取状态值
	 * 
	 * @param type
	 * @param statusName
	 * @return
	 */
	public static String getEnumValue(String type, String statusName) {
		String result = null;

		switch (type) {
		case "SoftWareStatusEnum":
			result = SoftWareStatusEnum.getStatusValue(statusName).toString();
			break;
		case "StorageStatusEnum":
			result = StorageStatusEnum.getStatusValue(statusName).toString();
			break;
		case "AdminRoleStatusEnum":
			result = AdminRoleStatusEnum.getStatusValue(statusName).toString();
			break;
		case "SoftwareOperationEnum":
			result = SoftwareOperationEnum.getStatusValue(statusName).toString();
			break;
		default:
			break;
		}

		return result;

	}

	/***
	 * 系统表操作日志
	 * @date 		2016-5-3下午7:02:14
	 * @mail 		humin@bjca.org.cn
	 * @author 		humin
	 * @param operationLogSql
	 * @param Parameter
	 */
	public static void sysExecLog(String operationLogSql,String parameterJson){
		OperationLogService operationLogService = (OperationLogService) initWebApplicationContext().getBean("operationLogService");
		operationLogService.batchProcess(operationLogSql,parameterJson);
	}


	public static void main(String[] args) throws Exception {

		String string = MyDate.toString(new Date(), "yyyy");
		System.out.println(string);
		// HttpClientCommon clientCommon = new HttpClientCommon();
		// String url = "http://www.fynas.com/workday/count";
		// String content="end_date=2015-12-08&start_date=2015-12-08";
		//
		// String string = clientCommon.post(url, content,
		// "application/x-www-form-urlencoded");
		// System.out.println(string);
		// String key=JsonUtils.parseJson(string, "total");
		// //System.out.println(map.get("total"));
		// System.out.println(key);
	}

}
