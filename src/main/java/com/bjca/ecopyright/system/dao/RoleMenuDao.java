package com.bjca.ecopyright.system.dao;

import com.bjca.ecopyright.system.model.RoleMenu;
import com.bjca.framework.dao.BaseDao;

import java.util.List;
import java.util.Map;

public interface RoleMenuDao extends BaseDao<RoleMenu, String>
{
    /**
     * 通过map获取roleMenu
     * @param map
     * @return
     */
    public List<RoleMenu> getRoleMenuByMap(Map<String, Object> map);

    /**
     * 通过roleID 删除
     * @param roleID
     * @return
     */
    public Integer deleteByRole(String roleID);

    /**
     * 通过menuID 删除
     * @param menuID
     * @return
     */
    public Integer deleteByMenu(String menuID);
}
