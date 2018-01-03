package com.bjca.framework.page;

/**
 * 
 *Administrator
 *下午05:47:18
 *
 */
import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;

import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonUtils;

@Intercepts( { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class DiclectStatementHandlerInterceptor implements Interceptor
{

     // private static final String DIALECT = "com.afocus.page.MySQLDialect";
     private static final String DIALECT = "com.bjca.framework.page.MySQLDialect";
     

     public Object intercept(Invocation invocation) throws Throwable
     {
          RoutingStatementHandler statement = (RoutingStatementHandler) invocation.getTarget();
          PreparedStatementHandler handler = (PreparedStatementHandler) ReflectUtil.getFieldValue(statement, "delegate");
          RowBounds rowBounds = (RowBounds) ReflectUtil.getFieldValue(handler, "rowBounds");
          /******执行数据库执行日志的操作      开始*********/
//          String operationLogSql = statement.getBoundSql().getSql();
//          String parameterJson= JsonUtils.objectToJsonString(handler.getParameterHandler().getParameterObject());
//          Function.sysExecLog(operationLogSql,parameterJson);
          /******执行数据库执行日志的操作      结束*********/
          if (rowBounds.getLimit() > 0 && rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT)
          {
               BoundSql boundSql = statement.getBoundSql();
               String sql = boundSql.getSql();

               Dialect dialect = (Dialect) Class.forName(DIALECT).newInstance();
               sql = dialect.getLimitString(sql, rowBounds.getOffset(), rowBounds.getLimit());
               System.out.println(sql);
               ReflectUtil.setFieldValue(boundSql, "sql", sql);
          }
          return invocation.proceed();
     }

     public Object plugin(Object target)
     {
          return Plugin.wrap(target, this);
     }

     public void setProperties(Properties properties)
     {
     }
     
}
