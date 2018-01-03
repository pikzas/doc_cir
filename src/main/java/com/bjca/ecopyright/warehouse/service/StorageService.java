package com.bjca.ecopyright.warehouse.service;

import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareVO;
import com.bjca.ecopyright.system.model.JsonResult;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

public interface StorageService
{

     /**
      * 添加/更新
      */
     public boolean saveStorage(Storage storage);


     /**
      * 根据ID查询
      */
     public Storage findStorage(String id);

     /**
      * 根据条件查询
      */
     public PageObject queryStorage(QueryParamater param);

     

     /**
      * 按条件查询
      * @param param
      * @return
      */
	public List<Storage> queryStorageByParam(Map<String, Object> param);
     /**
      * 添加二级父级仓位操作
      * @date 2016-5-20上午11:44:21
      * @mail humin@bjca.org.cn
      * @author humin
      * @return
      */
     public boolean addFStorage(String fid,String name,String totalspace,String description);
     
     /**
      * 添加一级仓库
      * @date 2016-5-21下午12:18:15
      * @mail humin@bjca.org.cn
      * @author humin
      * @param name
      * @return
      */
     public boolean addFStorage(String name,String mark);

     /**
      * 根据StorageId查询
      * @author wangna
      */
     public Storage findStorageByStorageId(String storageid);

     
     
     /**
      * 获取所有的仓库信息
      * @date 2016-5-23上午10:11:31
      * @mail humin@bjca.org.cn
      * @author humin
      * @return
      */
     public List<Storage> selectAllStorage();
     
     /**
      * 二级仓库废弃
      * @date 2016-5-23上午11:38:31
      * @mail humin@bjca.org.cn
      * @author humin
      * @param id
      * @return
      */
     public boolean abandonStorage(String id,String abandonreason);
     
     /**
      * 二级仓库的扩容
      * @date 2016-5-23下午4:50:15
      * @mail humin@bjca.org.cn
      * @author humin
      * @param id
      * @param addTotalspace
      * @return
      */
     public boolean expandStorage(String id,String addTotalspace);

     /**
      * 获取剩余空间
      * @param pos
      * @return
      */
	public Integer getSpareSpace(String pos);

	/**
	 * 锁定仓库
	 * @param pos
	 * @return
	 */
	public boolean lock(String pos);

	/**
	 * 解锁仓库
	 * @param pos
	 * @return
	 */
	public boolean unlock(String pos);
	/**
	 * 校验仓库状态
	 * @return
	 */
	public boolean checkLock();



	/**
	 * 入库操作
	 * @param softwares
	 * @param checkInType 入库类型
     * @return
     */
	JsonResult CheckInStorage(List<Software> softwares,int checkInType);


	/**
	 * 根据条件查找仓库
	 * @date 2016-5-30下午1:19:58
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param map
	 * @return
	 */
	public List<Storage> searchWarehouse(Map map);


	/**
	 * 解锁二级仓库
	 * @date 2016-5-31下午6:25:56
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param id
	 * @return
	 */
	public boolean unlockStorage(String id);
	
	/**
	 * 根据软件id查找库
	 * @date 2016-6-1上午11:08:37
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param id
	 * @return
	 */
	public Storage findStorageBysoftwareId(String id);
	
	
	/**
	 * 根据ffid(一级id查找下属的二级仓库)
	 * @date 2016-6-1下午1:13:05
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param ffid
	 * @return
	 */
	public List<Storage> getAllFStorageByFFid(String ffid);


	/**
	 * 查询出所有的有可用空间的一级仓位
	 * @return
     */
	public List<Storage> getFStorage(String mark);
	
	/**
	 * 按照一级仓库名查询一级仓库是否存在
	 * @date 2016-8-30下午2:46:39
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