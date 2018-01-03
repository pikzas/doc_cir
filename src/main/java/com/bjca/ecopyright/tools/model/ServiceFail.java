package com.bjca.ecopyright.tools.model;

import java.util.Date;

/**
 * 服务接口调用错误
 * 
 * @author wangxf
 *
 */
public class ServiceFail {
	private String id ;
	/**
	 * 错误类型
	 */
	private Integer type ;
	/**
	 * 错误请求数据id
	 */
	private String failID;
	private Integer commitCount;
	private Date createDate;
	
	public Integer getCommitCount() {
		return commitCount;
	}
	public void setCommitCount(Integer commitCount) {
		this.commitCount = commitCount;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getFailID() {
		return failID;
	}
	public void setFailID(String failID) {
		this.failID = failID;
	}
	
	
}
