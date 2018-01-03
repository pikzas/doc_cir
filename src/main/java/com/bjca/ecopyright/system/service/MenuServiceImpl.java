package com.bjca.ecopyright.system.service;

import com.bjca.ecopyright.system.dao.MenuDao;
import com.bjca.ecopyright.system.dao.RoleMenuDao;
import com.bjca.ecopyright.system.model.Menu;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonUtils;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;
	@Autowired
	private RoleMenuDao roleMenuDao;

	/**
	 * 添加/更新
	 */
	@Override
	@Transactional
	public boolean saveMenu(Menu menu) {
		boolean flag = false;
		try {
			if(Function.isEmpty(menu.getId())){
				menu.setId(UUID.randomUUID().toString());
				menu.setUpdateDate(new Date());
				menu.setCreateDate(new Date());
				menuDao.insert(menu);
			}else{
				menu.setUpdateDate(new Date());
				menuDao.update(menu);
			}
			flag = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return flag;

	}

	/**
	 * 根据ID查询
	 */
	@Override
	public Menu findMenu(String id) {
		Menu menu = null;
		try {
			menu = this.menuDao.select(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menu;

	}


	/**
	 * 删除
	 */
	@Override
	public boolean removeMenu(String id) {
		boolean flag = false;
		try {
			this.menuDao.delete(id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	@Override
	public String queryMenuForTree() {
		JSONArray array = new JSONArray();
		//先取出跟节点的数据
		Map<String, Object> map = new HashMap<>();
		map.put("level", "0");
		List<Menu> items = menuDao.queryMenuList(map);
		Menu result = new Menu();
		//如果是初始加载 items 为null 初始化一个根菜单
		if(Function.isEmpty(items)){
			result.setId(UUID.randomUUID().toString());
			result.setName("系统菜单");
			result.setDescription("根菜单");
			result.setLevel(0);
			result.setCreateDate(new Date());
			menuDao.insert(result);
		}
		for (Menu menu : items) {
			//拿出根节点
			if(menu.getLevel()==0){
				result=menu;
				List<Menu> list1 = new ArrayList<Menu>();
				for (Menu menu1 : items) {
					//拿出一级菜单
					if(menu1.getLevel()==1){
						for (Menu menu2 : items) {
							//拿出二级菜单
							if(menu2.getpId()==menu1.getId()){
								if(menu1.getChildren()==null){
									List<Menu> childs = new ArrayList<Menu>();
									childs.add(menu2);
									menu1.setChildren(childs);
								}else{
									menu1.getChildren().add(menu2);
								}
							}
						}
						list1.add(menu1);
					}
				}
				//将一级菜单加入根菜单
				result.setChildren(list1);
			}
		}
		return JsonUtils.objectToJsonString(result);
	}

	/**
	 * 更新菜单数据
	 * 首先查出所有原有的数据 然后和当前数据进行比较
	 * 有则更新
	 * 无则插入
	 * 丢失删除 同时处理关系表
	 *
	 * @param newMenus
	 * @return
	 */
	@Override
	public boolean updateMenu(List<Menu> newMenus) {
		boolean flag = false;
		List<Menu> existMenus = new ArrayList<Menu>();
		try{
			existMenus = menuDao.queryAll();
			//遍历新的菜单 无则插入 有则更新 然后移除已存在列表
			for (Menu menu:newMenus) {
				Menu ret = this.findMenu(menu.getId());
				if(ret==null){
					menu.setCreateDate(new Date());
					menu.setUpdateDate(new Date());
					this.menuDao.insert(menu);
				}else{
					menu.setUpdateDate(new Date());
					this.menuDao.update(menu);
					existMenus.remove(ret);
				}
			}

			//该删除菜单数据  主要是删除角色菜单关联表
			for (Menu menu:existMenus) {
				roleMenuDao.deleteByMenu(menu.getId());
				menuDao.delete(menu.getId());
			}
			flag = true;

		}catch (Exception e){
			e.printStackTrace();
		}
		return flag;
	}


}