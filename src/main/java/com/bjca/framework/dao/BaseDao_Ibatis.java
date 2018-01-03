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
 *            实体类
 * @param <PK>
 *            主键类，必须实现Serializable接口
 * 
 * @see com.thinkon.commons.dao.GenericIbatisDao
 */
public interface BaseDao_Ibatis<T, PK extends Serializable>
{

    public int insert(T entity);

    public int delete(PK id);

    public int deleteAll(String ids);

    public int delete(PK[] ids);

    public int update(T entity);

    public T select(PK id);

    public int count(Map map);

    public PageObject query(QueryParamater param);

    public List queryAll();
    public List queryList(Map map);

}
