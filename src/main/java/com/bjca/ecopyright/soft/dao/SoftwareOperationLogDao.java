package com.bjca.ecopyright.soft.dao;

import java.util.List;

import com.bjca.ecopyright.soft.model.SoftwareOperationLog;
import com.bjca.framework.dao.BaseDao;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

public interface SoftwareOperationLogDao extends BaseDao<SoftwareOperationLog, String>
{

	public List<SoftwareOperationLog> selectByserialnum(String serialnum);

	
	PageObject querySoftwareOperationLog(QueryParamater param);
	

}
