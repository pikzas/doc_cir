package com.bjca.ecopyright.system.service;


import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bjca.ecopyright.system.dao.AdminDao;
import com.bjca.ecopyright.system.dao.OperationLogDao;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.system.model.OperationLog;
import com.bjca.ecopyright.util.Function;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

@Service("operationLogService")
public class OperationLogServiceImpl implements OperationLogService {

	@Autowired
    @Qualifier("operationLogDao")
	private OperationLogDao operationLogDao;
	@Autowired
    @Qualifier("adminDao")
	private AdminDao adminDao;

	@Override
	public void batchProcess(String operationLogSql,String parameterJson) {
		try {
			if(operationLogSql.contains("sys_operationlog")){
				return;
			}else{
				//替换sql中的？和逗号,------否则插入sql语句的时候报错
				String handleSql = operationLogSql.replaceAll("[\\s+|\\t+|\\r+|\\n+]", " ");
				String[] batchArray = handleSql.split(" ",4);
				String category = batchArray[0];
				if("select".equalsIgnoreCase(category)){
					//System.out.println("我只是查询的sql！");
				}else{
					OperationLog operationLog = new OperationLog();
					if(parameterJson != null){
						operationLog.setExecParameter(parameterJson);
					}
					operationLog.setExecSql(handleSql);
					if("delete".equalsIgnoreCase(category)){
						operationLog.setType("delete");
						operationLog.setExecTable(batchArray[2]);
					}
					if("update".equalsIgnoreCase(category)){
						operationLog.setType("update");
						operationLog.setExecTable(batchArray[1]);
					}
					if("insert".equalsIgnoreCase(category)){
						operationLog.setType("insert");
						operationLog.setExecTable(batchArray[2]);
					}
					if(Admin.sessionAdmin() != null){
						operationLog.setAdminId(Admin.sessionAdmin().getID());
						operationLog.setOperationTime(new Date());
						this.saveOperationLog(operationLog);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean saveOperationLog(OperationLog operationLog) {
		boolean flag = false;
		try {
			if (Function.isEmpty(operationLog.getId())) {
				operationLog.setId(UUID.randomUUID().toString());
				operationLogDao.insert(operationLog);
				flag = true;
			} else {
				operationLogDao.update(operationLog);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	@Override
	public PageObject queryOperationLog(QueryParamater param) {

		PageObject po = null;
		try {
			po = this.operationLogDao.query(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po;
	}
	
	
	
	
}
