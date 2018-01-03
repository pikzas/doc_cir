package com.bjca.ecopyright.system.dao.imp;

import com.bjca.ecopyright.system.dao.RoleMenuDao;
import com.bjca.ecopyright.system.model.RoleMenu;
import com.bjca.framework.dao.IbatisDaoImp;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("roleMenuDao")
public class RoleMenuDaoImpl extends IbatisDaoImp<RoleMenu, String> implements RoleMenuDao
{

    /**
     * 通过map获取roleMenu
     *
     * @param map
     * @return
     */
    @Override
    public List<RoleMenu> getRoleMenuByMap(Map<String, Object> map) {
        return this.getSqlSession().selectList(this.getSqlmapNamespace()+".getRoleMenuByMap",map);
    }

    /**
     * 通过roleID删除roleMenu
     *
     * @param roleID
     * @return
     */
    @Override
    public Integer deleteByRole(String roleID) {
        return this.getSqlSession().delete(this.getSqlmapNamespace()+".deleteByRole",roleID);
    }

    /**
     * 通过menuID 删除
     *
     * @param menuID
     * @return
     */
    @Override
    public Integer deleteByMenu(String menuID) {
        return this.getSqlSession().delete(this.getSqlmapNamespace()+".deleteByMenu",menuID);
    }
}
