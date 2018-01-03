package com.bjca.ecopyright.warehouse.dao;

import java.util.List;

import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.soft.model.SoftwareVO;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.framework.dao.BaseDao;

public interface StorageDao extends BaseDao<Storage, String>
{
	/**
	 * 查询所有的仓位信息，做节点树的操作
	 * @date 2016-5-24下午4:15:50
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @return
	 */
	public List<Storage> selectAllStorage();
	
	/**
	 * 查询所有二级节点的信息（一级节点不涉及到具体的业务）
	 * @date 2016-5-24下午4:16:34
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param fid
	 * @return
	 */
	public List<Storage> queryAllStorageByfid(String fid);
	
	/**
	 * 按条件查询
	 * @param param
	 * @return
	 */
	List<Storage> queryStorageByParam(Map<String, Object> param);


	public Storage selectByStorageId(String storageid);
	
	/**
	 * 根据条件查找仓库
	 * @date 2016-5-30下午1:23:05
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param map
	 * @return
	 */
	public List<Storage> searchWarehouse(Map map);
	
	
	/**
	 * 根据软件id查找库信息
	 * @date 2016-6-1上午11:11:40
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param id
	 * @return
	 */
	public Storage findStorageBysoftwareId(String id);
	
	
	/**
	 * 根据ffid(一级id查找下属的二级仓库)
	 * @date 2016-6-1下午1:16:07
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param ffid
	 * @return
	 */
	public List<Storage> getAllFStorageByFFid(String ffid);


	/**
	 * 查询出所有的二级仓位有可用空间的一级仓位
	 * @return
     */
	List<Storage> getFStorage(String mark);
	
	/**
	 * 按照一级仓库名查询一级仓库是否存在
	 * @date 2016-8-30下午2:49:21
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param name
	 * @return
	 */
	public Storage findStorageByFFstorageName(String name);

	/**
	 * 通过二级仓位的ID 查询出其下所有的三级仓位 仓位号最大的那个库位
	 * @param fid
	 * @return
     */
	public Storage findMaxThirdStorage(String fid);

	List<SoftwareVO> getSecStorage(String pos);
	
	/**
	 * 逾期二级仓库查询
	 * @date 2016-12-9上午11:01:37
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param pos
	 * @return
	 */
	List<SoftwareVO> getOverDueSecStorage(String pos);
}
