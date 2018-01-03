package com.bjca.framework.service.imp;

import java.io.Serializable;
import java.util.List;


import com.bjca.framework.dao.BaseDao;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import com.bjca.framework.service.BaseService;

public abstract class BaseServiceImp <T, PK extends Serializable> implements  BaseService<T,PK>{

	protected BaseDao<T, PK> baseDao;
	public abstract void setBaseDao(BaseDao<T, PK> baseDao);
	@Override
	public int count() {
		
		return 0;
	}

	@Override
	public void delete(PK id) {
		baseDao.delete(id);
		
	}

	@Override
	public void deleteObject(T model) {
		
		this.baseDao.deleteObject(model);
	}

	@Override
	public T get(PK id) {
		
		return (T)this.baseDao.select(id);
	}

	@Override
	public List<T> listAll() {
		
		return baseDao.queryAll();
	}

	@Override
	public void merge(T model) {
		
		baseDao.merge(model);
	}

	@Override
	public PK save(T model) {
		
		System.out.println("save-------:"+baseDao);
		return (PK)baseDao.insert(model);
	}

	@Override
	public void saveOrUpdate(T model) {
		
		//baseDao.saveOrUpdate(model);
	}

	@Override
	public void update(T model) {
		
		baseDao.update(model);
	}
	public PageObject query(QueryParamater param){
		
		return baseDao.query(param);
		
	}
}
