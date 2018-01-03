package com.bjca.ecopyright.warehouse.service;

import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.warehouse.model.StorageSoftware;

public interface StorageSoftwareService {
	
	/**
	 * 依据软件的id判定该软件是否存在数据库中
	 * @param softwareId
	 * @return 如果数据库存在 返回true 不存在返回false
	 */
	boolean isExist(String softwareId);
	
	/**
	 * 依据软件的id取出对象
	 * @param softwareId
	 * @return
	 */
	StorageSoftware queryBySoftwareId(String softwareId);
}
