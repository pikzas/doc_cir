package com.bjca.ecopyright.soft.service;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareOperationLog;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

public interface SoftwareOperationLogService
{

     /**
      * 添加/更新
      */
     public boolean saveSoftwareOperationLog(SoftwareOperationLog softwareOperationLog);

     /**
      * 根据ID查询
      */
     public SoftwareOperationLog findSoftwareOperationLog(String id);

     /**
      * 根据条件查询
      */
     public PageObject querySoftwareOperationLog(QueryParamater param);

     /**
      * 删除
      */
     public boolean removeSoftwareOperationLog(String id);

    /**
     * 保存日志
     * @param software 软件
     * @param type  日志类型
     * @return
     */
     boolean saveLog(Software software,int type,Admin admin);

}