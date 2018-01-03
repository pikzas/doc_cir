package com.bjca.framework.config;

import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.velocity.app.Velocity;

import com.bjca.ecopyright.util.Function;

/**
 * 配置多站模式、模块分布部署配置信息
 * 
 * @author wangtj
 * 
 */
public class InitConfig  extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void destroy() { 
		super.destroy(); 
	}

	public void init() throws ServletException {
		System.out.println("............ 系统初始化");
		Function.initPop();
		Properties properties = new Properties();
		// 指定模板文件加载位置 velocity.properties
		properties.put("file.resource.loader.path", getServletContext().getRealPath("/WEB-INF/dev"));
		// 指定输入编码格式
		properties.put("input.encoding", "UTF-8");
		// 指定velocity的servlet向浏览器输出内容的编码
		properties.put("default.contentType", "text/html;charset\\=UTF-8");
		// 指定输出编码格式
		properties.put("output.encoding", "UTF-8");
		properties.put("file.resource.loader.class ", "org.apache.velocity.tools.view.WebappResourceLoader");
		// 迭代索引名
		properties.put("directive.foreach.counter.name", "index");
		// 迭代开始的下标
		properties.put("directive.foreach.counter.initial.value", "0");
		try {
			Velocity.init(properties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
