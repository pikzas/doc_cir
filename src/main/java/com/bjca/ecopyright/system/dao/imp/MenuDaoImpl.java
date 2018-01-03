package com.bjca.ecopyright.system.dao.imp;

import com.bjca.ecopyright.system.dao.MenuDao;
import com.bjca.ecopyright.system.model.Menu;
import com.bjca.framework.dao.IbatisDaoImp;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("menuDao")
public class MenuDaoImpl extends IbatisDaoImp<Menu, String> implements MenuDao
{
    @Override
    public Integer deleteAll() {
        return super.getSqlSession().delete(getExecutorName("deleteAll"));
    }

    @Override
    public List<Menu> queryMenuList(Map<String, Object> map) {
        return this.getSqlSession().selectList(this.getSqlmapNamespace()+".queryMenuList",map);
    }

    @Override
    public Menu queryMenuListById(String id) {
        return this.getSqlSession().selectOne(this.getSqlmapNamespace()+".queryMenuListById",id);
    }

}
