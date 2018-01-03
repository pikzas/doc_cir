package com.bjca.ecopyright.warehouse.dao.imp;

import java.util.List;

import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.soft.model.SoftwareVO;
import org.springframework.stereotype.Repository;

import com.bjca.ecopyright.warehouse.dao.StorageDao;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.framework.dao.IbatisDaoImp;
@Repository("storageDao")
public class StorageDaoImpl extends IbatisDaoImp<Storage, String> implements StorageDao
{
	/**
	 * 按条件查询
	 */
	@Override
	public List<Storage> queryStorageByParam(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".queryStorageByParam",map);
	}
	/*
	 * (non-Javadoc)
	 * @see com.bjca.ecopyright.warehouse.dao.StorageDao#selectByStorageId(java.lang.String)
	 * 根据storageid查询
	 */
	
	@Override
	public Storage selectByStorageId(String storageid) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".selectByStorageId", storageid);
	}

	@Override
	public List<Storage> selectAllStorage() {
		return this.getSqlSession().selectList(this.getSqlmapNamespace()+".selectAllStorage");
	}

	@Override
	public List<Storage> queryAllStorageByfid(String fid) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace()+".queryAllStorageByfid",fid);
	}

	@Override
	public List<Storage> searchWarehouse(Map map) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".searchWarehouseByParam",map);
	}
	
	@Override
	public Storage findStorageBysoftwareId(String id) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".findStorageBySoftwareId",id);
	}

	@Override
	public List<Storage> getAllFStorageByFFid(String ffid) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".getAllFStorageByFFid",ffid);
	}

	/**
	 * 查询出所有的二级仓位有可用空间的一级仓位
	 *
	 * @return
	 */
	@Override
	public List<Storage> getFStorage(String mark) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".getFStorage",mark);
	}

	@Override
	public Storage findStorageByFFstorageName(String name) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".findStorageByFFstorageName",name);
	}

	/**
	 * 通过二级仓位的ID 查询出其下所有的三级仓位 仓位号最大的那个库位
	 *
	 * @param fid
	 * @return
	 */
	@Override
	public Storage findMaxThirdStorage(String fid) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace()+".findMaxThirdStorage",fid);
	}

	@Override
	public List<SoftwareVO> getSecStorage(String pos) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace()+".getSecStorage",pos);
	}

	@Override
	public List<SoftwareVO> getOverDueSecStorage(String pos) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace()+".getOverDueSecStorage",pos);
	}

}
