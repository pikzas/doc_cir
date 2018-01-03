package com.bjca.ecopyright.system.dao;

import com.bjca.ecopyright.system.model.Menu;
import com.bjca.framework.dao.BaseDao;

import java.util.List;
import java.util.Map;

public interface MenuDao extends BaseDao<Menu, String>
{
    public Integer deleteAll();

    List<Menu> queryMenuList(Map<String,Object> map);

    Menu queryMenuListById(String id);

}
