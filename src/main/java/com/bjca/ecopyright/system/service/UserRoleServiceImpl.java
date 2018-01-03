package com.bjca.ecopyright.system.service;

import com.bjca.ecopyright.system.dao.UserRoleDao;
import com.bjca.ecopyright.system.model.UserRole;
import com.bjca.ecopyright.util.Function;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService
{


	 @Autowired
 	 private UserRoleDao userRoleDao;

     
     /**
      * 添加/更新
      */
     @Override
     public boolean saveUserRole(UserRole userRole){
     
        boolean flag = false;
		try {
			if (Function.isEmpty(userRole.getId())) {
				userRole.setId(UUID.randomUUID().toString());
				userRoleDao.insert(userRole);
				flag = true;
			} else {
				userRoleDao.update(userRole);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
     }

     /**
      * 根据ID查询
      */
     @Override
     public UserRole findUserRole(String id){
		UserRole userRole = null;
		try {
			userRole = this.userRoleDao.select(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userRole;

     }

     /**
      * 根据条件查询
      */
     @Override
     public PageObject queryUserRole(QueryParamater param){
		
		PageObject po = null;
		try {
			po = this.userRoleDao.query(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po; 
		    	
     }

     
     /**
      * 删除
      */
     @Override
     public boolean removeUserRole(String id){
     	boolean flag = false;
		try {
			this.userRoleDao.delete(id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
     }

    

}