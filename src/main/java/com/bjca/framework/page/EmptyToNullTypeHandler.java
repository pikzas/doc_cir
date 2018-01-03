package com.bjca.framework.page;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

import com.mysql.jdbc.PreparedStatement;

/**
 * 
 *Administrator 下午03:14:58
 * 
 */
public class EmptyToNullTypeHandler extends StringTypeHandler
{

     public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException
     {
          //System.out.println("-------------------------测试");
          if ("".equals(parameter))
          {

               parameter = null;
          }
          ps.setString(i, ((String) parameter));

     }

     @Override
     public Object getResult(ResultSet rs, String columnName) throws SQLException
     {
          //System.out.println("-------------------------测试");
          // TODO Auto-generated method stub
          return rs.getString(columnName);
     }

     @Override
     public Object getResult(CallableStatement cs, int columnIndex) throws SQLException
     {
          //System.out.println("-------------------------测试");
          // TODO Auto-generated method stub
          return cs.getString(columnIndex);
     }

     @Override
     public void setParameter(java.sql.PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException
     {
          // TODO Auto-generated method stub
          ps.setString(i, (String) parameter);

     }

	@Override
	public Object getResult(ResultSet arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


}
