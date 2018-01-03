package com.bjca.framework.dao;

import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;



public class IbatisDaoImp<T, PK extends Serializable> extends SqlSessionDaoSupport implements BaseDao<T, PK>
//public class IbatisDaoImp<T, PK extends Serializable> extends SqlSessionDaoSupport
{

     // sqlmap.xml定义文件中对应的sqlid
     public static final String SQLID_INSERT     = "insert";
     public static final String SQLID_UPDATE     = "update";
     public static final String SQLID_DELETE     = "delete";
     public static final String SQLID_SELECT     = "select";
     public static final String SQLID_SELECT_PK  = "selectPk";
     public static final String SQLID_SELECT_FK  = "selectFk";
     public static final String SQLID_COUNT      = "count";
     public static final String SQLID_QUERY      = "query";
     public static final String SQLID_COUNTWHERE = "countWhere";
     public static final String SQLID_QUERYALL   = "queryAll";
     public static final String SQLID_QUERYLIST = "queryList";
     
     @Resource
     public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
	         super.setSqlSessionFactory(sqlSessionFactory);
	    }
     
     
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

     
     public int delete(PK id)
     {
          return this.getSqlSession().delete(getExecutorName(SQLID_DELETE), id);
     }

     
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


     public PK insert(T entity)
     {
          // TODO Auto-generated method stub
        //  return (PK)this.getSqlSession().insert(getExecutorName(SQLID_INSERT), entity);
    	  this.getSqlSession().insert(getExecutorName(SQLID_INSERT), entity);
         return null;
     }

     
     public T select(PK id)
     {
          // TODO Auto-generated method stub
          return (T) this.getSqlSession().selectOne(getExecutorName(SQLID_SELECT), id);
     }

     
     public int update(T entity)
     {

          return this.getSqlSession().update(getExecutorName(SQLID_UPDATE), entity);
     }

     public String getExecutorName(String str)
     {

          return getSqlmapNamespace() + "." + str + getSqlmapNamespace();
     }

     
     public PageObject query(QueryParamater param)
     {

          // TODO Auto-generated method stub
          RowBounds rb = new RowBounds();
          PageObject po = new PageObject();
          param.setTotalCount(this.count(param.getMap()));
          po.setCurrentPage(param.getCurrentPage());//TODO 设置当前页
          po.setResults(this.getSqlSession().selectList(getExecutorName(SQLID_QUERY), param.getMap(),
                    new RowBounds(param.getFromIndex(), param.getNeverypage())));
          po.setNeverypage(param.getNeverypage());
          po.setTotal(param.getTotalCount());
          return po;
     }

     
     public int count(Map map)
     {
          // TODO Auto-generated method stub
          return (Integer) this.getSqlSession().selectOne(getExecutorName(SQLID_COUNT), map);
     }

     
     public List queryAll()
     {
          // TODO Auto-generated method stub
          return this.getSqlSession().selectList(getExecutorName(SQLID_QUERYALL));
     }


	@Override
	public void deleteObject(T model) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void merge(T model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<T> queryList(Map map) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList(getExecutorName(SQLID_QUERYLIST),map);
	}
    
	
    
}
