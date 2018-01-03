package com.bjca.ecopyright.tools.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mysql.fabric.xmlrpc.base.Param;

public interface ChinaCalendarService {
	
	/**
	 * 添加日历信息
	 */
	public boolean saveChinaCalendar();
	
	/**
	 * 根据传入的日期查询日历
	 */
	public Date queryChinaCalendar(Param param);
	/**
	 * 根据传入的日期查询日历
	 */
	public List queryWorkCalendars(Map map);
	/**
	 * 根据传入的剩余天数查询到期日期
	 */
	public Date queryRankWorkday(String urgentDays);

	/**
	 * 查询某一天是否是工作日
	 * @param date
	 * @return
     */
	public boolean isWorkDay(String date);

	/**
	 * 查询指定工作日 前后指定的几个工作日的具体日期
	 * @param theDate
	 * @param urgentDays
     * @return
     */
	public Date queryRankWorkday(String theDate,String urgentDays);

}
