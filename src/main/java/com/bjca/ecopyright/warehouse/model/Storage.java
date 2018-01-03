package com.bjca.ecopyright.warehouse.model;

import java.util.Date;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.framework.bean.BaseBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(value={"handler"})
public class Storage extends BaseBean{

	private String id; //id
	private String fid; //父id
	private int level;//层级
	private String groupname;//组名字
	private String name; //名称
	private int houseorder;//仓库次序
	private String mark; //标记-------------仓库类型标记
	private int totalspace; //总容纳量
	private int usespace; //已容纳量
	private int surplus;//剩余量
	private String description; //描述
	private int isabandon; //是否弃用
	private int isuse; //是否占用
	private int locksign;//锁定标识(只有二级菜单)
	private String abandonreason; //废弃原因
	private Date createdate; //创建时间
	private Date updatedate; //更新时间
	
	@JsonIgnore
	private Software software;
	
	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public void setId(String id){
		this.id=id;
	}
	
	public void setFid(String fid){
		this.fid=fid;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setMark(String mark){
		this.mark=mark;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public void setIsabandon(int isabandon){
		this.isabandon=isabandon;
	}
	
	public void setIsuse(int isuse){
		this.isuse=isuse;
	}
	
	public void setAbandonreason(String abandonreason){
		this.abandonreason=abandonreason;
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
	 
	public String getFid(){
		return fid;
	}
	 
	public String getName(){
		return name;
	}
	 
	public String getMark(){
		return mark;
	}
	 
	public String getDescription(){
		return description;
	}
	 
	public int getIsabandon(){
		return isabandon;
	}
	 
	public int getIsuse(){
		return isuse;
	}
	 
	public String getAbandonreason(){
		return abandonreason;
	}
	 
	public Date getCreatedate(){
		return createdate;
	}
	 
	public Date getUpdatedate(){
		return updatedate;
	}

	public int getTotalspace() {
		return totalspace;
	}

	public void setTotalspace(int totalspace) {
		this.totalspace = totalspace;
	}

	public int getUsespace() {
		return usespace;
	}

	public void setUsespace(int usespace) {
		this.usespace = usespace;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHouseorder() {
		return houseorder;
	}

	public void setHouseorder(int houseorder) {
		this.houseorder = houseorder;
	}

	public int getLocksign() {
		return locksign;
	}

	public void setLocksign(int locksign) {
		this.locksign = locksign;
	}

	public int getSurplus() {
		return surplus;
	}

	public void setSurplus(int surplus) {
		this.surplus = surplus;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	
	
}