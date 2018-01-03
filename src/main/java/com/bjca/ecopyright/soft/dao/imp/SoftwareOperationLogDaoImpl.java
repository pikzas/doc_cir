package com.bjca.ecopyright.soft.dao.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.bjca.ecopyright.soft.dao.SoftwareOperationLogDao;
import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareOperationLog;
import com.bjca.framework.dao.IbatisDaoImp;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
@Repository("softwareOperationLogDao")
public class SoftwareOperationLogDaoImpl extends IbatisDaoImp<SoftwareOperationLog, String> implements SoftwareOperationLogDao
{

	@Override
	public List<SoftwareOperationLog> selectByserialnum(String serialnum) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".selectByserialnum", serialnum);
	}


	@Override
	public PageObject querySoftwareOperationLog(QueryParamater param) {
		RowBounds rb = new RowBounds();
        PageObject po = new PageObject();
        param.setTotalCount(this.count(param.getMap()));
        po.setCurrentPage(param.getCurrentPage());//TODO 设置当前页
        po.setResults(this.getSqlSession().selectList(this.getSqlmapNamespace() + ".querySoftwareOperationLog", param.getMap(),
                  new RowBounds(param.getFromIndex(), param.getNeverypage())));
        po.setNeverypage(param.getNeverypage());
        po.setTotal(param.getTotalCount());
        return po;
	}




}
