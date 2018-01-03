package com.bjca.ecopyright.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;




public class StageSecurityInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(StageSecurityInterceptor.class);

	/**
	 * 免登入 免检查地址
	 */
	public String[] allowUrls;// 还没发现可以直接配置不拦截的资源，所以在代码里面来排除

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String contentURL = request.getServletPath();
		logger.debug(contentURL);
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
		System.out.println(requestUrl);
		/*if (null != allowUrls && allowUrls.length >= 1) {
			for (String url : allowUrls) {
				if (requestUrl.contains(url)) {
					return true;
				}
			}
		}*/
		if(requestUrl.contains("center")){
			
			Object obj= request.getSession().getAttribute("member");
			if(obj!=null){
				return true;
			}else{
				response.sendRedirect("/login.jhtml");
				return false;
			}
		}
		//UserInfo member = (UserInfo) request.getSession().getAttribute("member");//后台用户
		/*if (member != null) {
			return true; // 返回true，则这个方面调用后会接着调用postHandle(), afterCompletion()
		} else {
			// 未登录 跳转到登录页面
			response.sendRedirect("/login.php");
			return false;

		}*/
		return true;

	}
}
