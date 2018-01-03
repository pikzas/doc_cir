package com.bjca.ecopyright.soft.model;

import com.bjca.ecopyright.system.model.Admin;
import com.bjca.framework.bean.BaseBean;
import java.util.Date;

public class SoftwareHistory extends BaseBean{

	private String id; //id
	private String adminid; //操作员id
	private String trialId;//分审员id
	private Date acceptdate; //接受日期
	private String serialnum; //流水号
	private String applyperson; //申请人
	private String softwarename; //软件全称
	private String salesman; //业务员
	private Date certificatedate; //出证日期
	private int softwarestatus; //软件状态
	private int isexist; //是否在库
	private String description; //备注
	private String temphouseorder;//临时仓库记录
	private String matchsign;//匹配状态
	private Date createdate; //创建时间
	private Date updatedate; //更新时间
	private Date centeracceptdate;//
	private Date auditdate;//分审日期
	private Date checkindate;//入库日期
	private Admin admin;
	private Admin trialAdmin;
	
	public Date getCenteracceptdate() {
		return centeracceptdate;
	}

	public void setCenteracceptdate(Date centeracceptdate) {
		this.centeracceptdate = centeracceptdate;
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

	public Date getAuditdate() {
		return auditdate;
	}

	public void setAuditdate(Date auditdate) {
		this.auditdate = auditdate;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Admin getTrialAdmin() {
		return trialAdmin;
	}

	public void setTrialAdmin(Admin trialAdmin) {
		this.trialAdmin = trialAdmin;
	}

	public Date getCheckindate() {
		return checkindate;
	}

	public void setCheckindate(Date checkindate) {
		this.checkindate = checkindate;
	}
	
	
}