package com.bjca.ecopyright.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyDate
{

     public static String DATE_FORMAT     = "yyyy-MM-dd";
     public static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

     // 获取当天日期
     public static Date get()
     {
          Date date = new Date();

          return date;
     }

     // 日期比较
     public static int compare(Date date1, Date date2)
     {
          return date1.compareTo(date2);
     }

     // 日期加减
     public static String add(String strDate, int day)
     {
          return MyDate.toString(MyDate.add(MyDate.get(strDate), day));
     }

     public static Date add(Date date, int day)
     {
          GregorianCalendar gc = new GregorianCalendar();

          gc.setTime(date);
          gc.add(5, day);
          gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));

          return gc.getTime();
     }

     // 获取日期
     public static Date get(String strDate)
     {
          return MyDate.get(strDate, MyDate.DATE_FORMAT);
     }

     // 获取日期
     public static Date get(String strDate, Date defaultDate)
     {
          Date date = null;

          date = MyDate.get(strDate);

          if (date == null)
          {
               date = defaultDate;
          }

          return date;
     }

     public static Date get(String strDate, String strFormat)
     {
          Date date = null;
          SimpleDateFormat df = new SimpleDateFormat(strFormat);

          try
          {
               date = df.parse(strDate);
          }
          catch (Exception e)
          {
               date = null;
          }

          return date;
     }

     public static Date getDateTime(String strDate)
     {
          return MyDate.get(strDate, MyDate.DATETIME_FORMAT);
     }

     public static Date getDateTime(String strDate, Date defaultDate)
     {
          Date date = null;

          date = MyDate.getDateTime(strDate);

          if (date == null)
          {
               date = defaultDate;
          }

          return date;
     }

     // 输出字符串
     public static String toString(java.sql.Timestamp timestamp)
     {
          return MyDate.toString(new Date(timestamp.getTime()), MyDate.DATE_FORMAT);
     }

     public static String toString(java.sql.Timestamp timestamp, String format)
     {
          return MyDate.toString(new Date(timestamp.getTime()), format);
     }

     public static String toString(Date date)
     {
          return MyDate.toString(date, MyDate.DATE_FORMAT);
     }

     public static String toDateTimeString(Date date)
     {
          return MyDate.toString(date, MyDate.DATETIME_FORMAT);
     }

     public static String toString(Date date, String format)
     {
          String strDate = null;

          if (date != null)
          {
               SimpleDateFormat df = new SimpleDateFormat(format);
               df = new SimpleDateFormat(format);
               strDate = df.format(date);
          }
          else
          {
               strDate = "";
          }

          return strDate;
     }

     public static void main(String args[])
     {
    	 //Date date = MyDate.get("2015-11-11","yyyy-MM-dd");
		 	//System.out.println(date);
//          MyDate.get("2008-07-07 12:00", "yyyy-MM-dd HH:mm");
          String date = "2016-08-31";
          Date nowDate =  MyDate.get(date);
          String now = MyDate.toDateTimeString(MyDate.add(nowDate,1));
          String now1 = MyDate.toString(MyDate.add(nowDate,1));
          System.out.println(now);
          System.out.println(now1);
     }

}
