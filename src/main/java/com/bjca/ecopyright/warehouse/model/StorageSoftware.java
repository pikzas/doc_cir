package com.bjca.ecopyright.warehouse.model;

import com.bjca.framework.bean.BaseBean;
import java.util.Date;

public class StorageSoftware extends BaseBean{

	private String id; //
	private String softwareid; //软件id
	private String storageid; //仓库id
	private Date createdate; //创建时间
	private Date updatedate; //更新时间
	private Storage storage; //更新时间
	
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	public void setId(String id){
		this.id=id;
	}
	
	public void setSoftwareid(String softwareid){
		this.softwareid=softwareid;
	}
	
	public void setStorageid(String storageid){
		this.storageid=storageid;
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
	 
	public String getSoftwareid(){
		return softwareid;
	}
	 
	public String getStorageid(){
		return storageid;
	}
	 
	public Date getCreatedate(){
		return createdate;
	}
	 
	public Date getUpdatedate(){
		return updatedate;
	}
}