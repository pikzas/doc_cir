package com.bjca.ecopyright.tools.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjca.ecopyright.tools.dao.ChinaCalendarDao;
import com.bjca.ecopyright.tools.model.ChinaCalendar;
import com.bjca.ecopyright.util.HttpClientCommon;
import com.bjca.ecopyright.util.JsonUtils;
import com.mysql.fabric.xmlrpc.base.Param;

@Service
public class ChinaCalendarServiceImpl implements ChinaCalendarService {
	@Autowired
	private ChinaCalendarDao chinaCalendarDao;

	@Override
	public boolean saveChinaCalendar() {
		boolean flag = false;
		HttpClientCommon clientCommon = new HttpClientCommon();
		String url = "http://www.fynas.com/workday/count";
		Date date = null;
		String content = null;
		String string = null;
		try {
			date = (new SimpleDateFormat("yyyy-MM-dd")).parse("2015-12-30");// 时间转日期
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();// 日期变成可加减的
		cal.setTime(date);
		// 循环截止的时间
		Date date2 = null;
		try {
			date2 = (new SimpleDateFormat("yyyy-MM-dd")).parse("2016-01-01");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		while (cal.getTime().before(date2)) {
			// 把日期变成字符串
			String datenew = (new SimpleDateFormat("yyyy-MM-dd")).format(cal
					.getTime());
			// 拼接content内容
			content = "end_date=" + datenew + "&start_date=" + datenew;
			try {
				// 得到的是json串
				string = clientCommon.post(url, content,"application/x-www-form-urlencoded");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String weekend = JsonUtils.parseJson(string, "weekend");
			String holiday = JsonUtils.parseJson(string, "holiday");
			String workday = JsonUtils.parseJson(string, "workday");

			ChinaCalendar chinaCalendar = new ChinaCalendar();
			chinaCalendar.setId(UUID.randomUUID().toString());
			try {
				chinaCalendar.setDate((new SimpleDateFormat("yyyy-MM-dd"))
						.parse(datenew));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			chinaCalendar.setIsWeekend(Integer.valueOf(weekend));
			chinaCalendar.setIsHoliday(Integer.valueOf(holiday));
			chinaCalendar.setIsWorkday(Integer.valueOf(workday));
			try {
				this.chinaCalendarDao.insert(chinaCalendar);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 循环体,日期加一天
			cal.add(Calendar.DATE, 1);
			// 睡眠20秒
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	@Override
	public Date queryChinaCalendar(Param param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List queryWorkCalendars(Map map) {
		List calendarList  = null;
		try {
			calendarList = this.chinaCalendarDao.queryList(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendarList;
	}

	@Override
	public Date queryRankWorkday(String urgentDays) {
		Date date = new Date();
		try {
			date = this.chinaCalendarDao.queryRankWorkday(urgentDays);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 查询某一天是否是工作日
	 *
	 * @param date
	 * @return
	 */
	@Override
	public boolean isWorkDay(String date) {
		boolean flag = false;
		try {
			ChinaCalendar chinaCalendar = chinaCalendarDao.queryChinaCalendarByDate(date);
			if(chinaCalendar!=null&&chinaCalendar.getRankWorkday()!=null){
				flag = true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 查询指定工作日 前后指定的几个工作日的具体日期
	 *
	 * @param theDate
	 * @param urgentDays
	 * @return
	 */
	@Override
	public Date queryRankWorkday(String theDate, String urgentDays) {
		Date date = new Date();
		Map<String,String> map = new HashMap<String,String>();
		map.put("theDate",theDate);
		map.put("urgentDays",urgentDays);
		try {
			date = this.chinaCalendarDao.queryThePointedDay(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

}
