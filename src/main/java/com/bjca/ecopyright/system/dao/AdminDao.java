package com.bjca.ecopyright.system.dao;

import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.system.model.Admin;
import com.bjca.framework.dao.BaseDao;

public interface AdminDao extends BaseDao<Admin, String>
{
	/**
	 * 系统管理员登录
	 * @param userName
	 * @return
	 */
	public Admin loginByName(String userName);
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<Admin> queryAllAdmin() ;

	/**
	 * 根据角色查询
	 * @param string
	 * @return
	 */
	public List<Admin> queryAdminByRole(String role);
	/**
	 * 按条件查询
	 * @param param
	 * @return
	 */
	public List<Admin> queryAdminByParam(Map<String, Object> param);

	/**
	 * 按条件查询
	 * @param param
	 * @return
	 */
	public List<Admin> queryAdminByRoleName(Map<String, Object> param);
	
	
	/**
	 * 查找雍和操作员
	 * @date 2016-12-14上午11:48:21
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param type
	 * @return
	 */
	public Admin searchAdminByType(String type);

}
