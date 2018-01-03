package com.bjca.ecopyright.system.model;

import java.util.Date;

import com.bjca.framework.bean.BaseBean;

public class OperationLog extends BaseBean{
	
	
	private String id;
	private String execSql;//执行sql
	private String execParameter;//sql执行参数
	private String execTable;//操作表名
	private String type;//操作类型
	private Date operationTime;//执行时间
	private String adminId;//操作人
	
	private Admin admin;
	
	
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getExecSql() {
		return execSql;
	}
	public void setExecSql(String execSql) {
		this.execSql = execSql;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getExecTable() {
		return execTable;
	}
	public void setExecTable(String execTable) {
		this.execTable = execTable;
	}
	public String getExecParameter() {
		return execParameter;
	}
	public void setExecParameter(String execParameter) {
		this.execParameter = execParameter;
	}
	

}
