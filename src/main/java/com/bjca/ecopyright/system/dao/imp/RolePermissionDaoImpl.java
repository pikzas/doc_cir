package com.bjca.ecopyright.system.dao.imp;

import com.bjca.framework.dao.IbatisDaoImp;
import com.bjca.ecopyright.system.dao.RolePermissionDao;
import com.bjca.ecopyright.system.model.RolePermission;
import org.springframework.stereotype.Repository;
@Repository("rolePermissionDao")
public class RolePermissionDaoImpl extends IbatisDaoImp<RolePermission, String> implements RolePermissionDao
{

}
