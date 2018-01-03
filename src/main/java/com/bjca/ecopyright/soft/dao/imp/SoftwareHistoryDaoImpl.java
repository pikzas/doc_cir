package com.bjca.ecopyright.soft.dao.imp;

import java.util.Map;

import com.bjca.framework.dao.IbatisDaoImp;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import com.bjca.ecopyright.soft.dao.SoftwareHistoryDao;
import com.bjca.ecopyright.soft.model.SoftwareHistory;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
@Repository("softwareHistoryDao")
public class SoftwareHistoryDaoImpl extends IbatisDaoImp<SoftwareHistory, String> implements SoftwareHistoryDao
{

	@Override
	public PageObject querySoftwareHistoryList(QueryParamater param) {
        PageObject po = new PageObject();
        param.setTotalCount(this.countSoftwareHistoryList(param.getMap()));
        po.setCurrentPage(param.getCurrentPage());
        po.setResults(this.getSqlSession().selectList(this.getSqlmapNamespace() + ".querySoftwareHistoryList", param.getMap(),
                  new RowBounds(param.getFromIndex(), param.getNeverypage())));
        po.setNeverypage(param.getNeverypage());
        po.setTotal(param.getTotalCount());
        return po;
	}
	
	@Override
	public int countSoftwareHistoryList(Map map) {
		return this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".countSoftwareHistoryList",map);
	}

}
