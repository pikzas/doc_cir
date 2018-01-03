package com.bjca.ecopyright.system.dao.imp;

import com.bjca.framework.dao.IbatisDaoImp;
import com.bjca.ecopyright.system.dao.RoleDao;
import com.bjca.ecopyright.system.model.Role;
import org.springframework.stereotype.Repository;
@Repository("roleDao")
public class RoleDaoImpl extends IbatisDaoImp<Role, String> implements RoleDao
{

}
