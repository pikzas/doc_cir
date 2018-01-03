package com.bjca.ecopyright.tools.dao;

import java.util.Date;
import java.util.Map;

import com.bjca.ecopyright.tools.model.ChinaCalendar;
import com.bjca.framework.dao.BaseDao;

public interface ChinaCalendarDao extends BaseDao<ChinaCalendar,String>{
	/**
	 * 查询到期时间
	 * @return
	 */
	public Date queryRankWorkday(String urgentDays);

	/**
	 * 查询某一个指定的工作日的 前后指定的几个工作日是那一天
	 * @param map
	 * @return
     */
	public Date queryThePointedDay(Map map);

	/**
	 * 查询今天日期
	 * @return
	 * @author bxt-chen
	 */
	public ChinaCalendar queryChinaCalendarByDate(String date);
}
