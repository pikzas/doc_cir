package com.bjca.ecopyright.tools.dao.imp;

import org.springframework.stereotype.Repository;

import com.bjca.ecopyright.tools.dao.ServiceFailDao;
import com.bjca.ecopyright.tools.model.ServiceFail;
import com.bjca.framework.dao.IbatisDaoImp;

@Repository("serviceFailDao")
public class ServiceFailDaoImpl extends IbatisDaoImp<ServiceFail, String> implements ServiceFailDao {

}
