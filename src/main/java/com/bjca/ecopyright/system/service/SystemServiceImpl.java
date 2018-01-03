package com.bjca.ecopyright.system.service;


import java.util.*;

import com.bjca.ecopyright.system.dao.UserRoleDao;
import com.bjca.ecopyright.system.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bjca.ecopyright.system.dao.AdminDao;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.util.Function;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import org.springframework.transaction.annotation.Transactional;

@Service("systemService")
public class SystemServiceImpl implements SystemService {

	@Autowired
    @Qualifier("adminDao")
	private AdminDao adminDao;

	@Autowired
	private UserRoleDao userRoleDao;
	
	
	
	
	@Override
	public Admin loginByName(String userName) {
		// TODO Auto-generated method stub
		try{
			return adminDao.loginByName(userName);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public boolean saveAdmin(Admin admin) {
		// TODO Auto-generated method stub
		boolean flag=false;
		try{
			if(Function.isEmpty(admin.getID())){

				//获取ID
				admin.setID(UUID.randomUUID().toString());
				//密码加密
				admin.setPassword(Function.MD5(admin.getPassword()));
				admin.setAuthorizationcode(Function.MD5(admin.getAuthorizationcode()));
				 this.adminDao.insert(admin);
				 flag=true;
			 }else{
				//密码加密
				admin.setPassword(Function.MD5(admin.getPassword()));
				admin.setAuthorizationcode(Function.MD5(admin.getAuthorizationcode()));
				this.adminDao.update(admin);
				flag=true;
			 }
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 
		return flag;
	}

	@Override
	@Transactional
	public boolean saveAdminAndRole(Admin admin, String roleIdArray) {
		this.saveAdmin(admin);
		//保存用户角色数据
		if(!Function.isEmpty(roleIdArray)){
			String[] roleIds = roleIdArray.split(",");
			//删除关联关系，然后重新创建
			userRoleDao.deleteUserRoleByAdmin(admin.getID());
			for(int i=0;i<roleIds.length;i++){
				UserRole userRole = new UserRole();
				userRole.setAdminId(admin.getID());
				userRole.setRoleId(roleIds[i]);
				userRole.setCreateDate(new Date());
				userRole.setId(UUID.randomUUID().toString());
				userRoleDao.insert(userRole);
			}
		}
		return true;
	}

	/**
	 * 查询出所有的审核员
	 *
	 * @return
	 */
	@Override
	public List<Admin> queryAdminByRoleName(Map map) {
		List<Admin> admins = new ArrayList<Admin>();
		try {
			admins = this.adminDao.queryAdminByRoleName(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		return admins;
	}


	@Override
	public boolean deleteAadmin(String id) {
		try{
			String[] ids = id.split(",");
			for(int i=0;i<ids.length;i++){
				this.adminDao.delete(ids[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public PageObject queryAdmin(QueryParamater param) {
		try {
			return this.adminDao.query(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Admin queryAdminByID(String adminID) {
		try {
			return this.adminDao.select(adminID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Admin> queryAllAdmin() {
		List<Admin> admins= null;
		try {
			admins = this.adminDao.queryAllAdmin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return admins;
	}

	@Override
	public List<Admin> queryAdminByRole(String string) {
		return this.adminDao.queryAdminByRole(string);
	}


	@Override
	public List<Admin> queryAdminByParam(Map<String, Object> param) {
		return this.adminDao.queryAdminByParam(param);
	}


	@Override
	public Admin searchAdminByType(String type) {
		return this.adminDao.searchAdminByType(type);
	}
}
