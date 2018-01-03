package com.bjca.ecopyright.system.service;

import com.bjca.ecopyright.system.dao.RoleDao;
import com.bjca.ecopyright.system.model.Role;
import com.bjca.ecopyright.util.Function;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("roleService")
public class RoleServiceImpl implements RoleService
{


	 @Autowired
 	 private RoleDao roleDao;

     
     /**
      * 添加/更新
      */
     @Override
     public boolean saveRole(Role role){
     
        boolean flag = false;
		try {
			if (Function.isEmpty(role.getId())) {
				role.setId(UUID.randomUUID().toString());
				roleDao.insert(role);
				flag = true;
			} else {
				roleDao.update(role);
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
     public Role findRole(String id){
		Role role = null;
		try {
			role = this.roleDao.select(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role;

     }

     /**
      * 根据条件查询
      */
     @Override
     public PageObject queryRole(QueryParamater param){
		
		PageObject po = null;
		try {
			po = this.roleDao.query(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po; 
		    	
     }

     
     /**
      * 删除
      */
     @Override
     public boolean removeRole(String id){
     	boolean flag = false;
		try {
			this.roleDao.delete(id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
     }

	/**
	 * 查出所有
	 */
	@Override
	public List<Role> selectAll() {
		List<Role> roleList= new ArrayList<Role>();
		Map map = new HashMap<>();
		try {
			roleList = roleDao.queryList(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		return roleList;
	}


}