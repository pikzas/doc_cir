package com.bjca.ecopyright.system.service;

import com.bjca.ecopyright.system.model.UserRole;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

public interface UserRoleService
{

     /**
      * 添加/更新
      */
     public boolean saveUserRole(UserRole userRole);


     /**
      * 根据ID查询
      */
     public UserRole findUserRole(String id);

     /**
      * 根据条件查询
      */
     public PageObject queryUserRole(QueryParamater param);

     
     /**
      * 删除
      */
     public boolean removeUserRole(String id);

    

}