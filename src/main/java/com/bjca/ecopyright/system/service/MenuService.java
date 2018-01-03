package com.bjca.ecopyright.system.service;

import com.bjca.ecopyright.system.model.Menu;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

import java.util.List;

public interface MenuService {

	/**
	 * 添加/更新s
	 */
	public boolean saveMenu(Menu menu);

	/**
	 * 根据ID查询
	 */
	public Menu findMenu(String id);


	/**
	 * 删除
	 */
	public boolean removeMenu(String id);
	
	/**
	 * 查询树状结构的菜单
	 * @return
	 */
	String queryMenuForTree();


	/**
	 * 更新菜单数据
	 * 首先查出所有原有的数据 然后和当前数据进行比较
	 * 有则更新
	 * 无则插入
	 * 丢失删除
	 *
	 * @param menus
	 * @return
     */
	public boolean updateMenu(List<Menu> menus);

}