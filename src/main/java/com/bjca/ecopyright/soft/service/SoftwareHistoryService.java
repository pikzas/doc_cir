package com.bjca.ecopyright.soft.service;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;


public interface SoftwareHistoryService {
	/**
	 * 将software的记录移动到softwareHistory中
	 * @param software
	 * @return
	 */
	public boolean insertBySoftware(Software software);
	
	public PageObject querySoftwareHistoryList(QueryParamater param);
}
