package com.bjca.ecopyright.soft.service;

import java.util.Date;
import java.util.UUID;

import com.bjca.ecopyright.system.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjca.ecopyright.soft.dao.SoftwareHistoryDao;
import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareHistory;
import com.bjca.ecopyright.statuscode.SoftWareStatusEnum;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

@Service("softwareHistoryService")
public class SoftwareHistoryServiceImpl implements SoftwareHistoryService {
	
	@Autowired
	private SoftwareHistoryDao softwareHistoryDao;
	/**
	 * 将software的记录移动到softwareHistory中
	 * @param software
	 * @return
	 */
	@Override
	public boolean insertBySoftware(Software software) {
		boolean flag = false;
		if(software == null){
			return flag;
		}
		SoftwareHistory entity = new SoftwareHistory();
		entity.setId(software.getId());
		entity.setAdminid(Admin.sessionAdmin().getID());
		entity.setAcceptdate(software.getAcceptdate());
		entity.setSerialnum(software.getSerialnum());
		entity.setCenteracceptdate(software.getCenteracceptdate());
		entity.setApplyperson(software.getApplyperson());
		entity.setSoftwarename(software.getSoftwarename());
		entity.setSalesman(software.getSalesman());
		entity.setCertificatedate(software.getCertificatedate());
		entity.setSoftwarestatus(SoftWareStatusEnum.CERTIFICATE_OK.getValue());
		entity.setIsexist(0);
		entity.setDescription(software.getDescription());
		entity.setTrialId(software.getTrialId());
		entity.setCreatedate(software.getCreatedate());
		entity.setUpdatedate(new Date());
		entity.setAuditdate(software.getAuditdate());
		try{
			this.softwareHistoryDao.insert(entity);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	
	@Override
	public PageObject querySoftwareHistoryList(QueryParamater param) {
        return softwareHistoryDao.querySoftwareHistoryList(param);
    }

}
