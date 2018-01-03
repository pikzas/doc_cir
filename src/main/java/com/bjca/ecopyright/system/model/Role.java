package com.bjca.ecopyright.system.model;

import com.bjca.framework.bean.BaseBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Role extends BaseBean{

	private String id; //
	private String name; //角色名称
	private String description; //角色描述
	private Integer sort; //顺序
	private Date createdate; //创建时间
	private Date updatedate; //修改时间
	private List<RoleMenu> roleMenus = new ArrayList<>();

	public List<RoleMenu> getRoleMenus() {
		return roleMenus;
	}

	public void setRoleMenus(List<RoleMenu> roleMenus) {
		this.roleMenus = roleMenus;
	}

	public void setId(String id){
		this.id=id;
	}

	public void setName(String name){
		this.name=name;
	}

	public void setDescription(String description){
		this.description=description;
	}

	public void setSort(Integer sort){
		this.sort=sort;
	}

	public void setCreatedate(Date createdate){
		this.createdate=createdate;
	}

	public void setUpdatedate(Date updatedate){
		this.updatedate=updatedate;
	}

	public String getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public Integer getSort(){
		return sort;
	}

	public Date getCreatedate(){
		return createdate;
	}

	public Date getUpdatedate(){
		return updatedate;
	}
}