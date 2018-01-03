package com.bjca.ecopyright.soft.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.soft.model.SoftwareAuditVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareVO;
import com.bjca.ecopyright.system.model.JsonResult;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

public interface SoftwareService
{

     /**
      * 添加/更新
      */
     public boolean saveSoftware(Software software);


     /**
      * 根据ID查询
      */
     public Software findSoftware(String id);

     /**
      * 根据条件查询
      */
     public PageObject querySoftware(QueryParamater param);

     
     PageObject querySoftInfoList(QueryParamater param);
     
     /**
      * 删除
      */
     public boolean removeSoftware(String id);

     /**
      * Excel导入软件数据
      * @param inputStream   文件路径
      * @return 将excel转换得到的List
      * @author bxt-chenjian
      * @date 2016.5.19
      */
	public List<Software> importExcel(InputStream inputStream);
	
	 /**
     * Excel导入软件数据 新入库
     * @param inputStream   文件路径
     * @return 数据库中已存在数据
     */
	public List<Software> importExcelForRuku(InputStream inputStream);
	
	/**
     * Excel导入软件数据 制证出库
     * @param inputStream   文件路径
     * @return 数据库中已存在数据
     * @author bxt-chenjian
     * @date 2016.5.19
     */
	public List<Software> importExcelForZZCK(InputStream inputStream);


	/**
	 * 按序列号查询出软件
	 * @param serialNum
	 * @return
	 */
	public Software querySoftwareBySerialnum(String serialNum);
	/**
     * 根据出证日期查询
     * certificatedate
     * @author wangna
     */
	public int findSoftwareByCertificatedate(String certificatedate);

	/**
	 * 条件查询不分页
	 * @param map
	 * @return
	 * @author bxt-chenjian
	 * @date 2016.5.26
	 */
	public List<Software> queryList(Map<String, String> map);

	/**
	 * 查询制证列表
	 */
	public List<SoftwareVO> queryMakeCardList(String beginDate, String endDate);
	/**
	 * 查询预制证列表
	 */
	public List<SoftwareVO> queryExpectMakeCardList(String beginDate, String endDate);
	/**
	 * 查询分审列表
	 */
	public List<SoftwareVO> queryAuditList(String beginDate, String endDate);

	/**
	 * 分审核销统计
	 * @param map
	 * @return
     */
	List<SoftwareAuditVO> statisticForAudit(Map<String ,Object> map);

	/**
	 * 分审出库操作
	 * @param software 需要分审案件
	 * @return
	 * @author bxt-chenjian
	 * @date 2016.5.26
	 */
	public boolean checkOutStorage(Software software);
	/**
	 * 仓库沉底
	 * @param storageId
	 * @return
	 * @author bxt-chenjian
	 * @date 2016.5.26
	 */
	public String storageDropDown(String storageId);
	/**
	 * 查询所有待分审软件，按仓库名称排序
	 * @return
	 * @author bxt-chenjian
	 * @param map 
	 * @date 2016.5.26
	 */
	public List<Software> queryWaitAuditSoftware(Map<String, String> map);

	/**
	 * 异常回库的预入库操作
	 * @param certificatedDate 选定的出证时间
	 * @param serialNum 序列号
	 * @param storePos 仓库id
	 * @param reBackType 回库类型
	 * @return
	 */
	public JsonResult preCheckIn(String certificatedDate, String serialNum, String storePos,Integer reBackType);

	/**
	 * 导出excel用
	 * @return
	 * @wangn
	 */
	public HSSFWorkbook export(List<Software> list);

	/**
	 * 分审出库
	 * @date 2016-6-2下午3:13:05
	 * @mail chenping@bjca.org.cn
	 * @author chenping
	 * @return
	 */
	public JsonResult exportForAudit(String serialNum, String person,
			String startDate, String endDate);


	public HSSFWorkbook generateExcelForAs(List<Software> list);
	
	
	/**
	 * 修改证书查询使用
	 * @date 2016-8-11下午4:30:33
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param param
	 * @return
	 */
	public PageObject querySoftInfoListParam(QueryParamater param);


	/**
	 * 通过仓位信息查询出 该仓位信息主要放的是哪天出证的软件
	 * @return
     */
	public Software findSoftwareByStorageId(String storageId);
	
	/**
	 * 修改出证日期，并对该软件进行出库操作
	 * @date 2016-8-31上午10:30:31
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param certificatedate
	 * @param software
	 * @return
	 */
	public Boolean updateCreDate(String certificatedate,Software software,String adminId);
	
	
	/**
	 * 异常出库操作
	 * @date 2016-9-8上午11:26:29
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param serialNum
	 * @return
	 */
	public JsonResult exceptionCheckout(String serialNum);
	
	
	/**
	 * 修改软件状态
	 * @date 2016-9-12下午3:32:09
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param
	 * @param software
	 * @param adminId
	 * @return
	 */
	public Boolean updateSoftwareStatus(String softwarestatus,Software software,String adminId);
	
	
	/**
	 * 批量核费功能
	 * @date 2016-9-18下午2:25:36
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param inputStream
	 * @return
	 */
	public List<Software> importExcelForAccount(InputStream inputStream,String adminId);
	
	
	/**
	 * 查询所有软件信息，包括已制证的软件
	 * @date 2016-9-23上午10:28:52
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param param
	 * @return
	 */
	public PageObject queryAllSoftware(QueryParamater param);
	
	/**
	 * 撤回操作
	 * @date 2016-10-8下午3:02:30
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param serialNum
	 * @return
	 */
	public JsonResult toRevoke(String serialNum);
	
	/**
	 * 案件查询（包括在库和历史表记录）
	 * @date 2016-12-8上午11:17:49
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param param
	 * @return
	 */
	PageObject querySoftwareAndSfhistory(QueryParamater param);
}