package com.bjca.ecopyright.system.service;

import com.bjca.ecopyright.system.model.Menu;
import com.bjca.ecopyright.system.model.RoleMenu;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

import java.util.List;

public interface RoleMenuService
{

     /**
      * 添加/更新
      */
     public boolean saveRoleMenu(RoleMenu roleMenu);


     /**
      * 根据ID查询
      */
     public RoleMenu findRoleMenu(String id);

     /**
      * 根据条件查询
      */
     public PageObject queryRoleMenu(QueryParamater param);

     
     /**
      * 删除
      */
     public boolean removeRoleMenu(String id);

    /**
     * 获取指定用户的菜单
     * @param roleID
     * @return
     */
     public List<Menu> getMenuByRole(String roleID);

    /**
     * 更新角色的菜单列表
     * @param roleID
     * @param menuIDs
     * @return
     */
    public boolean updateMenuByRole(String roleID, String menuIDs);



}