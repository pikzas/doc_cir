package com.bjca.framework.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

/**
 * GenericDao DAO层泛型接口，定义基本的DAO功能
 * 
 * @author wangtj
 * @since 0.1
 * @param <T>
 *             实体类
 * @param <PK>
 *             主键类，必须实现Serializable接口
 * 
 * @see com.thinkon.commons.dao.GenericIbatisDao
 */
public interface BaseDao<T, PK extends Serializable>
{

	
	public int count(Map map) ;
	
	public int delete(PK id);
	
	public int delete(PK[] ids) ;
	
	public PK insert(T entity) ;
	
	public PageObject query(QueryParamater param);
	
	public List<T> queryAll();

	public T select(PK id);

	public int update(T entity);

	public void merge(T model);

	public void flush();

	//public void saveOrUpdate(T model) ;
	
	public void deleteObject(T model);
	
	public List<T> queryList(Map map);
	
}
