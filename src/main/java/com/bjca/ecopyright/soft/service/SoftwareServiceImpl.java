package com.bjca.ecopyright.soft.service;

import com.bjca.ecopyright.soft.dao.SoftwareDao;
import com.bjca.ecopyright.soft.dao.SoftwareOperationLogDao;
import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareAuditVO;
import com.bjca.ecopyright.soft.model.SoftwareVO;
import com.bjca.ecopyright.statuscode.SoftWareStatusEnum;
import com.bjca.ecopyright.statuscode.SoftwareOperationEnum;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.system.model.JsonResult;
import com.bjca.ecopyright.system.service.SystemService;
import com.bjca.ecopyright.tools.service.ChinaCalendarService;
import com.bjca.ecopyright.util.*;
import com.bjca.ecopyright.warehouse.dao.StorageDao;
import com.bjca.ecopyright.warehouse.dao.StorageSoftwareDao;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.ecopyright.warehouse.model.StorageSoftware;
import com.bjca.ecopyright.warehouse.service.StorageService;
import com.bjca.ecopyright.warehouse.service.StorageSoftwareService;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import net.sf.json.JsonConfig;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("softwareService")
public class SoftwareServiceImpl implements SoftwareService {
    Log log = LogFactory.getLog(SoftwareServiceImpl.class);

	 @Autowired
 	 private SoftwareDao softwareDao;
	 @Autowired
	 private StorageSoftwareDao storageSoftwareDao;
	 @Autowired
	 private StorageDao storageDao;
	 @Autowired
	 private SoftwareOperationLogDao softwareOperationLogDao;

	 @Autowired
	 private SoftwareHistoryService softwareHistoryService;
	 @Autowired
	 private StorageService storageService;
	 @Autowired
	 private StorageSoftwareService storageSoftwareService;
	 @Autowired
	 private SystemService systemService;
     @Autowired
     private SoftwareOperationLogService logService;
     @Autowired
     private ChinaCalendarService chinaCalendarService;


     @Autowired
     private MemcachedClient mcc;


    /**
     * 添加/更新
     */
    @Override
    public boolean saveSoftware(Software software) {
        boolean flag = false;
        try {
            if (Function.isEmpty(software.getId())) {
                software.setId(UUID.randomUUID().toString());
                softwareDao.insert(software);
                flag = true;
            } else {
                softwareDao.update(software);
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
    public Software findSoftware(String id) {
        Software software = null;
        try {
            software = this.softwareDao.select(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return software;
    }

    /**
     * 根据条件查询
     */
    @Override
    public PageObject querySoftware(QueryParamater param) {

        PageObject po = null;
        try {
            po = this.softwareDao.query(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return po;

    }


    /**
     * 删除
     */
    @Override
    public boolean removeSoftware(String id) {
        boolean flag = false;
        try {
            this.softwareDao.delete(id);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }


    /**
     * Excel导入软件数据
     *
     * @param inputStream 文件路径
     * @return
     */
    @Override
    public List<Software> importExcel(InputStream inputStream) {
        List<Software> softlist = new ArrayList<Software>();
        softlist = ReadWriteExcelUtil.readExcel(inputStream);
        log.debug("Excel导入softlist长度：" + softlist.size());
        return softlist;
    }

    /**
     * @param inputStream
     * @return
     */
    public List<Software> importZZCKExcel(InputStream inputStream) {
        List<Software> softlist = new ArrayList<Software>();
        softlist = ReadWriteExcelUtil.readZZCKExcel(inputStream);
        log.debug("Excel导入softlist长度：" + softlist.size());
        return softlist;
    }


    @Override
    public Software querySoftwareBySerialnum(String serialNum) {
        return this.softwareDao.selectByserialnum(serialNum);
    }

    /**
     * 根据制证日期查询
     * certificatedate
     *
     * @author wangna
     */
    @Override
    public int findSoftwareByCertificatedate(String certificatedate) {
        int software = 0;
        try {
            software = this.softwareDao.countByCertificatedate(certificatedate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return software;

    }

    @Override
    public PageObject querySoftInfoList(QueryParamater param) {
        return softwareDao.querySoftInfoList(param);
    }

    /**
     * 导入入库excel 返回所有的已存在的soft记录
     */
    @Override
    public List<Software> importExcelForRuku(InputStream inputStream) {
        List<Software> failSoftList = new ArrayList<Software>();
        List<Software> importSofts = this.importExcel(inputStream);
        for (Software software : importSofts) {
            software.setId(UUID.randomUUID().toString());
            software.setCreatedate(new Date());
            software.setUpdatedate(new Date());
            /**根据流水号判断数据库中是否存在该软件**/
            String serialnum = software.getSerialnum();
            //避免由于导入的excel文件样式问题导致的software 有对象 但是数据都是空的情况
            if(Function.isEmpty(serialnum)){
                continue;
            }
            Software selectByserialnum = this.softwareDao.selectByserialnum(serialnum);
            if (selectByserialnum != null) {
                log.debug("该数据已存在,软件名：" + software.getSoftwarename() + "      流水号：" + software.getSerialnum());
                failSoftList.add(selectByserialnum);
            } else {
                this.softwareDao.insert(software);
            }
        }
        return failSoftList;
    }

    /**
     * 制证出库 导入excel 返回所有的异常信息
     * 当前逻辑只匹配流水号
     */
    @Override
    public List<Software> importExcelForZZCK(InputStream inputStream) {
        List<Software> badList = new ArrayList<Software>();
        List<Software> goodList = new ArrayList<Software>();
        List<Software> importSofts = this.importZZCKExcel(inputStream);
        //获取所有审核员的名字
        for (Software software : importSofts) {
            /**根据流水号判断数据库中是否存在该软件**/
            String serialnum = software.getSerialnum();
            //避免由于导入的excel文件样式问题导致的software 有对象 但是数据都是空的情况
            if(Function.isEmpty(serialnum)){
                continue;
            }
            Software soft = this.softwareDao.selectByserialnum(serialnum);
            if (soft != null) {
                //说有该流水号存在
                Storage store = soft.getStorage();
                if (store == null) {
                    //不再库中
                    soft.setTemphouseorder("该记录不在仓库中！");
                    badList.add(soft);
                } else {
                    //对状态进行判定
                    int state = soft.getSoftwarestatus();
                    if (state == SoftWareStatusEnum.PENDING_CERTIFICATE.getValue()) {
                        //状态正常 出库动作 清除加入history表中 清除二级库存 三级库存 考虑到 一次是搬空一个仓位 直接 就 对二级库存 清空 三级库存清空
                        goodList.add(soft);
                        this.checkOutForCertificatedSoft(store, soft);
                    } else {
                        //在库里 但是状态不对
                        soft.setTemphouseorder("该记录不属于待制证状态！");
                        badList.add(soft);
                    }
                }
            } else {
                Software badSoft = new Software();
                badSoft.setSerialnum(serialnum);
                badSoft.setTemphouseorder("数据库中不存在该流水号记录！");
                badList.add(badSoft);
            }
		}
		goodList.addAll(badList);
		return goodList;
	}

	/**
	 * 制证出库
	 * 1，清除software 表中记录
	 * 2，清除softwareStorage表中记录
	 * 3，清除storage 二级 仓库的使用量
	 * 4，跟新storage 三级 仓库的状态
	 * @param storage
	 * @param software
	 * @return
	 */
	@Transactional
	public void checkOutForCertificatedSoft(Storage storage,Software software){
		Admin admin = Admin.sessionAdmin();
		try{
			//1.删除软件和库关系
			StorageSoftware storeSoft = this.storageSoftwareDao.selectBySoftwareId(software.getId());
			this.storageSoftwareDao.delete(storeSoft.getId());
			//2.对库中的仓库进行释放
			Storage pstore = this.storageDao.select(storage.getFid());
			if(pstore.getUsespace() == 0 && pstore.getSurplus() == pstore.getTotalspace()){

			}else{
				//二级仓库使用量减1
				pstore.setUsespace(pstore.getUsespace()-1);
				//二级仓库剩余量加1
				pstore.setSurplus(pstore.getSurplus()+1);
			}
			pstore.setUpdatedate(new Date());
			this.storageDao.update(pstore);
			storage.setIsuse(0);
			storage.setUpdatedate(new Date());
			this.storageDao.update(storage);

			//3.删除软件信息以及备份
			/**      hm个人建议-----  建议先删除关联关系后再对软件信息进行操作                       ***/
			//删除库中软件记录
			this.softwareDao.delete(software.getId());
			//并且往历史软件记录中插入该条记录，以被以后查询
			this.softwareHistoryService.insertBySoftware(software);
			//日志中将软件状态修改为已制证
			software.setSoftwarestatus(SoftWareStatusEnum.CERTIFICATE_OK.getValue());
			//4.对软件进行历史记录插入
			//添加软件操作日志操作-----------制证出库
            logService.saveLog(software,SoftwareOperationEnum.CARD_OUT_STORAGE.getValue(),admin);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


    @Override
    public List<Software> queryList(Map<String, String> map) {
        return softwareDao.queryList(map);
    }

    /**
     * 查询制证列表
     *
     * @param beginDate
     * @param endDate
     */
    @Override
    public List<SoftwareVO> queryMakeCardList(String beginDate, String endDate) {
        List<SoftwareVO> retList = new ArrayList<SoftwareVO>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("softwarestatus", 5);//待出库
        try {
            retList = this.queryStatisticList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retList;
    }

    /**
     * 查询预制证列表
     *
     * @param beginDate
     * @param endDate
     */
    @Override
    public List<SoftwareVO> queryExpectMakeCardList(String beginDate, String endDate) {
        List<SoftwareVO> retList = new ArrayList<SoftwareVO>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        try {
            retList = this.queryStatisticList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retList;
    }

    /**
     * 查询分审列表
     *
     * @param beginDate
     * @param endDate
     */
    @Override
    public List<SoftwareVO> queryAuditList(String beginDate, String endDate) {
    	List<SoftwareVO> softwareVOList = new ArrayList<SoftwareVO>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("softwarestatus", 2);//待分审
        try {
        	softwareVOList = this.queryStatisticList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return softwareVOList;
    	
       /* List<SoftwareVO> softwareVOList = new ArrayList<SoftwareVO>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //将每个日期向前推五个工作日 查询出数据
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(beginDate);
            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(d1);//设置日期起始时间
            while(dd.getTime().before(d2)) {//判断是否到结束日期
                String date = sdf.format(dd.getTime());
                boolean isWorkDay = chinaCalendarService.isWorkDay(date);
                if(isWorkDay){
                    SoftwareVO softVO = new SoftwareVO();
                    Date retDate = chinaCalendarService.queryRankWorkday(date,"5");
                    String retDateString = sdf.format(retDate.getTime());
                    softVO.setCategory(retDateString);
                    //依据日期和状态查出software
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("softwarestatus",2);//待分审
                    map.put("certificatedate",date);
                    List<SoftwareVO> retLists = queryStatisticList(map);
                    if(retLists!=null&&retLists.size()>0){
                        softVO.setTotal(retLists.get(0).getTotal());
                        softwareVOList.add(softVO);
                    }
                }
                dd.add(Calendar.DAY_OF_YEAR, 1);//进行当前日期月份加1
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    /**
     * 分审核销统计
     *
     * @param map
     * @return
     */
    @Override
    public List<SoftwareAuditVO> statisticForAudit(Map<String, Object> map) {
        List<SoftwareAuditVO> softwareVOList = new ArrayList<>();
        try {
            softwareVOList = softwareDao.statisticForAudit(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return softwareVOList;
    }


    private List<SoftwareVO> queryStatisticList(Map<String, Object> map) {
        List<SoftwareVO> softwareVOList = new ArrayList<SoftwareVO>();
        try {
            softwareVOList = softwareDao.queryStatisticList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return softwareVOList;
    }


    /**
     * 分审出库操作
     *
     * @param software 需要分审案件
     * @return
     * @author bxt-chenjian
     * @date 2016.5.26
     */
    @Override
    @Transactional
    public boolean checkOutStorage(Software software) {
        boolean flag = false;
        /********************分审出库*****************
         * 1.将匹配状态置为√						   *
         * 2.绑定当前操作员id						   *
         * 3.绑定分审员id							   *
         * 4.将待分审状态更为已分审状态				   *
         * 5.软件表中库位号清空					   *
         * 6.解除仓库和案件关系					   *
         * 7.该软件所在仓库需要沉底					   *
         * *****************************************/
        if (software == null) {
            log.debug("分审出库操作softWare为null！！！");
            return flag;
        }
        if (software.getSoftwarestatus() == SoftWareStatusEnum.TRIAL_OK.getValue()) {
            log.debug("该软件为已分审状态，不用再次分审。。。");
            return flag;
        }
        /**保存software相关状态**/
        software.setUpdatedate(new Date());
        software.setSoftwarestatus(SoftWareStatusEnum.TRIAL_OK.getValue());
        this.softwareDao.update(software);

        /**解除仓库和案件关系**/
        StorageSoftware storageSoftware = this.storageSoftwareDao.selectBySoftwareId(software.getId());
        if (storageSoftware == null) {
            log.debug("分审出库操作根据软件id查询storageSoftware为null！！！");
            return flag;
        }
        this.storageSoftwareDao.delete(storageSoftware.getId());
        String storageId = storageSoftware.getStorageid();//该软件所在仓库位置id

        /**更新当前仓库位置状态**/
//		this.storageDropDown(storageId);
        Storage storage = this.storageDao.select(storageId);
        if (storage == null) {
            log.debug("分审出库操作根据仓库id查询storage为null！！！");
            return flag;
        }
        storage.setUpdatedate(new Date());
//		storage.setHouseorder(0);
        storage.setIsuse(0);
        this.storageDao.update(storage);

        /**查询该仓库位置的二级仓库,更新对应二级仓库状态**/
        String fStorageid = storage.getFid();//该软件所在仓库的父仓库id
        Storage fStorage = this.storageDao.select(fStorageid);
        if (fStorage == null) {
            log.debug("根据id查询二级仓库结果为null！！！");
        }
        if (fStorage.getUsespace() > 0) {
            fStorage.setUsespace(fStorage.getUsespace() - 1);//对应二级仓库的已容纳量-1
        }
        fStorage.setSurplus(fStorage.getSurplus() + 1);//剩余量
        fStorage.setUpdatedate(new Date());
        this.storageDao.update(fStorage);
        flag = true;
        return flag;
    }

    /**
     * 仓库沉底
     *
     * @param storageId 软件所在仓库位置   三级
     * @return
     * @author bxt-chenjian
     * @date 2016.5.26
     */
    @Override
    @Transactional
    public String storageDropDown(String storageId) {
        Storage storage = this.storageDao.select(storageId);
        if (storage == null) {
            log.debug("分审出库操作根据仓库id查询storage为null！！！");
            return "";
        }
        int houseorder = storage.getHouseorder();//软件所在仓库位置houseorder
        String fStorageid = storage.getFid();//该软件所在仓库的父仓库id

        /**查询该仓库位置的二级仓库**/
        Storage fStorage = this.storageDao.select(fStorageid);
        if (fStorage == null) {
            log.debug("根据id查询二级仓库结果为null！！！");
        }
        fStorage.setUsespace(fStorage.getUsespace() - 1);//对应二级仓库的已容纳量-1
        fStorage.setUpdatedate(new Date());
        this.storageDao.update(fStorage);

        /**查询该二级仓库下所有的三级仓库**/
        List<Storage> allStorages = new ArrayList<>();
        allStorages = this.storageDao.queryAllStorageByfid(fStorageid);
        /**将所有该软件对应仓库位置的所有上边的仓库的houseorder都加1 即沉底**/
        for (Storage storage2 : allStorages) {
            if (storage2.getHouseorder() < houseorder) {
                storage2.setHouseorder(storage2.getHouseorder() + 1);
                storage2.setUpdatedate(new Date());
                this.storageDao.update(storage2);
            }
        }
        /**将该软件对应的仓库位置的houseorder置为1 即放置最顶层并且状态置为可用**/
        storage.setUpdatedate(new Date());
        storage.setHouseorder(1);
        storage.setIsuse(1);
        this.storageDao.update(storage);
        return "";
    }


	@Override
	@Transactional
	public JsonResult exportForAudit(String serialNum, String person,String startDate, String endDate) {
		Admin admin = Admin.sessionAdmin();
		JsonResult result = new JsonResult();
		String datePattern = "yyyy-MM-dd";
		Software software = this.querySoftwareBySerialnum(serialNum);
		if(software==null){
			result.setState(false);
			result.setMsg("该流水号在数据库中没有记录！");
			return result;
		}
		//做出库操作 更改software 状态 更改库状态
		Storage store = software.getStorage();
		if(store==null){
			result.setState(false);
			result.setMsg("该流水号存在数据库中但无仓库记录！");
			result.setData(JsonUtils.objectToJsonString(software));
			return result;
		}
		try{
			//1.删除库存关系和软件关系
			//删除storage—software 关系表
			StorageSoftware ss = this.storageSoftwareDao.selectBySoftwareId(software.getId());
			this.storageSoftwareDao.delete(ss.getId());
			//2.更新仓库信息
			//更新三级仓库
			store.setIsuse(0);
			store.setUpdatedate(new Date());
			this.storageDao.update(store);
			//更新二级仓库
			Storage secondStore = this.storageDao.select(store.getFid());
			//避免仓库出现负数
			if(secondStore.getUsespace() == 0 && secondStore.getSurplus() == secondStore.getTotalspace()){

			}else{
				secondStore.setUsespace(secondStore.getUsespace()-1);
				secondStore.setSurplus(secondStore.getSurplus()+1);
			}
			secondStore.setUpdatedate(new Date());
			this.storageDao.update(secondStore);
			//3.对出库软件的状态和时间进行操作
			//对software状态进行更改 对时间 和状态进行判定
			int state = software.getSoftwarestatus();
			if(state!=SoftWareStatusEnum.PENDING_TRIAL.getValue()){
				//如果软件状态不为待分审    软件库状态变更为不在库
                software.setIsexist(0);
                software.setAdminid(Admin.sessionAdmin().getID());
                software.setUpdatedate(new Date());
                this.softwareDao.update(software);
                result.setState(false);
                String msg;
                switch (state) {
                    case 1:
                        msg = "该记录目前的状态为待缴费！";
                        break;
                    case 3:
                        msg = "该记录目前的状态为已分审！";
                        break;
                    case 4:
                        msg = "该记录目前的状态为待补证！";
                        break;
                    case 5:
                        msg = "该记录目前的状态为待制证！";
                        break;
                    case 6:
                        msg = "该记录目前的状态为已出库！";
                        break;
                    default:
                        msg = "该记录目前的状态异常！";
                        break;
                }
                result.setMsg(msg);
                result.setData(JsonUtils.objectToJsonString(software));
                logService.saveLog(software,SoftwareOperationEnum.TRIAL_OUT_STORAGE.getValue(),admin);
                return result;
			}
			//如果软件状态为待分审    软件库状态变更为不在库
			long certTime = software.getCertificatedate().getTime();
			if(certTime>Function.parseDate(endDate, datePattern).getTime()||certTime<Function.parseDate(startDate, datePattern).getTime()){
				//更新software 的状态为已经出库
				software.setIsexist(0);
	            software.setAdminid(Admin.sessionAdmin().getID());
	            software.setUpdatedate(new Date());
                software.setAuditdate(new Date());
				this.softwareDao.update(software);
                //添加软件操作日志操作-----------分审出库
                logService.saveLog(software,SoftwareOperationEnum.TRIAL_OUT_STORAGE.getValue(),admin);
				result.setState(false);
				result.setMsg("该记录出库成功，但制证日期与选定的日期不符！");
				result.setData(JsonUtils.objectToJsonString(software));
				return result;
			}
			software.setIsexist(0);
			software.setTrialId(person);
            software.setAdminid(Admin.sessionAdmin().getID());
			software.setSoftwarestatus(SoftWareStatusEnum.TRIAL_OK.getValue());
			software.setUpdatedate(new Date());
            software.setAuditdate(new Date());
			this.softwareDao.update(software);
		}catch(Exception e){
			e.printStackTrace();
		}
        //添加软件操作日志操作-----------分审出库
        logService.saveLog(software,SoftwareOperationEnum.TRIAL_OUT_STORAGE.getValue(),admin);
		result.setData(JsonUtils.objectToJsonString(software));
		result.setState(true);
		result.setMsg("出库成功");
		return result;
	}




    @Override
    public List<Software> queryWaitAuditSoftware(Map<String, String> map) {
        return this.softwareDao.queryWaitAuditSoftware(map);
    }

    /**
     * 预入库操作
     *
     * @param date 选定的出证时间
     * @param num  序列号
     * @param pos  选定的三级仓位ID
     * @param reBackType  回库类型
     * @return
     */
    @Override
    public JsonResult preCheckIn(String date,String num, String pos,Integer reBackType) {
        JsonResult result = new JsonResult();
        int exp = Function.getMemcachedExpireTime();

        //防止并发时候 分配库位号重复
        if(!Function.isEmpty((String) mcc.get(num))){
            return returnFalseMessage(result,num,"该条流水号已经扫过");
        }else{
            mcc.set(num,exp,num);
        }

        // 请选择库位号！
        if (Function.isEmpty(pos)) {
            return returnFalseMessage(result,num,"请选择库位号！");
        }

        // 流水号长度不合法
        if (!checkSerialNum(num)) {
            return returnFalseMessage(result,num,"流水号输入长度不合法！");
        }

        Software soft = this.querySoftwareBySerialnum(num);// 当前软件对象
        Storage store = this.storageService.findStorage(pos);// 当前仓库对象

        // 数据库中无该流水号记录
        if (soft == null) {
            return returnFalseMessage(result,num,"该记录在仓库中不存在！");
        }
        
        /******************首先对日期进行校验，如果是逾期回库的检查软件的出证日期是否小于当前的日期***********************/
        /******************然后对其他类型入库的软件进行制证日期和选择日期的比较***********************/
        //逾期回库会检查软件的日期和当前日期，其他回库检查制证日期和所选日期
        if(SoftWareStatusEnum.TO_OVERDUE_CHECKIN.getValue() == reBackType){
        	if(soft.getCertificatedate().before(new Date())){
        		log.debug("仅仅是日期符合逾期回库操作！随后将检查软件状态是否符合条件");
        	}else{
        		soft.setTemphouseorder("该记录制证日期不符合逾期回库！");
                result.setMsg("异常");
                result.setState(false);
                result.setData(JsonUtils.objectToJsonString(soft));
        		return result;
        	}
        }else{
        	// 出证日期与记录不符
        	long adate = Function.parseDate(date, "yyyy-MM-dd").getTime();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	long softDate = Function.parseDate(sdf.format(soft.getCertificatedate()),"yyyy-MM-dd").getTime();
        	if (adate != softDate) {
        		soft.setTemphouseorder("制证日期与记录不符！");
                result.setMsg("异常");
                result.setState(false);
                result.setData(JsonUtils.objectToJsonString(soft));
        		return result;
        	}
        }
        /********************************然后对入库操作的类型进行区别*************************************/
        int currentState = soft.getSoftwarestatus();
        //新入库
        if(SoftWareStatusEnum.TO_NEW_CHECKIN.getValue() == reBackType){
            if (currentState != SoftWareStatusEnum.TO_BE_IN_STORAGE.getValue()) {
                switch (currentState) {
                    case 1:
                    	soft.setTemphouseorder("该记录的当前状态为 待缴费！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    case 2:
                    	soft.setTemphouseorder("该记录的当前状态为 待分审！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    case 3:
                    	soft.setTemphouseorder("该记录的当前状态为 已分审！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    case 4:
                    	soft.setTemphouseorder("该记录的当前状态为 待补正！");
                    	result.setMsg("异常");
                    	result.setState(false);
                    	result.setData(JsonUtils.objectToJsonString(soft));
                    	return result;
                    case 5:
                    	soft.setTemphouseorder("该记录的当前状态为 待制证！");
                    	result.setMsg("异常");
                    	result.setState(false);
                    	result.setData(JsonUtils.objectToJsonString(soft));
                    	return result;
                    case 6:
                    	soft.setTemphouseorder("该记录的当前状态为 已制证！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    case 7:
                    	soft.setTemphouseorder("该记录的当前状态为 已撤回！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                }
            }
        }
        //异常回库
        if(SoftWareStatusEnum.TO_EXECPTION_CHECKIN.getValue() == reBackType){
            log.debug("first：---------------------异常回库 ");
            switch (currentState) {
	            case 0:
	            	soft.setTemphouseorder("该记录的当前状态为 未入库！");
	                result.setMsg("异常");
	                result.setState(false);
	                result.setData(JsonUtils.objectToJsonString(soft));
	        		return result;
	            case 6:
                	soft.setTemphouseorder("该记录的当前状态为 已制证！");
                    result.setMsg("异常");
                    result.setState(false);
                    result.setData(JsonUtils.objectToJsonString(soft));
            		return result;
	        }
        }
        
        // 如果是制证回库/逾期回库 的数据 则要求进行状态判断
        if(SoftWareStatusEnum.TO_MARKCARD_CHECKIN.getValue() == reBackType || SoftWareStatusEnum.TO_OVERDUE_CHECKIN.getValue() == reBackType){
            if (currentState != SoftWareStatusEnum.TRIAL_OK.getValue() && currentState != SoftWareStatusEnum.PENDING_CORRECT.getValue()) {
                switch (currentState) {
	                case 0:
	                	soft.setTemphouseorder("该记录的当前状态为 未入库！");
	                    result.setMsg("异常");
	                    result.setState(false);
	                    result.setData(JsonUtils.objectToJsonString(soft));
	            		return result;
	                case 1:
	                	soft.setTemphouseorder("该记录的当前状态为 待缴费！");
	                    result.setMsg("异常");
	                    result.setState(false);
	                    result.setData(JsonUtils.objectToJsonString(soft));
	            		return result;
	                case 2:
	                	soft.setTemphouseorder("该记录的当前状态为 待分审！");
	                    result.setMsg("异常");
	                    result.setState(false);
	                    result.setData(JsonUtils.objectToJsonString(soft));
	            		return result;
	                case 5:
	                	soft.setTemphouseorder("该记录的当前状态为 待制证！");
	                    result.setMsg("异常");
	                    result.setState(false);
	                    result.setData(JsonUtils.objectToJsonString(soft));
	            		return result;
	                case 6:
	                	soft.setTemphouseorder("该记录的当前状态为 已制证！");
	                    result.setMsg("异常");
	                    result.setState(false);
	                    result.setData(JsonUtils.objectToJsonString(soft));
	            		return result;
	                case 7:
	                	soft.setTemphouseorder("该记录的当前状态为 已撤回！");
	                    result.setMsg("异常");
	                    result.setState(false);
	                    result.setData(JsonUtils.objectToJsonString(soft));
	            		return result;
                }
            }
        }
        //快捷入库：雍和
        if(SoftWareStatusEnum.TO_QUICK_CHECKIN.getValue() == reBackType){
            if (currentState != SoftWareStatusEnum.TO_BE_IN_STORAGE.getValue()) {
                switch (currentState) {
                    case 1:
                    	soft.setTemphouseorder("该记录的当前状态为 待缴费！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    case 2:
                    	soft.setTemphouseorder("该记录的当前状态为 待分审！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    case 5:
                    	soft.setTemphouseorder("该记录的当前状态为 待制证！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    case 6:
                    	soft.setTemphouseorder("该记录的当前状态为 已制证！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    case 7:
                    	soft.setTemphouseorder("该记录的当前状态为 已撤回！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                }
            }
        }
        
        // 判定库存
        //1 获取当前三级仓库中最大的库位序列号
        Storage maxThirdStorage = storageService.findMaxThirdStorage(store.getId());
        int maxNow = 0;
        if(maxThirdStorage!=null){ //三级仓位有存放记录
            maxNow = maxThirdStorage.getHouseorder();
        }else{	//三级仓位无存放记录
            maxNow = store.getUsespace();
        }
        //2 获取二级仓库的总量
        int totalSpace = store.getTotalspace();
        //3 获取差值 分配仓位
        int spare = totalSpace - maxNow;
        if (spare <= 0) {// 库存不足
            return returnFalseMessage(result,num,"该库位无可用空间！");
        }else {
            // 判定是否已经有存入的记录 使用software 的 id 去softwarestorage去比对 没有记录 说明没存过
            boolean exist = this.storageSoftwareService.isExist(soft.getId()); //count
            if (exist) {// 存在于数据库中
            	soft.setTemphouseorder("该记录已入库！");
                result.setMsg("异常");
                result.setState(false);
                result.setData(JsonUtils.objectToJsonString(soft));
        		return result;
            }
            // 有可用库存spare 从缓存中得到所有的对象
            List<Software> softLists = (List<Software>) mcc.get(pos);
            if (softLists == null) {// 缓存中无数据
                softLists = new ArrayList<Software>();
                //2016-09-01更新 将原本由二级仓位获取剩余量分配库位的方式 改变为 通过获取当前二级仓库下最大的三级仓位的序号加1的方式
                soft.setTemphouseorder(store.getGroupname() + store.getName() + "-" + (maxNow + 1));
                softLists.add(soft);
                result.setMsg("√");
                result.setState(true);
                result.setData(JsonUtils.objectToJsonString(soft));
                mcc.set(pos, exp, softLists);
                mcc.delete(num);
                return result;
            } else {// 缓存中有数据
                int existSize = softLists.size();// 获取已缓存的数量
                if (existSize < spare) {// 还可以放入该仓库
                    // 判定缓存中是否已存在
                    boolean flag = false;
                    for (Software software : softLists) {
                        if (software.getSerialnum().equalsIgnoreCase(num)) {
                            flag = true;
                        }
                    }
                    if (flag) {
                    	soft.setTemphouseorder("该条流水号已扫描过！");
                        result.setMsg("异常");
                        result.setState(false);
                        result.setData(JsonUtils.objectToJsonString(soft));
                		return result;
                    }else {
                        //2016-09-01更新 将原本由二级仓位获取剩余量分配库位的方式 改变为 通过获取当前二级仓库下最大的三级仓位的序号加1的方式
                        soft.setTemphouseorder(store.getGroupname() + store.getName() + "-" + (maxNow + existSize + 1));
                        softLists.add(soft);
                        result.setMsg("√");
                        result.setState(true);
                        result.setData(JsonUtils.objectToJsonString(soft));
                        mcc.set(pos, exp, softLists);
                        mcc.delete(num);
                        return result;
                    }
                }else {// 仓库满了
                    return returnFalseMessage(result,num,"该库位无可用空间！");
                }
            }
        }

    }

    /**
     * 判定序列号长度是否合法
     *
     * @param serialNum
     * @return 合法则返回true
     */
    public boolean checkSerialNum(String serialNum) {
        boolean flag = false;
        if (serialNum == null) {
            return flag;
        }
        int size = Function.getLeagalLengthOfSerialNum();
        int len = serialNum.trim().length();
        if (len == size) {
            flag = true;
        }
        return flag;
    }

    /**
     * 软件状态不合法返回页面的方法
     * @param result
     * @param serialNumber
     * @param message
     * @return
     */
    private JsonResult returnFalseMessage(JsonResult result,String serialNumber,String message){
        mcc.delete(serialNumber);
        Software soft = new Software();
        soft.setSerialnum(serialNumber);
        soft.setTemphouseorder(message);
        soft.setSoftwarename("");
        soft.setApplyperson("");
        soft.setSalesman("");
        result.setMsg("异常");
        result.setState(false);
        result.setData(JsonUtils.objectToJsonString(soft));
        return result;
    }


    // 导出选中excel表格
    public HSSFWorkbook export(List<Software> list) {
        String[] excelHeader = {"流水号", "申请人", "软件全称", "制证日期","业务员","审查员", "匹配状态"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("批量补正表");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        // 设置列宽度（像素） // 添加表格头
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            Software software = (Software) list.get(i);
            if (software != null) {
                if (software.getSerialnum() == null) {
                    row.createCell(0).setCellValue("");
                } else {
                    row.createCell(0).setCellValue(software.getSerialnum()); // 流水号
                }
                row.createCell(1).setCellValue(software.getApplyperson());// 申请人
                row.createCell(2).setCellValue(software.getSoftwarename());// 软件全称
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                row.createCell(3).setCellValue(sdf.format(software.getCertificatedate()));// 制证日期
                if (software.getSalesman() == null) {
                    row.createCell(4).setCellValue("");
                } else {
                    row.createCell(4).setCellValue(software.getSalesman()); // 业务员
                }
                if (software.getTrialAdmin() == null||software.getTrialAdmin().getRelName() == null) {
                    row.createCell(5).setCellValue("");
                } else {
                    row.createCell(5).setCellValue(software.getTrialAdmin().getRelName()); // 审查员
                }

                if (software.getSoftwarestatus() == 0) {
                    row.createCell(6).setCellValue("该记录的当前状态为 未入库！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 1) {
                    row.createCell(6).setCellValue("该记录的当前状态为 待缴费！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 2) {
                    row.createCell(6).setCellValue("该记录的当前状态为 待分审！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 4) {
                    row.createCell(6).setCellValue("√");// 匹配状态
                }
                /*if (software.getSoftwarestatus() == 4 && software.getTemphouseorder().equals("true")) {
                    row.createCell(4).setCellValue("√");// 匹配状态
                }*/
               /* if (software.getSoftwarestatus() == 4 && software.getTemphouseorder().equals("false")) {
                    row.createCell(4).setCellValue("该记录的当前状态为 待补证！");// 匹配状态
                }*/
                if (software.getSoftwarestatus() == 5) {
                    row.createCell(6).setCellValue("该记录的当前状态为 待制证！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 6) {
                    row.createCell(6).setCellValue("该记录的当前状态为 已制证！");// 匹配状态
                }
                if ((Integer) software.getSoftwarestatus() == null) {
                    row.createCell(6).setCellValue("该记录异常！");// 匹配状态
                }
            }
        }
        return wb;
    }



    public HSSFWorkbook generateExcelForAs(List<Software> list) {
        if (list == null || list.size() == 0) {
            return new HSSFWorkbook();
        }
        String[] excelHeader = {"流水号", "申请人", "软件全称", "制证日期", "匹配状态"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("批量补正表");
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        // 设置列宽度（像素） // 添加表格头
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            Software software = (Software) list.get(i);
            if (software != null) {
                if (software.getSerialnum() == null) {
                    row.createCell(0).setCellValue(" ");
                } else {
                    row.createCell(0).setCellValue(software.getSerialnum()); // 流水号
                }
                row.createCell(1).setCellValue(software.getApplyperson());// 申请人
                row.createCell(2).setCellValue(software.getSoftwarename());// 软件全称
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                row.createCell(3).setCellValue(sdf.format(software.getCertificatedate()));// 制证日期
                if (software.getSoftwarestatus() == 0) {
                    row.createCell(4).setCellValue("该记录的当前状态为 未入库！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 1) {
                    row.createCell(4).setCellValue("该记录的当前状态为 待缴费！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 2) {
                    row.createCell(4).setCellValue("该记录的当前状态为 待分审！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 4 && software.getTemphouseorder().equals("true")) {
                    row.createCell(4).setCellValue("√");// 匹配状态
                }
                if (software.getSoftwarestatus() == 4 && software.getTemphouseorder().equals("false")) {
                    row.createCell(4).setCellValue("该记录的当前状态为 待补证！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 5) {
                    row.createCell(4).setCellValue("该记录的当前状态为 待制证！");// 匹配状态
                }
                if (software.getSoftwarestatus() == 6) {
                    row.createCell(4).setCellValue("该记录的当前状态为 已制证！");// 匹配状态
                }
                if ((Integer) software.getSoftwarestatus() == null) {
                    row.createCell(4).setCellValue("该记录异常！");// 匹配状态
                }
            }
        }
        return wb;
    }

    @Override
    public PageObject querySoftInfoListParam(QueryParamater param) {
        return softwareDao.querySoftInfoListParam(param);
    }

    /**
     * 通过仓位信息查询出 该仓位信息主要放的是哪天出证的软件
     *
     * @param storageId
     * @return
     */
    @Override
    public Software findSoftwareByStorageId(String storageId) {
        Software software = new Software();
        try {
            software = softwareDao.findSoftwareByStorageId(storageId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return software;
    }

	@Override
	@Transactional
	public Boolean updateCreDate(String certificatedate, Software software,String adminId) {
		Admin admin = Admin.sessionAdmin();
		Boolean flag = false;
		//1.修改该软件的出证日期
		software.setCertificatedate(MyDate.get(certificatedate));
		//2.将该软件的在库状态修改
		software.setIsexist(0);
		this.softwareDao.update(software);
		//判断软件是不是在库，在库的话对仓库进行释放
		if(software.getStorage() != null){
			//3.将该软件对应的仓库二级仓库使用量减1，剩余量加1。
			Storage fStorage = storageDao.selectByStorageId(software.getStorage().getFid());
			fStorage.setUsespace(fStorage.getUsespace()-1);
			fStorage.setSurplus(fStorage.getSurplus()+1);
			this.storageDao.update(fStorage);
			//4.对三级仓库占用状态进行释放
			Storage storage = storageDao.selectByStorageId(software.getStorage().getId());
			storage.setIsuse(0);
			this.storageDao.update(storage);
			//5.清除软件和库之间的关系
			String ssid = (storageSoftwareDao.selectBySoftwareId(software.getId())).getId();
			this.storageSoftwareDao.delete(ssid);
		}
		//添加软件操作日志操作---------修改出证日期
        logService.saveLog(software,SoftwareOperationEnum.UPDATE_CERTIFICATE_DATE.getValue(),admin);
		flag = true;
		return flag;
	}

	@Override
	public JsonResult exceptionCheckout(String serialNum) {
		Admin admin = Admin.sessionAdmin();
		JsonResult result = new JsonResult();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		config.setExcludes(new String[]{"storage"});
		
		Software software = this.querySoftwareBySerialnum(serialNum);
		if(software==null){
			Software errorSoftware = new Software();
			errorSoftware.setSerialnum(serialNum);
			result.setState(false);
			result.setMsg("该流水号在数据库中没有记录！");
            result.setData(JsonUtils.objectToJsonString(errorSoftware));
			return result;
		}
		//做出库操作 更改software 状态 更改库状态
		Storage store = software.getStorage();
		if(store==null){
			result.setState(false);
			result.setMsg("该流水号在系统中但不在仓位中 ！");
            result.setData(JsonUtils.objectToJsonString(software));
			return result;
		}
		try{
			//1.删除库存关系和软件关系
			//删除storage—software 关系表
			StorageSoftware ss = this.storageSoftwareDao.selectBySoftwareId(software.getId());
			this.storageSoftwareDao.delete(ss.getId());
			//2.更新仓库信息
			//更新三级仓库
			store.setIsuse(0);
			store.setUpdatedate(new Date());
			this.storageDao.update(store);
			//更新二级仓库
			Storage secondStore = this.storageDao.select(store.getFid());
			//避免仓库出现负数
			if(secondStore.getUsespace() == 0 && secondStore.getSurplus() == secondStore.getTotalspace()){
				
			}else{
				secondStore.setUsespace(secondStore.getUsespace()-1);
				secondStore.setSurplus(secondStore.getSurplus()+1);
			}
			secondStore.setUpdatedate(new Date());
			this.storageDao.update(secondStore);
			//3.对出库软件进行出库操作，更新状态为不在库
			software.setIsexist(0);
			software.setAdminid(Admin.sessionAdmin().getID());
			software.setUpdatedate(new Date());
			this.softwareDao.update(software);

			//添加软件操作日志操作-----------异常出库
            logService.saveLog(software,SoftwareOperationEnum.EXCEPTION_CHECKOUT.getValue(),admin);
        }catch(Exception e){
			e.printStackTrace();
		}
        result.setData(JsonUtils.objectToJsonString(software));
		result.setState(true);
		result.setMsg("该软件异常出库成功");
		return result;	
			
	}

	@Override
	@Transactional
	public Boolean updateSoftwareStatus(String softwarestatus, Software software, String adminId) {
		Admin admin = Admin.sessionAdmin();
		Boolean flag = false;
		//1.修改该软件的状态
		software.setSoftwarestatus(Integer.valueOf(softwarestatus));
		software.setUpdatedate(new Date());
		this.softwareDao.update(software);
		//2.添加软件操作日志操作---------修改软件状态
        logService.saveLog(software,SoftwareOperationEnum.UPDATE_SOFTWARE_STATUS.getValue(),admin);
		flag = true;
		return flag;
	}
	
	/**
	 * 批量核费 导入excel 返回所有的异常信息
     * 当前逻辑只匹配流水号
	 */
	@Override
	@Transactional
	public List<Software> importExcelForAccount(InputStream inputStream,String adminId) {
		Admin admin = Admin.sessionAdmin();
		List<Software> badList = new ArrayList<Software>();
        List<Software> goodList = new ArrayList<Software>();
        List<Software> importSofts = this.importAccountExcel(inputStream);
        //获取所有审核员的名字
        for (Software software : importSofts) {
            /**根据流水号判断数据库中是否存在该软件**/
            String serialnum = software.getSerialnum();
            //避免由于导入的excel文件样式问题导致的software 有对象 但是数据都是空的情况
            if(Function.isEmpty(serialnum)){
                continue;
            }
            Software soft = this.softwareDao.selectByserialnum(serialnum);
            if (soft != null) {
                //对状态进行判定
                int state = soft.getSoftwarestatus();
                if (SoftWareStatusEnum.PENDING_PAYMENT.getValue() == state) {
                	soft.setSoftwarestatus(SoftWareStatusEnum.PENDING_TRIAL.getValue());
                	soft.setUpdatedate(new Date());
					boolean flag = this.saveSoftware(soft);
					Storage storage = this.storageService.findStorageBysoftwareId(soft.getId());
					String des = storage.getGroupname() + "" + storage.getName() + "-" + storage.getHouseorder();
					
					if (flag) {
						//添加软件操作日志操作-----------批量核费
                        logService.saveLog(soft,SoftwareOperationEnum.VERIFICATION_FEE.getValue(),admin);
						soft.setDescription(des);
						soft.setTemphouseorder("");
						goodList.add(soft);
					} else {
						soft.setDescription(des);
						soft.setTemphouseorder("✘  核费失败！");
						badList.add(soft);
					}
					
                } else if (SoftWareStatusEnum.TO_BE_IN_STORAGE.getValue() == state) {
                	soft.setDescription("");
                	soft.setTemphouseorder("该记录还未入库！");
                	badList.add(soft);
				} else {
					Storage storage = this.storageService.findStorageBysoftwareId(soft.getId());
					if(storage != null){
						soft.setDescription(storage.getGroupname() + "" + storage.getName() + "-" + storage.getHouseorder());
						soft.setTemphouseorder("该记录已核费！");
	                    badList.add(soft);
					}else{
						soft.setDescription("");
						soft.setTemphouseorder("该记录不在库且已核费！");
	                    badList.add(soft);
					}
				}
                
            } else {
                Software badSoft = new Software();
                badSoft.setSerialnum(serialnum);
                badSoft.setCertificatedate(null);
                badSoft.setApplyperson("");
                badSoft.setSoftwarename("");
                badSoft.setSalesman("");
                badSoft.setDescription("");
                badSoft.setTemphouseorder("数据库中不存在该流水号记录！");
                badList.add(badSoft);
            }
		}
		goodList.addAll(badList);
		return goodList;
	}
	
	/**
	 * 批量核费导入Excel
	 * @date 2016-9-18下午2:57:22
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param inputStream
	 * @return
	 */
	public List<Software> importAccountExcel(InputStream inputStream) {
        List<Software> softlist = new ArrayList<Software>();
        softlist = ReadWriteExcelUtil.readAccountExcel(inputStream);
        log.debug("Excel导入softlist长度：" + softlist.size());
        return softlist;
    }

	@Override
	public PageObject queryAllSoftware(QueryParamater param) {
		PageObject po = null;
        try {
            po = this.softwareDao.queryAllSoftware(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return po;
	}

	@Override
	@Transactional
	public JsonResult toRevoke(String serialNum) {
		Admin admin = Admin.sessionAdmin();
		JsonResult result = new JsonResult();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		config.setExcludes(new String[]{"storage"});
		
		Software software = this.querySoftwareBySerialnum(serialNum);
		if(software==null){
			Software errorSoftware = new Software();
			errorSoftware.setSerialnum(serialNum);
			result.setState(false);
			result.setMsg("该流水号在数据库中没有记录！");
            result.setData(JsonUtils.objectToJsonString(errorSoftware));
			return result;
		}
		
		if(SoftWareStatusEnum.REVOKE.getValue() == software.getSoftwarestatus()){
			result.setState(false);
			result.setMsg("该软件为已撤回状态！");
            result.setData(JsonUtils.objectToJsonString(software));
			return result;
		}else{
			//做出库操作 更改software 状态 更改库状态
			Storage store = software.getStorage();
			if(store==null){
				software.setSoftwarestatus(SoftWareStatusEnum.REVOKE.getValue());
				software.setAdminid(Admin.sessionAdmin().getID());
				software.setUpdatedate(new Date());
				Boolean flag = this.saveSoftware(software);
				if(flag){
					//添加软件操作日志操作-----------撤回操作
                    logService.saveLog(software,SoftwareOperationEnum.DO_REVOKE.getValue(),admin);
				}
				result.setState(true);
				result.setMsg("√");
                result.setData(JsonUtils.objectToJsonString(software));
				return result;
			}
			try{
				//1.删除库存关系和软件关系
				//删除storage—software 关系表
				StorageSoftware ss = this.storageSoftwareDao.selectBySoftwareId(software.getId());
				this.storageSoftwareDao.delete(ss.getId());
				//2.更新仓库信息
				//更新三级仓库
				store.setIsuse(0);
				store.setUpdatedate(new Date());
				this.storageDao.update(store);
				//更新二级仓库
				Storage secondStore = this.storageDao.select(store.getFid());
				//避免仓库出现负数
				if(secondStore.getUsespace() == 0 && secondStore.getSurplus() == secondStore.getTotalspace()){
					
				}else{
					secondStore.setUsespace(secondStore.getUsespace()-1);
					secondStore.setSurplus(secondStore.getSurplus()+1);
				}
				secondStore.setUpdatedate(new Date());
				this.storageDao.update(secondStore);
				//3.对出库软件进行出库操作，更新状态为不在库,并且修改软件的状态为撤回状态
				software.setIsexist(0);
				software.setSoftwarestatus(SoftWareStatusEnum.REVOKE.getValue());
				software.setAdminid(Admin.sessionAdmin().getID());
				software.setUpdatedate(new Date());
				this.softwareDao.update(software);
				
				//添加软件操作日志操作-----------撤回操作
                logService.saveLog(software,SoftwareOperationEnum.DO_REVOKE.getValue(),admin);
			}catch(Exception e){
				e.printStackTrace();
			}
            result.setData(JsonUtils.objectToJsonString(software));
			result.setState(true);
			result.setMsg("√");
			return result;
		}
	}

	@Override
	public PageObject querySoftwareAndSfhistory(QueryParamater param) {
		return softwareDao.querySoftwareAndSfhistory(param);
	}
	
	
	
}