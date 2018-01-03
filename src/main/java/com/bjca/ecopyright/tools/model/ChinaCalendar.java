package com.bjca.ecopyright.tools.model;

import java.util.Date;

import com.bjca.framework.bean.BaseBean;

/**
 * 中国日历
 * 
 * @author wangxf
 *
 */
public class ChinaCalendar extends BaseBean{
	private String id ;
	private Date date;
	private Integer isWeekend ;
	private Integer isHoliday;
	private Integer isWorkday;
	private Integer rankWorkday;
	
	public Integer getRankWorkday() {
		return rankWorkday;
	}
	public void setRankWorkday(Integer rankWorkday) {
		this.rankWorkday = rankWorkday;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getIsWeekend() {
		return isWeekend;
	}
	public void setIsWeekend(Integer isWeekend) {
		this.isWeekend = isWeekend;
	}
	public Integer getIsHoliday() {
		return isHoliday;
	}
	public void setIsHoliday(Integer isHoliday) {
		this.isHoliday = isHoliday;
	}
	public Integer getIsWorkday() {
		return isWorkday;
	}
	public void setIsWorkday(Integer isWorkday) {
		this.isWorkday = isWorkday;
	}
	
	
}
