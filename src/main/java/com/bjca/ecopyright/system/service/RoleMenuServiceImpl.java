package com.bjca.ecopyright.system.service;

import com.bjca.ecopyright.system.dao.MenuDao;
import com.bjca.ecopyright.system.dao.RoleMenuDao;
import com.bjca.ecopyright.system.model.Menu;
import com.bjca.ecopyright.system.model.RoleMenu;
import com.bjca.ecopyright.util.Function;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("roleMenuService")
public class RoleMenuServiceImpl implements RoleMenuService
{


	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private MenuDao menuDao;
     
     /**
      * 添加/更新
      */
     @Override
     public boolean saveRoleMenu(RoleMenu roleMenu){
     
        boolean flag = false;
		try {
			if (Function.isEmpty(roleMenu.getId())) {
				roleMenu.setId(UUID.randomUUID().toString());
				roleMenu.setCreateDate(new Date());
				roleMenu.setUpdateDate(new Date());
				roleMenuDao.insert(roleMenu);
				flag = true;
			} else {
				roleMenu.setCreateDate(new Date());
				roleMenu.setUpdateDate(new Date());
				roleMenuDao.update(roleMenu);
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
     public RoleMenu findRoleMenu(String id){
		RoleMenu roleMenu = null;
		try {
			roleMenu = this.roleMenuDao.select(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleMenu;

     }

     /**
      * 根据条件查询
      */
     @Override
     public PageObject queryRoleMenu(QueryParamater param){
		
		PageObject po = null;
		try {
			po = this.roleMenuDao.query(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return po; 
		    	
     }

     
     /**
      * 删除
      */
     @Override
     public boolean removeRoleMenu(String id){
     	boolean flag = false;
		try {
			this.roleMenuDao.delete(id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
     }

	/**
	 * 获取指定用户的菜单
	 *
	 * @param roleID
	 * @return
	 */
	@Override
	public List<Menu> getMenuByRole(String roleID) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Menu> menuList = new ArrayList<Menu>();
		try {
			map.put("roleId",roleID);
			List<RoleMenu> roleMenuList = roleMenuDao.getRoleMenuByMap(map);
			for (RoleMenu roleMenu:roleMenuList) {
				Menu menu = menuDao.select(roleMenu.getMenuId());
				if(menu!=null){
					menuList.add(menu);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return menuList;
	}

	/**
	 * 更新角色的菜单列表
	 *
	 * @param roleID
	 * @param menuIDs
	 * @return
	 */
	@Override
	public boolean updateMenuByRole(String roleID, String menuIDs) {
		boolean flag = false;
		try {
			//删除当前用户所有的菜单权限
			roleMenuDao.deleteByRole(roleID);
			if (menuIDs.indexOf(",") != -1) {
				String[] idArray = menuIDs.split(",");
				for (String menuID : idArray) {
					RoleMenu roleMenu = new RoleMenu();
					roleMenu.setRoleId(roleID);
					roleMenu.setMenuId(menuID);
					this.saveRoleMenu(roleMenu);
				}
			}else {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleId(roleID);
				roleMenu.setMenuId(menuIDs);
				this.saveRoleMenu(roleMenu);
			}
			flag = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 将查出的关系表 转换得到MenuTree
	 * @param roleMenuList
	 * @return
     *//*
	private JSONArray handler(List<RoleMenu> roleMenuList){
		JSONArray jsonArray = null;
		List<Menu> menuTree = new ArrayList<Menu>();
		if(roleMenuList==null||roleMenuList.size()==0){
			return jsonArray;
		}
		try {
			//1  查出所有的菜单
			Map<String, Object> map = new HashMap<>();
			map.put("level", "0");
			List<Menu> allMenus = menuDao.queryList(map);
			//1  查出该角色所有的菜单
			for (RoleMenu roleMenu:roleMenuList) {
				Menu menu = menuDao.select(roleMenu.getMenuId());
				//判定  TODO
				if(menu!=null&& allMenus.contains(menu)){

				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return jsonArray;
	}*/


}