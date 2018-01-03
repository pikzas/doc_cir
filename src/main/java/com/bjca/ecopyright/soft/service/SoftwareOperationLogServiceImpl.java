package com.bjca.ecopyright.soft.service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bjca.ecopyright.statuscode.SoftwareOperationEnum;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjca.ecopyright.soft.dao.SoftwareOperationLogDao;
import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareOperationLog;
import com.bjca.ecopyright.statuscode.SoftWareStatusEnum;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.system.model.JsonResult;
import com.bjca.ecopyright.system.service.SystemService;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonDateValueProcessor;
import com.bjca.ecopyright.util.ReadWriteExcelUtil;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.ecopyright.warehouse.model.StorageSoftware;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;


@Service("softwareOperationLogService")
public class SoftwareOperationLogServiceImpl implements SoftwareOperationLogService
{
	Log log = LogFactory.getLog(SoftwareOperationLogServiceImpl.class);

	 @Autowired
	 private SoftwareOperationLogDao softwareOperationLogDao;



	/**
	 * 保存日志
	 *
	 * @param software 软件
	 * @param type     日志类型
	 * @return
	 */
	@Override
	public boolean saveLog(Software software, int type,Admin admin) {
		boolean flag = false;
		try {
			//添加软件操作日志操作-----------分审出库
			SoftwareOperationLog softwareOperationLog = new SoftwareOperationLog();
			softwareOperationLog.setId(UUID.randomUUID().toString());
			softwareOperationLog.setAdminid(admin.getID());
			softwareOperationLog.setSerialnum(software.getSerialnum());
			softwareOperationLog.setSalesman(software.getSalesman());
			softwareOperationLog.setApplyperson(software.getApplyperson());
			softwareOperationLog.setSoftwarestatus(software.getSoftwarestatus());
			softwareOperationLog.setCenteracceptdate(software.getCenteracceptdate());
			softwareOperationLog.setCertificatedate(software.getCertificatedate());
			softwareOperationLog.setDescription(software.getDescription());
			softwareOperationLog.setIsexist(software.getIsexist());
			softwareOperationLog.setMatchsign(software.getMatchsign());
			softwareOperationLog.setNameLevel(software.getNameLevel());
			softwareOperationLog.setSoftwarename(software.getSoftwarename());
			softwareOperationLog.setTemphouseorder(software.getTemphouseorder());
			softwareOperationLog.setTrialId(software.getTrialId());
			softwareOperationLog.setOperationType(type);
			softwareOperationLog.setCreatedate(new Date());
			softwareOperationLogDao.insert(softwareOperationLog);
		}catch (Exception e){
			e.printStackTrace();
		}
		return flag;
	}
     
     /**
      * 添加/更新
      */
     @Override
     public boolean saveSoftwareOperationLog(SoftwareOperationLog softwareOperationLog){
     
        boolean flag = false;
		try {
			if (Function.isEmpty(softwareOperationLog.getId())) {
				softwareOperationLog.setId(UUID.randomUUID().toString());
				softwareOperationLogDao.insert(softwareOperationLog);
				flag = true;
			} else {
				softwareOperationLogDao.update(softwareOperationLog);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
     }

     /**
      * 根据条件查询
      */
     @Override
     public PageObject querySoftwareOperationLog(QueryParamater param){
		
		PageObject po = null;
		try {
			po = this.softwareOperationLogDao.query(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po; 
		    	
     }
     
     /**
      * 删除
      */
     @Override
     public boolean removeSoftwareOperationLog(String id){
     	boolean flag = false;
		try {
			this.softwareOperationLogDao.delete(id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
     }




	@Override
	public SoftwareOperationLog findSoftwareOperationLog(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	


}