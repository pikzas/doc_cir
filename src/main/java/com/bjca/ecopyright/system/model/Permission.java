package com.bjca.ecopyright.system.model;

import com.bjca.framework.bean.BaseBean;
import java.util.Date;

public class Permission extends BaseBean{

	private String id; //
	private String permissionname; //
	private int isuse; //
	private String description; //
	private String tablename; //
	private int isinsert; //
	private int isdelete; //
	private int isupdate; //
	private int isselect; //
	private Date createdate; //
	private Date updatedate; //
	
	public void setId(String id){
		this.id=id;
	}
	
	public void setPermissionname(String permissionname){
		this.permissionname=permissionname;
	}
	
	public void setIsuse(int isuse){
		this.isuse=isuse;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public void setTablename(String tablename){
		this.tablename=tablename;
	}
	
	public void setIsinsert(int isinsert){
		this.isinsert=isinsert;
	}
	
	public void setIsdelete(int isdelete){
		this.isdelete=isdelete;
	}
	
	public void setIsupdate(int isupdate){
		this.isupdate=isupdate;
	}
	
	public void setIsselect(int isselect){
		this.isselect=isselect;
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
	 
	public String getPermissionname(){
		return permissionname;
	}
	 
	public int getIsuse(){
		return isuse;
	}
	 
	public String getDescription(){
		return description;
	}
	 
	public String getTablename(){
		return tablename;
	}
	 
	public int getIsinsert(){
		return isinsert;
	}
	 
	public int getIsdelete(){
		return isdelete;
	}
	 
	public int getIsupdate(){
		return isupdate;
	}
	 
	public int getIsselect(){
		return isselect;
	}
	 
	public Date getCreatedate(){
		return createdate;
	}
	 
	public Date getUpdatedate(){
		return updatedate;
	}
}