package com.bjca.ecopyright.filter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bjca.ecopyright.system.model.Menu;
import com.bjca.ecopyright.util.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.util.Function;


public class BackSecurityInterceptor  extends HandlerInterceptorAdapter {
	public static Log log = LogFactory.getLog(BackSecurityInterceptor.class);
    protected String[] patterns;
    private String loginView;

    public String getLoginView() {
        return loginView;
    }

    public void setLoginView(String loginView) {
        this.loginView = loginView;
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    public String[] getPatterns() {
        return patterns;
    }

    @Override
    public void afterCompletion(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj, Exception exception) throws Exception {
        super.afterCompletion(httpservletrequest, httpservletresponse, obj, exception);
    }

    @Override
    public void postHandle(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj, ModelAndView modelandview) throws Exception {
       
        super.postHandle(httpservletrequest, httpservletresponse, obj, modelandview);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getServletPath();
        if (matchAnyone(url)) {

            //2016-10-09 添加权限功能修改 开始
           List<Menu> menus = (ArrayList<Menu>) request.getSession().getAttribute("menus1");
            String menusJson = JsonUtils.objectToJsonString(menus);
            if (!menusJson.contains(url)) {
                Map map = new HashMap();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                map.put("statusCode", "301");
                map.put("message", "账户没有操作该菜单权限，请重新登录");
                out.write(Function.MaptoJSON(map));
                return false;
            }
            //2016-10-09 添加权限功能修改 结束

            if (needLogin(request)) {
                return true;
            } else {
                Map map = new HashMap();
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();  
                map.put("statusCode", "301");
                out.write(Function.MaptoJSON(map));
                return false;
            }

        } else {
            return true;
        }
    }

    //认证
    private boolean needLogin(HttpServletRequest request) {
        boolean flag=false;
        Admin admin=Admin.sessionAdmin();
        if(admin!=null){
        	flag=true;
        	
        }else{
        	log.debug("登录失败。。。。");
        }
        return flag;
       
    }

    //正则匹配
    protected boolean matchAnyone(String pattern) {
        for (int i = 0; i < patterns.length; i++) {
            if (matches(pattern, i)) {
                return true;
            }
        }
        return false;
    }

    protected boolean matches(String pattern, int patternIndex) {
        try {
            boolean matched = pattern.matches(patterns[patternIndex]);
            return matched;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
