package com.bjca.ecopyright.system.model;

import com.bjca.framework.bean.BaseBean;

import java.util.Date;

public class RoleMenu extends BaseBean {

	private String id; //
	private String menuId; //菜单id
	private String roleId; //权限id
	private String functions; //功能说能
	private Date createDate; //创建时间
	private Date updateDate; //修改时间
	private Menu menu = null;


	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public void setId(String id){
		this.id=id;
	}
	
	public void setMenuId(String menuId){
		this.menuId=menuId;
	}
	
	public void setRoleId(String roleId){
		this.roleId=roleId;
	}
	
	public void setFunctions(String functions){
		this.functions=functions;
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
	 
	public String getMenuId(){
		return menuId;
	}
	 
	public String getRoleId(){
		return roleId;
	}
	 
	public String getFunctions(){
		return functions;
	}
	 
	public Date getCreateDate(){
		return createDate;
	}
	 
	public Date getUpdateDate(){
		return updateDate;
	}
}