package com.bjca.ecopyright.system.dao.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bjca.ecopyright.system.dao.AdminDao;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.framework.dao.IbatisDaoImp;
@Repository("adminDao")
public class AdminDaoImpl extends IbatisDaoImp<Admin, String> implements AdminDao
{

	@Override
	public Admin loginByName(String userName) {
		// TODO Auto-generated method stub
	 return (Admin)this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".loginByName", userName);
	}


	@Override
	public List<Admin> queryAllAdmin() {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".queryAllAdmins");
	}

	@Override
	public List<Admin> queryAdminByRole(String role) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".queryAdminByRole",role);
	}


	@Override
	public List<Admin> queryAdminByParam(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".queryAdminByParam",map);
	}

	/**
	 * 按条件查询
	 *
	 * @param param
	 * @return
	 */
	@Override
	public List<Admin> queryAdminByRoleName(Map<String, Object> map) {
		return this.getSqlSession().selectList(this.getSqlmapNamespace() + ".queryAdminByRoleName",map);
	}


	@Override
	public Admin searchAdminByType(String type) {
		return (Admin)this.getSqlSession().selectOne(this.getSqlmapNamespace() + ".searchAdminByType", type);
	}

}
