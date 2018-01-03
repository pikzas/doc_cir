package com.bjca.ecopyright.soft.dao.imp;

import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.soft.model.SoftwareAuditVO;
import com.bjca.ecopyright.soft.model.SoftwareVO;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.bjca.ecopyright.soft.dao.SoftwareDao;
import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.framework.dao.IbatisDaoImp;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
@Repository("softwareDao")
public class SoftwareDaoImpl extends IbatisDaoImp<Software, String> implements SoftwareDao
{

	@Override
	public Software selectByserialnum(String serialnum) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".selectByserialnum", serialnum);
	}

	@Override
	public int countByCertificatedate(String certificatedate) {
		return (int)this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".countByCertificatedate", certificatedate);
	}


	@Override
	public PageObject querySoftInfoList(QueryParamater param) {
        PageObject po = new PageObject();
        param.setTotalCount(this.countSoftInfoList(param.getMap()));
        po.setCurrentPage(param.getCurrentPage());
        po.setResults(this.getSqlSession().selectList(this.getSqlmapNamespace() + ".querySoftInfoList", param.getMap(),
                  new RowBounds(param.getFromIndex(), param.getNeverypage())));
        po.setNeverypage(param.getNeverypage());
        po.setTotal(param.getTotalCount());
        return po;
	}

	@Override
	public List<SoftwareVO> queryStatisticList(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".queryStatisticList",map);
	}

	@Override
	public List<Software> queryWaitAuditSoftware(Map<String, String> map) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".queryWaitAuditSoftware",map);
	}
	@Override
	public int countSoftInfoList(Map map) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".countSoftInfoList",map);
	}

	@Override
	public PageObject querySoftInfoListParam(QueryParamater param) {
		RowBounds rb = new RowBounds();
        PageObject po = new PageObject();
        param.setTotalCount(this.count(param.getMap()));
        po.setCurrentPage(param.getCurrentPage());
        po.setResults(this.getSqlSession().selectList(this.getSqlmapNamespace() + ".querySoftInfoListParam", param.getMap(),
                  new RowBounds(param.getFromIndex(), param.getNeverypage())));
        po.setNeverypage(param.getNeverypage());
        po.setTotal(param.getTotalCount());
        return po;
	}
	
	@Override
	public int countSoftInfoListParam(Map map) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".countSoftInfoListParam",map);
	}



	@Override
	public Software findSoftwareByStorageId(String storageId) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace()+".findSoftwareByStorageId",storageId);
	}

	@Override
	public PageObject queryAllSoftware(QueryParamater param) {
		RowBounds rb = new RowBounds();
        PageObject po = new PageObject();
        param.setTotalCount(this.countAllSoftware(param.getMap()));
        po.setCurrentPage(param.getCurrentPage());
        po.setResults(this.getSqlSession().selectList(this.getSqlmapNamespace() + ".queryAllSoftware", param.getMap(),
                  new RowBounds(param.getFromIndex(), param.getNeverypage())));
        po.setNeverypage(param.getNeverypage());
        po.setTotal(param.getTotalCount());
        return po;
	}
	
	public int countAllSoftware(Map map) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".countAllSoftware",map);
	}

	@Override
	public PageObject querySoftwareAndSfhistory(QueryParamater param) {
		PageObject po = new PageObject();
        param.setTotalCount(this.countSoftwareAndSfhistory(param.getMap()));
        po.setCurrentPage(param.getCurrentPage());
        po.setResults(this.getSqlSession().selectList(this.getSqlmapNamespace() + ".querySoftwareAndSfhistory", param.getMap(),
                  new RowBounds(param.getFromIndex(), param.getNeverypage())));
        po.setNeverypage(param.getNeverypage());
        po.setTotal(param.getTotalCount());
        return po;
	}
	
	public int countSoftwareAndSfhistory(Map map) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".countSoftwareAndSfhistory",map);
	}

	/**
	 * 分审核销统计
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<SoftwareAuditVO> statisticForAudit(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".statisticForAudit",map);
	}


}
