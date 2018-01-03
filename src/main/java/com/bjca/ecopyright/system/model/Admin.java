package com.bjca.ecopyright.system.model;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bjca.ecopyright.statuscode.Constants;
import com.bjca.framework.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class Admin extends BaseBean{
	
	public static Log log = LogFactory.getLog(Admin.class);
	
	private String ID; //
	private String loginName; //登录名称
	private String password; //登录密码、证书密码
	private String email; //电子邮箱
	private String phone; //电话
	private String relName; //姓名、证书名
	private String state; //状态    1 正常，2作废，0禁用
	private String roleId; //角色--------角色字段没有
	private String type;//类别----为了雍和分配分审员
	private String authorizationcode; //授权码
	
	private List<UserRole> userRoleList = new ArrayList<>();;//用户角色关联表
	

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRelName() {
		return relName;
	}
	public void setRelName(String relName) {
		this.relName = relName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getAuthorizationcode() {
		return authorizationcode;
	}
	public void setAuthorizationcode(String authorizationcode) {
		this.authorizationcode = authorizationcode;
	}

	public List<UserRole> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<UserRole> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	/**
	 * 获取session中账号信息
	 * @return
	 */
	public static Admin sessionAdmin() {
		try {
			HttpSession session=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			Admin admin=session.getAttribute(Constants.SESSION_ADMIN) != null ? (Admin) session.getAttribute(Constants.SESSION_ADMIN):null;
			return admin;
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			log.debug("用户没有登录············");
		}
		return null;
		
		
	}
	/**
	 * 更新session中用户
	 * @param admin
	 * @return
	 */
	public static boolean updateSessionAdmin(Admin admin) {
		boolean flag=false;
		try{
		HttpSession session=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		session.setAttribute(Constants.SESSION_ADMIN, admin);
		flag=true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flag;
	

	}
}