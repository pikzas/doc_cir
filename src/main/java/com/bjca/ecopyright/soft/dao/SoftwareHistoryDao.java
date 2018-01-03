package com.bjca.ecopyright.soft.dao;

import java.util.Map;

import com.bjca.ecopyright.soft.model.SoftwareHistory;
import com.bjca.framework.dao.BaseDao;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

public interface SoftwareHistoryDao extends BaseDao<SoftwareHistory, String>
{
	
	public PageObject querySoftwareHistoryList(QueryParamater param);
	
	public int countSoftwareHistoryList(Map map);
}
