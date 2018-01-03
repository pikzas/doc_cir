package com.bjca.ecopyright.soft.model;

import java.io.Serializable;
import java.util.Date;

import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.ecopyright.warehouse.model.StorageSoftware;
import com.bjca.framework.bean.BaseBean;

public class SoftwareOperationLog extends BaseBean {

	private String id; //id
	private String adminid; //操作员id
	private String trialId;//分审员id
	private Date acceptdate; //接受日期
	private String serialnum; //流水号
	private String applyperson; //申请人
	private String softwarename; //软件全称
	private String salesman; //业务员
	private Date certificatedate; //出证日期
	private int softwarestatus; //案件状态1.待缴费2.待分审3.已分审4.待补正5.待制证6.已制证
	private int isexist; //是否在库
	private String description; //备注
	private String temphouseorder;//临时仓库记录
	private String matchsign;//匹配状态
	private Date createdate; //创建时间
	private Date updatedate; //更新时间
	private Date centeracceptdate;//中心受理日期
	private int operationType;//操作类型
	private Storage storage;
	private StorageSoftware storageSoftware;
	private String nameLevel;//仓位号
	private Admin admin;
	private Admin trialAdmin;
	
	
	public Admin getTrialAdmin() {
		return trialAdmin;
	}

	public void setTrialAdmin(Admin trialAdmin) {
		this.trialAdmin = trialAdmin;
	}

	public Date getCenteracceptdate() {
		return centeracceptdate;
	}

	public void setCenteracceptdate(Date centeracceptdate) {
		this.centeracceptdate = centeracceptdate;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public StorageSoftware getStorageSoftware() {
		return storageSoftware;
	}

	public void setStorageSoftware(StorageSoftware storageSoftware) {
		this.storageSoftware = storageSoftware;
	}

	public String getNameLevel() {
		return nameLevel;
	}

	public void setNameLevel(String nameLevel) {
		this.nameLevel = nameLevel;
	}

	public void setId(String id){
		this.id=id;
	}
	
	public void setAdminid(String adminid){
		this.adminid=adminid;
	}
	
	public void setAcceptdate(Date acceptdate){
		this.acceptdate=acceptdate;
	}
	
	public void setSerialnum(String serialnum){
		this.serialnum=serialnum;
	}
	
	public void setApplyperson(String applyperson){
		this.applyperson=applyperson;
	}
	
	public void setSoftwarename(String softwarename){
		this.softwarename=softwarename;
	}
	
	public void setSalesman(String salesman){
		this.salesman=salesman;
	}
	
	public void setCertificatedate(Date certificatedate){
		this.certificatedate=certificatedate;
	}
	
	public void setSoftwarestatus(int softwarestatus){
		this.softwarestatus=softwarestatus;
	}
	
	public void setIsexist(int isexist){
		this.isexist=isexist;
	}
	
	public void setDescription(String description){
		this.description=description;
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
	 
	public String getAdminid(){
		return adminid;
	}
	 
	public Date getAcceptdate(){
		return acceptdate;
	}
	 
	public String getSerialnum(){
		return serialnum;
	}
	 
	public String getApplyperson(){
		return applyperson;
	}
	 
	public String getSoftwarename(){
		return softwarename;
	}
	 
	public String getSalesman(){
		return salesman;
	}
	 
	public Date getCertificatedate(){
		return certificatedate;
	}
	 
	public int getSoftwarestatus(){
		return softwarestatus;
	}
	 
	public int getIsexist(){
		return isexist;
	}
	 
	public String getDescription(){
		return description;
	}
	 
	public Date getCreatedate(){
		return createdate;
	}
	 
	public Date getUpdatedate(){
		return updatedate;
	}

	public String getTemphouseorder() {
		return temphouseorder;
	}

	public void setTemphouseorder(String temphouseorder) {
		this.temphouseorder = temphouseorder;
	}

	public String getMatchsign() {
		return matchsign;
	}

	public void setMatchsign(String matchsign) {
		this.matchsign = matchsign;
	}

	public String getTrialId() {
		return trialId;
	}

	public void setTrialId(String trialId) {
		this.trialId = trialId;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	
	
}