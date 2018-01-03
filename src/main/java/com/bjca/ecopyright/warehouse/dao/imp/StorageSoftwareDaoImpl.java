package com.bjca.ecopyright.warehouse.dao.imp;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.warehouse.dao.StorageSoftwareDao;
import com.bjca.ecopyright.warehouse.model.StorageSoftware;
import com.bjca.framework.dao.IbatisDaoImp;
@Repository("storageSoftwareDao")
public class StorageSoftwareDaoImpl extends IbatisDaoImp<StorageSoftware, String> implements StorageSoftwareDao
{
	/*
	 * 根据softwareid查询
	 * (non-Javadoc)
	 * @see com.bjca.ecopyright.warehouse.dao.StorageSoftwareDao#selectBySoftwareId(java.lang.String)
	 */
	public StorageSoftware selectBySoftwareId(String softwareid) {
		return  this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".selectBySoftwareId", softwareid);
	}

	@Override
	public int isExist(String softwareId) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace()+".isExist",softwareId);
	}

}
