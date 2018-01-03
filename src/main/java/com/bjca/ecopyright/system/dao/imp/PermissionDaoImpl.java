package com.bjca.ecopyright.system.dao.imp;

import com.bjca.framework.dao.IbatisDaoImp;
import com.bjca.ecopyright.system.dao.PermissionDao;
import com.bjca.ecopyright.system.model.Permission;
import org.springframework.stereotype.Repository;
@Repository("permissionDao")
public class PermissionDaoImpl extends IbatisDaoImp<Permission, String> implements PermissionDao
{

}
