package com.bjca.ecopyright.system.service;

import org.apache.ibatis.executor.parameter.ParameterHandler;

import com.bjca.ecopyright.system.model.OperationLog;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;



public interface OperationLogService {
	
	/**
	 * 操作日志处理
	 * @date 		2016-5-4上午10:31:46
	 * @mail 		humin@bjca.org.cn
	 * @author 		humin
	 * @param operationLogSql
	 * @param Parameter
	 */
	public void batchProcess(String operationLogSql,String parameterJson);
	
	/**
	 * 保存及更新
	 * @date 		2016-5-4上午10:32:23
	 * @mail 		humin@bjca.org.cn
	 * @author 		humin
	 * @param operationLog
	 * @return
	 */
	public boolean saveOperationLog(OperationLog operationLog);

	/**
	 * 根据条件查询
	 * @date 		2016-5-4上午10:32:11
	 * @mail 		humin@bjca.org.cn
	 * @author 		humin
	 * @param param
	 * @return
	 */
    public PageObject queryOperationLog(QueryParamater param);
    
}
