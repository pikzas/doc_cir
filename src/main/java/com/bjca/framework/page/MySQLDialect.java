package com.bjca.framework.page;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *Administrator 下午07:15:13
 * 
 */
public class MySQLDialect implements Dialect
{

     public String getLimitString(String sql, int offset, int limit)
     {
          sql = StringUtils.trim(StringUtils.lowerCase(sql));
          StringBuffer sqlbuf = new StringBuffer();
          sqlbuf.append(sql);
          sqlbuf.append(" limit ");
          sqlbuf.append(offset);
          sqlbuf.append(",");
          sqlbuf.append(limit);
          return sqlbuf.toString(); // To change body of implemented methods use File | Settings | File Templates.
     }
}
