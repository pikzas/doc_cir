package com.bjca.ecopyright.system.dao.imp;

import com.bjca.ecopyright.system.dao.UserRoleDao;
import com.bjca.ecopyright.system.model.UserRole;
import com.bjca.framework.dao.IbatisDaoImp;
import org.springframework.stereotype.Repository;

@Repository("userRoleDao")
public class UserRoleDaoImpl extends IbatisDaoImp<UserRole, String> implements UserRoleDao
{
	@Override
	public int deleteUserRoleByAdmin(String adminId) {
		return this.getSqlSession().delete(this.getSqlmapNamespace() + ".deleteUserRoleByAdmin", adminId);
	}
}
