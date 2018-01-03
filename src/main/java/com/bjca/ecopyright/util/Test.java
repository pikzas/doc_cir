package com.bjca.ecopyright.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by pikzas on 2016/10/11.
 */
public class Test {
    public static void main(String[] args) {
//String tzId = "America/Los_Angeles";
        String tzId = TimeZone.getDefault().getID();
        TimeZone tz = TimeZone.getTimeZone(tzId);
        DateFormat df = DateFormat.getInstance();
        df.setTimeZone(tz);
        String date = df.format(new Date());
        System.out.println(date);

    }
}
