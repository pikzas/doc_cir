package com.bjca.framework.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.bjca.ecopyright.util.Function;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;



//public class IbatisDaoImp<T, PK extends Serializable> extends SqlSessionDaoSupport implements BaseDao<T, PK>
public class IbatisDaoImpl<T, PK extends Serializable> extends SqlSessionDaoSupport implements BaseDao_Ibatis<T, PK>
{

	  // sqlmap.xml定义文件中对应的sqlid
    public static final String SQLID_INSERT = "insert";
    public static final String SQLID_UPDATE = "update";
    public static final String SQLID_DELETE = "delete";
    public static final String SQLID_SELECT = "select";
    public static final String SQLID_SELECT_PK = "selectPk";
    public static final String SQLID_SELECT_FK = "selectFk";
    public static final String SQLID_COUNT = "count";
    public static final String SQLID_QUERY = "query";
    public static final String SQLID_COUNTWHERE = "countWhere";
    public static final String SQLID_QUERYALL = "queryAll";
    public static final String SQLID_QUERYLIST = "queryList";

    /**
     * sqlmapNamespace，对应sqlmap.xml中的命名空间
     * 
     * @return
     */
    public String getSqlmapNamespace()
    {
	try
	{
	    Class<T> clas = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	    return clas.getSimpleName();
	}
	catch (Exception ex)
	{
	    System.out.println("命名空间错误...");
	    ex.printStackTrace();
	}

	return "";
    }

    @Override
    public int delete(PK id)
    {
	return this.getSqlSession().delete(getExecutorName(SQLID_DELETE), id);
    }

    @Override
    // todo 删除有问题
    public int delete(PK[] ids)
    {
	int r = 0;
	for (PK id : ids)
	{
	    r += this.getSqlSession().delete(getExecutorName(SQLID_DELETE), id);
	}
	return r;
    }

    @Override
    public int insert(T entity)
    {
	// TODO Auto-generated method stub
	return this.getSqlSession().insert(getExecutorName(SQLID_INSERT), entity);
    }

    @Override
    public T select(PK id)
    {
	// TODO Auto-generated method stub
	return (T) this.getSqlSession().selectOne(getExecutorName(SQLID_SELECT), id);
    }

    @Override
    public int update(T entity)
    {

	return this.getSqlSession().update(getExecutorName(SQLID_UPDATE), entity);
    }

    public String getExecutorName(String str)
    {

	return getSqlmapNamespace() + "." + str + getSqlmapNamespace();
    }

    @Override
    public PageObject query(QueryParamater param)
    {

	// TODO Auto-generated method stub
	RowBounds rb = new RowBounds();
	PageObject po = new PageObject();
	param.setTotalCount(this.count(param.getMap()));
	po.setResults(this.getSqlSession().selectList(getExecutorName(SQLID_QUERY), param.getMap(), new RowBounds(param.getFromIndex(), param.getToIndex())));
	po.setNeverypage(param.getNeverypage());
	po.setTotal(param.getTotalCount());
	return po;
    }

    @Override
    public int count(Map map)
    {
	// TODO Auto-generated method stub
	return (Integer) this.getSqlSession().selectOne(getExecutorName(SQLID_COUNT), map);
    }

    @Override
    public List queryAll()
    {
	// TODO Auto-generated method stub
	return this.getSqlSession().selectList(getExecutorName(SQLID_QUERYALL));
    }

    @Override
	public int deleteAll(String ids) {
		// TODO Auto-generated method stub
		int count = 0;
		if (!Function.isEmpty(ids)) 
		{
			if (ids.indexOf(",") != -1) 
			{
				String[] dels = ids.split(",");
				for (int i = 0; i < dels.length; i++) 
				{
					if (!Function.isEmpty(dels[i]) && !"0".equals(dels[i])) 
					{
						count++;
						this.getSqlSession().delete(getExecutorName(SQLID_DELETE),Integer.parseInt(dels[i]));
					}
				}
			}else {
				count++;
				this.getSqlSession().delete(getExecutorName(SQLID_DELETE),Integer.parseInt(ids));
			}
		}
			
		 
		return count;
    }

	@Override
	public List queryList(Map map) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList(getExecutorName(SQLID_QUERYLIST),map);
	}
    
}
