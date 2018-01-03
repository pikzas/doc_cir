package com.bjca.framework.page;

import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import com.mysql.jdbc.PreparedStatement;

/**
 * 
 *Administrator 下午03:09:40
 * 
 */
public abstract class BaseTypeHandler implements TypeHandler
{

     public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException
     {
          //System.out.println("-------------------------测试");
          if (parameter == null)
          {

               if (jdbcType == null)
               {

                    try
                    {

                         ps.setNull(i, JdbcType.OTHER.TYPE_CODE);

                    }
                    catch (SQLException e)
                    {
                         //System.out.println("-------------------------测试");
                         throw new TypeException(
                                   "Error setting null parameter.  Most JDBC drivers require that the JdbcType must be specified for all nullable parameters. Cause: "
                                             + e, e);

                    }

               }
               else
               {

                    ps.setNull(i, jdbcType.TYPE_CODE);

               }

          }
          else
          {

               setNonNullParameter(ps, i, parameter, jdbcType); // <-- 发生问题

          }

     }

     public abstract void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)

     throws SQLException;

}
