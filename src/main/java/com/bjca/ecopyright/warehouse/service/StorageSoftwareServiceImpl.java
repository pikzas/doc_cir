package com.bjca.ecopyright.warehouse.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.warehouse.dao.StorageSoftwareDao;
import com.bjca.ecopyright.warehouse.model.StorageSoftware;

@Service("storageSoftwareService")
public class StorageSoftwareServiceImpl implements StorageSoftwareService {
	
	@Autowired
	private StorageSoftwareDao storageSoftwareDao;
	
	/**
	 * 依据软件id判定该项目是否已经入库
	 */
	@Override
	public boolean isExist(String softwareId) {
		int ret = storageSoftwareDao.isExist(softwareId);
		return ret>0;
	}

	@Override
	public StorageSoftware queryBySoftwareId(String softwareId) {
		return  this.storageSoftwareDao.selectBySoftwareId(softwareId);
	}

}
