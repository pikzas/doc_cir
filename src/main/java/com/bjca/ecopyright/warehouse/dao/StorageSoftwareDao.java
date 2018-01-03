package com.bjca.ecopyright.warehouse.dao;

import java.util.List;
import java.util.Map;

import com.bjca.framework.dao.BaseDao;
import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.warehouse.model.StorageSoftware;

public interface StorageSoftwareDao extends BaseDao<StorageSoftware, String>
{

	StorageSoftware selectBySoftwareId(String softwareId);

	int isExist(String softwareId);

}
