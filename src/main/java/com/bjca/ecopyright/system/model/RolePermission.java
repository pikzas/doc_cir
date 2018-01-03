package com.bjca.ecopyright.system.model;

import com.bjca.framework.bean.BaseBean;
import java.util.Date;

public class RolePermission extends BaseBean{

	private String id; //
	private String roleid; //角色id
	private String permissionid; //权限id
	private Date createdate; //创建时间
	private Date updatedate; //更新时间
	
	public void setId(String id){
		this.id=id;
	}
	
	public void setRoleid(String roleid){
		this.roleid=roleid;
	}
	
	public void setPermissionid(String permissionid){
		this.permissionid=permissionid;
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
	 
	public String getRoleid(){
		return roleid;
	}
	 
	public String getPermissionid(){
		return permissionid;
	}
	 
	public Date getCreatedate(){
		return createdate;
	}
	 
	public Date getUpdatedate(){
		return updatedate;
	}
}