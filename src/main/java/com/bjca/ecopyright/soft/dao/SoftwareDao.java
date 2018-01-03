package com.bjca.ecopyright.soft.dao;

import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.soft.model.SoftwareAuditVO;
import com.bjca.ecopyright.soft.model.SoftwareVO;
import org.apache.ibatis.session.RowBounds;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.framework.dao.BaseDao;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

public interface SoftwareDao extends BaseDao<Software, String>
{

	public Software selectByserialnum(String serialnum);

	public int countByCertificatedate(String certificatedate);
	
	PageObject querySoftInfoList(QueryParamater param);

	
	public List<SoftwareVO> queryStatisticList(Map<String, Object> map);
	
	/**
	 * 查询所有待分审软件 按仓库排序
	 * @return
	 * @author bxt-chenjian
	 * @param map 
	 * @date 2016.5.26
	 */
	public List<Software> queryWaitAuditSoftware(Map<String, String> map);
	
	public PageObject querySoftInfoListParam(QueryParamater param);
	
	
	public PageObject queryAllSoftware(QueryParamater param);

	public Software findSoftwareByStorageId(String storageId);
	/**
	 * 自定义分页查询  count
	 * @date 2016-9-1下午4:18:49
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param map
	 * @return
	 */
	public int countSoftInfoList(Map map);
	
	public int countSoftInfoListParam(Map map);
	
	PageObject querySoftwareAndSfhistory(QueryParamater param);
	
	public int countSoftwareAndSfhistory(Map map);

	/**
	 * 分审核销统计
	 * @param map
	 * @return
     */
	List<SoftwareAuditVO> statisticForAudit(Map<String, Object> map);
}
