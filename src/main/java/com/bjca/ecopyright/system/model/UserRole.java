package com.bjca.ecopyright.system.model;


import com.bjca.framework.bean.BaseBean;

import java.util.Date;

public class UserRole extends BaseBean {

	private String id; //
	private String adminId; //用户id
	private String roleId; //角色id
	private Date createDate; //创建时间
	private Date updateDate; //修改时间
	private Role role;//用户角色列表
	
	public void setId(String id){
		this.id=id;
	}
	
	public void setAdminId(String adminId){
		this.adminId=adminId;
	}
	
	public void setRoleId(String roleId){
		this.roleId=roleId;
	}
	
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	
	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}
	 
	public String getId(){
		return id;
	}
	 
	public String getAdminId(){
		return adminId;
	}
	 
	public String getRoleId(){
		return roleId;
	}
	 
	public Date getCreateDate(){
		return createDate;
	}
	 
	public Date getUpdateDate(){
		return updateDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}