package com.bjca.ecopyright.system.service;

import com.bjca.ecopyright.system.model.Role;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

import java.util.List;

public interface RoleService
{

     /**
      * 添加/更新
      */
     public boolean saveRole(Role role);


     /**
      * 根据ID查询
      */
     public Role findRole(String id);

     /**
      * 根据条件查询
      */
     public PageObject queryRole(QueryParamater param);

     
     /**
      * 删除
      */
     public boolean removeRole(String id);


    /**
     * 查出所有
     */
     public List<Role> selectAll();


}