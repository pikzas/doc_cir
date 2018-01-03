package com.bjca.ecopyright.system.dao.imp;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.bjca.ecopyright.system.dao.OperationLogDao;
import com.bjca.ecopyright.system.model.OperationLog;
import com.bjca.framework.dao.IbatisDaoImp;
@Repository("operationLogDao")
public class OperationLogDaoImpl extends IbatisDaoImp<OperationLog, String> implements OperationLogDao
{


}
