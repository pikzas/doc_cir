package com.bjca.ecopyright.system.dao;

import com.bjca.ecopyright.system.model.UserRole;
import com.bjca.framework.dao.BaseDao;

public interface UserRoleDao extends BaseDao<UserRole, String>
{
	/**
	 * 根据用户id删除所属角色
	 * @param adminId
	 * @return
	 */
	int deleteUserRoleByAdmin(String adminId);
}
