package com.bjca.framework.page;

import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

import com.mysql.jdbc.PreparedStatement;

/**
 * 
 *Administrator 下午03:12:37
 * 
 */
public abstract class StringTypeHandler extends BaseTypeHandler
{

     public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)

     throws SQLException
     {
          //System.out.println("-------------------------测试");
          ps.setString(i, ((String) parameter));
     }

}
