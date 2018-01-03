package com.bjca.framework.page;

/**
 * 
 *Administrator 下午07:27:32 修改分页
 */
public class OracleDialect implements Dialect
{

     public String getLimitString(String sql, int offset, int limit)
     {
          // sql = StringUtils.trim(StringUtils.upperCase(sql));
          StringBuffer sqlbuf = new StringBuffer();

          sqlbuf.append("select * from (select t.*,rownum xrownum from (");
          sqlbuf.append(sql);
          sqlbuf.append(" ) t where rownum <= ").append(limit);
          sqlbuf.append(" ) where xrownum > ").append(offset);
          return sqlbuf.toString();
     }
}
