package com.bjca.ecopyright.warehouse.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjca.ecopyright.soft.dao.SoftwareDao;
import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareVO;
import com.bjca.ecopyright.soft.service.SoftwareOperationLogService;
import com.bjca.ecopyright.statuscode.AdminRoleStatusEnum;
import com.bjca.ecopyright.statuscode.SoftWareStatusEnum;
import com.bjca.ecopyright.statuscode.SoftwareOperationEnum;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.system.model.JsonResult;
import com.bjca.ecopyright.system.service.SystemService;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonUtils;
import com.bjca.ecopyright.warehouse.dao.StorageDao;
import com.bjca.ecopyright.warehouse.dao.StorageSoftwareDao;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.ecopyright.warehouse.model.StorageSoftware;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;


@Service("storageService")
public class StorageServiceImpl implements StorageService
{
	Log log = LogFactory.getLog(StorageServiceImpl.class);
	
	 @Autowired
	 private SystemService systemService;

	 @Autowired
 	 private StorageDao storageDao;
	 
	 @Autowired
	 private StorageSoftwareDao storageSoftwareDao;
	 
	 @Autowired
	 private SoftwareDao softwareDao;
	 
	 @Autowired
	 private SoftwareOperationLogService logService;

     private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
     /**
      * 添加/更新
      */
     @Override
     public boolean saveStorage(Storage storage){
     
        boolean flag = false;
		try {
			if (Function.isEmpty(storage.getId())) {
				storage.setId(UUID.randomUUID().toString());
				storageDao.insert(storage);
				flag = true;
			} else {
				storageDao.update(storage);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
     }

     /**
      * 根据ID查询
      */
     @Override
     public Storage findStorage(String id){
    	 Storage storage = null;
		try {
			storage = this.storageDao.select(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return storage;

     }

     /**
      * 根据条件查询
      */
     @Override
     public PageObject queryStorage(QueryParamater param){
		
		PageObject po = null;
		try {
			po = this.storageDao.query(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po; 
		    	
     }

     

     /**
      * 按条件查询
      */
	@Override
	public List<Storage> queryStorageByParam(Map<String, Object> param) {
		return this.storageDao.queryStorageByParam(param);
	}

	@Override
	public boolean addFStorage(String ffid,String name, String totalspace, String description) {
		boolean flag = false;
		try {
			Storage FFstorage = this.findStorage(ffid);
			Integer total = Integer.parseInt(totalspace);
			Storage Fstorage = new Storage();
			Fstorage.setFid(ffid);
			//设置组名称
			Fstorage.setGroupname(FFstorage.getName());
			//设置为二级仓库标识
			Fstorage.setLevel(2);
			Fstorage.setName(name);
			//设置库序号
			Fstorage.setHouseorder(0);
			//设置容限
			Fstorage.setTotalspace(total);
			//设置使用量
			Fstorage.setUsespace(0);
			//设置剩余量
			Fstorage.setSurplus(total);
			//设置废弃标识
			Fstorage.setIsabandon(0);
			//设置废弃原因
			Fstorage.setAbandonreason(null);
			//设置仓位备注
			Fstorage.setDescription(description);
			//设置创建时间
			Fstorage.setCreatedate(new Date());
			//设置更新时间
			Fstorage.setUpdatedate(new Date());
			//设置锁状态
			Fstorage.setLocksign(0);
			boolean addOk = this.saveStorage(Fstorage);
			if(addOk){
				String Fid = Fstorage.getId();
				for(int i = 0 ; i<total; i++){
					Storage storage = new Storage();
					//设置父级id
					storage.setFid(Fid);
					//设置为三级仓库标识
					storage.setLevel(3);
					//设置组名称
					storage.setGroupname(Fstorage.getGroupname());
					//设置设置子仓库位的编号
					storage.setName(Fstorage.getName());
					//设置库序号
					storage.setHouseorder(i+1);
					//设置废弃标识
					storage.setIsabandon(0);
					//设置占用标识
					storage.setIsuse(0);
					//设置废弃原因
					storage.setAbandonreason(null);
					//设置创建时间
					storage.setCreatedate(new Date());
					//设置更新时间
					storage.setUpdatedate(new Date());
					//设置锁状态
					storage.setLocksign(0);
					this.saveStorage(storage);
				}
				flag = true;
			}else{
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		
		return flag;
	}

	@Override
	public boolean addFStorage(String name,String mark) {
		boolean flag = false;
		try {
			Storage FFstorage = new Storage();
			FFstorage.setFid(null);
			//设置为一级仓库标识
			FFstorage.setLevel(1);
			FFstorage.setName(name);
			//设置库序号
			FFstorage.setHouseorder(0);
			//设置容限
			FFstorage.setTotalspace(0);
			//设置使用量
			FFstorage.setUsespace(0);
			//设置废弃标识
			FFstorage.setIsabandon(0);
			//设置废弃原因
			FFstorage.setAbandonreason(null);
			//设置仓位备注
			FFstorage.setDescription(null);
			//设置创建时间
			FFstorage.setCreatedate(new Date());
			//设置更新时间
			FFstorage.setUpdatedate(new Date());
			//设置锁状态
			FFstorage.setLocksign(0);
			//设置仓库的类型
			FFstorage.setMark(mark);
			boolean addOk = this.saveStorage(FFstorage);
			if(addOk){
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 查询出剩余的空间
	 */
	@Override
	public Integer getSpareSpace(String pos) {
		Storage storage = this.storageDao.select(pos);
		int max = storage.getTotalspace();
		int use = storage.getUsespace();
		return max-use;
	}

	@Override
	public List<Storage> selectAllStorage() {
		List<Storage> slist = null;
		slist = this.storageDao.selectAllStorage();
		return slist;
	}

	@Override
	public boolean abandonStorage(String id,String abandonreason) {
		boolean flag = false;
		try {
			Storage storage = this.storageDao.select(id);
			if(storage != null){
				storage.setIsabandon(1);
				storage.setAbandonreason(abandonreason);
				this.storageDao.update(storage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean expandStorage(String id, String addTotalspace) {
		boolean flag = false;
		try {
			Storage storage = this.storageDao.select(id);
			Integer addNumber = Integer.valueOf(addTotalspace);
//			List<Storage> slist = this.storageDao.queryAllStorageByfid(id);
//			//将以前仓库的位置按照扩容量后移扩容量
//			if(slist != null && slist.size()>0){
//				for(Storage s: slist){
//					s.setHouseorder(s.getHouseorder()+addNumber);
//					this.storageDao.update(s);
//				}
//			}
			//移动完毕扩容量后再对应的二级仓库下添加(三级仓位)扩容量仓位
			
			/*********************不做仓位移动的操作**************************/
			for(int i = 0 ;i<addNumber ;i++){
				Storage addStorage = new Storage();
				//设置父级id
				addStorage.setFid(id);
				//设置为三级仓库标识
				addStorage.setLevel(3);
				//设置组名称
				addStorage.setGroupname(storage.getGroupname());
				//设置设置子仓库位的编号
				addStorage.setName(storage.getName());
				//设置库序号
				addStorage.setHouseorder(storage.getTotalspace()+i+1);
				//设置废弃标识
				addStorage.setIsabandon(0);
				//设置占用标识
				addStorage.setIsuse(0);
				//设置废弃原因
				addStorage.setAbandonreason(null);
				//设置创建时间
				addStorage.setCreatedate(new Date());
				//设置更新时间
				addStorage.setUpdatedate(new Date());
				//设置锁状态
				addStorage.setLocksign(0);
				this.saveStorage(addStorage);
			}
			//设置二级仓库的总量
			storage.setTotalspace(storage.getTotalspace()+addNumber);
			//设置二级仓库剩余量
			storage.setSurplus(storage.getSurplus()+addNumber);
			this.saveStorage(storage);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return flag;
	}
	@Override
	public Storage findStorageByStorageId(String storageid) {
		return this.storageDao.selectByStorageId(storageid);
	}
	

	/**
	 * 锁定仓库
	 */
	@Override
	@Transactional
	public boolean lock(String pos) {
		//锁定该仓库 同时更新更改时间
		Storage store = this.storageDao.select(pos);
		store.setLocksign(1);
		store.setUpdatedate(new Date());
		int ret = this.storageDao.update(store);
		boolean flag = ret>0?true:false;
		return flag;
	}

	/**
	 * 解锁仓库
	 * @param pos
	 * @return
	 */
	@Override
	@Transactional
	public boolean unlock(String pos) {
		//锁定该仓库 同时更新更改时间
		Storage store = this.storageDao.select(pos);
		store.setLocksign(0);
		store.setUpdatedate(new Date());
		int ret = this.storageDao.update(store);
		boolean flag = ret>0?true:false;
		return flag;
	}
	/**
	 * 校验解锁
	 */
	@Override
	public boolean checkLock() {
		//取出所有的二级仓库 锁定的仓库 遍历 对时间进行校验
		Map<String,Object> param = new HashMap<String,Object>();
		long exp = 20*60*1000;//锁定有效时间20分钟
		long now = System.currentTimeMillis();
		param.put("level", 2);
		param.put("isabandon", 0);
		List<Storage> lists = this.queryStorageByParam(param);
		for (Storage storage : lists) {
			long storeTime = storage.getUpdatedate().getTime();
			if(now-storeTime>=exp){
				//超时可以解锁了
				this.unlock(storage.getId());
			}
		}
		return false;
	}

	/**
	 * 对于批量插入的数据 全部成功了则返回入库成功 如果有部分数据异常 则应该将该部分数据展示在页面
	 * @param softwares
	 * @param isException 是否是一场入库 加入此处参数的目的是为了在异常入库的时候方便记录日志
	 * @return
	 */
	@Override
	public JsonResult CheckInStorage(List<Software> softwares,int checkInType) {
		JsonResult result = new JsonResult();
		List<Software> badList = new ArrayList<Software>();
		boolean flag = false;
		int count = 0;
		for (Software soft:softwares) {
			flag = this.checkInStorage(soft,count,checkInType);
			if(!flag){ //该条数据入库异常了
				badList.add(soft);
				count++;
			}
		}
		if(badList==null||badList.size()==0){
			result.setState(true);
			result.setMsg("入库成功！");
		}else{
			result.setState(false);
			result.setMsg("部分入库成功，失败"+badList.size()+"个！");
			result.setData(JsonUtils.objectToJsonString(badList));
		}
		return result;
	}


	/**
	 *
	 * @param software
	 * @param count 该批次入库的异常数量 做下沉操作
	 * @param checkInType 表示该次入库操作类型
     * @return
     */
	@Transactional
	public boolean checkInStorage(Software software,int count,int checkInType) {
		Admin admin = Admin.sessionAdmin();
		boolean flag = false;
		try {
			String houseOrder = software.getTemphouseorder();
			int len = houseOrder.length();
			int pos = houseOrder.indexOf("-");
			/***************此处获取组名称和仓库名称应该按照正则表达式匹配原则，去除以前的硬编码方式*******************/
			//获取仓库groupname
//			String groupName = houseOrder.substring(0, 1);
			String groupName = "";
			Pattern p=Pattern.compile("^[a-zA-Z]{1,}"); 
	        Matcher m=p.matcher(houseOrder); 
	        while(m.find())
	        { 
	        	groupName = m.group(); 
	        } 
			//获取仓库的名字
//			String name = houseOrder.substring(1,pos);
	        String name = "";
	        Pattern p2=Pattern.compile("(\\d+)");   
	        Matcher m2=p2.matcher(houseOrder);       
	        if(m2.find()){
	        	name = m2.group(1);    
	        } 
			//获取仓库的次序
			String originOrder = houseOrder.substring(pos+1,len);
			String order = (Integer.parseInt(originOrder)-count)+""; //如果有异常 此处应该有下沉
			//更新仓库状态 三级更新状态 二级更新
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("groupName", groupName);
			map.put("storeName",name);
			map.put("houseorder", order);
			List<Storage> list = this.queryStorageByParam(map);
			Storage firstStore = list.get(0);
			//2016-09-29 陈平更新 由于在中间关系表中加了唯一索引 此处应该先对关系表操作 一旦出现异常 ，不会向storage 和 software更新 避免了脏数据的产生
			//更新关系表
			StorageSoftware ss = new StorageSoftware();
			ss.setId(UUID.randomUUID().toString());
			ss.setSoftwareid(software.getId());
			ss.setStorageid(firstStore.getId());
			ss.setCreatedate(new Date());
			this.storageSoftwareDao.insert(ss);
			//更新三级仓库
			firstStore.setIsuse(1);
			firstStore.setUpdatedate(new Date());
			this.storageDao.update(firstStore);
			//更新二级仓库
			Storage secondStore = this.storageDao.select(firstStore.getFid());
			secondStore.setUsespace(secondStore.getUsespace()+1);
			secondStore.setSurplus(secondStore.getSurplus()-1);
			secondStore.setUpdatedate(new Date());
			this.storageDao.update(secondStore);

			//更新软件状态 并插入操作历史
			int originalState = software.getSoftwarestatus();
			software.setIsexist(1);
			software.setUpdatedate(new Date());
			software.setTemphouseorder(null);
			//设置   放入仓库时间
			software.setCenteracceptdate(new Date());
			software.setAdminid(Admin.sessionAdmin().getID());
			/**************对于异常入库操作的软件 不做状态的变化只记录 操作日志***************/
			//异常回库
			if(SoftwareOperationEnum.EXCEPTION_IN_STORAGE.getValue() == checkInType){
				//异常入库的只是记录操作日志不做状态的变化
				logService.saveLog(software,SoftwareOperationEnum.EXCEPTION_IN_STORAGE.getValue(),admin);
			/**************对于不是异常入库操作的软件 记录对应的日志并且需要更新状态***************/
			//制证回库	
			}else if(SoftwareOperationEnum.CARD_IN_STORAGE.getValue() == checkInType){
				// 制证回库的soft 将状态由3（已分审）或者4（待补正）改为5（待制证）
				if(SoftWareStatusEnum.TRIAL_OK.getValue() == originalState){
					software.setSoftwarestatus(SoftWareStatusEnum.PENDING_CERTIFICATE.getValue());
					logService.saveLog(software,SoftwareOperationEnum.CARD_IN_STORAGE.getValue(),admin);
				}else if(SoftWareStatusEnum.PENDING_CORRECT.getValue() == originalState){
					software.setSoftwarestatus(SoftWareStatusEnum.PENDING_CERTIFICATE.getValue());
					logService.saveLog(software,SoftwareOperationEnum.CARD_IN_STORAGE.getValue(),admin);
				}
			//新入库
			}else if(SoftwareOperationEnum.NEW_IN_STORAGE.getValue() == checkInType && SoftWareStatusEnum.TO_BE_IN_STORAGE.getValue() == originalState){
				//新入库的soft 将状态由0改为1 
				software.setSoftwarestatus(SoftWareStatusEnum.PENDING_PAYMENT.getValue());
				logService.saveLog(software,SoftwareOperationEnum.NEW_IN_STORAGE.getValue(),admin);
			//逾期回库
			}else if(SoftwareOperationEnum.OVERDUE_IN_STORAGE.getValue() == checkInType){
				// 制证回库的soft 将状态由3（已分审）或者4（待补正）改为5（待制证）
				if(SoftWareStatusEnum.TRIAL_OK.getValue() == originalState){
					software.setSoftwarestatus(SoftWareStatusEnum.PENDING_CERTIFICATE.getValue());
					logService.saveLog(software,SoftwareOperationEnum.OVERDUE_IN_STORAGE.getValue(),admin);
				}else if(SoftWareStatusEnum.PENDING_CORRECT.getValue() == originalState){
					software.setSoftwarestatus(SoftWareStatusEnum.PENDING_CERTIFICATE.getValue());
					logService.saveLog(software,SoftwareOperationEnum.OVERDUE_IN_STORAGE.getValue(),admin);
				}
			//快捷入库：雍和
			}else if(SoftwareOperationEnum.QUICK_IN_STORAGE.getValue() == checkInType){
				if(SoftWareStatusEnum.TO_BE_IN_STORAGE.getValue() == originalState){
					//状态直接从“待入库”变为“待制证”，
					/*************快捷入库 将分审人员直接分配为雍和 并且添加分审日期*****************/
					Admin TrialAdmin = this.systemService.searchAdminByType(AdminRoleStatusEnum.YONGHE.getValue().toString());
					software.setTrialId(TrialAdmin.getID());
					software.setAuditdate(new Date());
					software.setSoftwarestatus(SoftWareStatusEnum.PENDING_CERTIFICATE.getValue());
					logService.saveLog(software,SoftwareOperationEnum.QUICK_IN_STORAGE.getValue(),admin);
				}
			}
			this.softwareDao.update(software);
			flag = true ;
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return flag;
	}



	@Override
	public List<Storage> searchWarehouse(Map map) {
		List<Storage> list = null;
		list = this.storageDao.searchWarehouse(map);
		return list;
	}

	@Override
	public boolean unlockStorage(String id) {
		boolean flag = false;
		Storage storage = this.storageDao.select(id);
		if(storage!=null){
			storage.setLocksign(0);
			this.storageDao.update(storage);
			flag = true;
		}
		return flag;
	}
	
	@Override
	public Storage findStorageBysoftwareId(String id) {
		Storage storage = null;
		storage = this.storageDao.findStorageBysoftwareId(id);
		return storage;
	}

	@Override
	public List<Storage> getAllFStorageByFFid(String ffid) {
		List<Storage> slist = null;
		slist = this.storageDao.getAllFStorageByFFid(ffid);
		return slist;
	}

	/**
	 * 查询出所有的有可用空间的一级仓位
	 *
	 * @return
	 */
	@Override
	public List<Storage> getFStorage(String mark) {
		List<Storage> storages= new ArrayList<Storage>();
		try {
			storages = this.storageDao.getFStorage(mark);
		}catch (Exception e){
			e.printStackTrace();
		}
		return storages;
	}

	@Override
	public Storage findStorageByFFstorageName(String name) {
		Storage storage = null;
		storage = this.storageDao.findStorageByFFstorageName(name);
		return storage;
	}

	/**
	 * 通过二级仓位的ID 查询出其下所有的三级仓位 仓位号最大的那个库位
	 *
	 * @param fid
	 * @return
	 */
	@Override
	public Storage findMaxThirdStorage(String fid) {
		Storage storage = new Storage();
		try {
			storage = storageDao.findMaxThirdStorage(fid);
		}catch (Exception e){
			e.printStackTrace();
		}
		return storage;
	}

	@Override
	public List<SoftwareVO> getSecStorage(String pos) {
		List<SoftwareVO> softwareVOs = new ArrayList<>();
		try {
			softwareVOs = storageDao.getSecStorage(pos);
		}catch (Exception e){
			e.printStackTrace();
		}
		return softwareVOs;
	}

	@Override
	public List<SoftwareVO> getOverDueSecStorage(String pos) {
		List<SoftwareVO> softwareVOs = new ArrayList<>();
		try {
			softwareVOs = storageDao.getOverDueSecStorage(pos);
		}catch (Exception e){
			e.printStackTrace();
		}
		return softwareVOs;
	}


}