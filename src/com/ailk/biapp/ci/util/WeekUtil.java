package com.ailk.biapp.ci.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

public class WeekUtil {
    private static Logger log = Logger.getLogger(WeekUtil.class);
    private static List<String> weekendIsWeekdayList = new ArrayList();
    private static List<String> weekdayIsHolidayList = new ArrayList();

    public WeekUtil() {
    }

    public static boolean isWeekDay(String dateStr, String formatStr) {
        boolean isWeekDay = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        Date date = null;

        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException var6) {
            var6.printStackTrace();
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if(calendar.get(7) != 7 && calendar.get(7) != 1) {
            if(weekdayIsHolidayList.contains(dateStr)) {
                isWeekDay = false;
            } else {
                isWeekDay = true;
            }
        } else if(weekendIsWeekdayList.contains(dateStr)) {
            isWeekDay = true;
        } else {
            isWeekDay = false;
        }

        return isWeekDay;
    }

    public static boolean isWeekend(String dateStr, String formatStr) {
        boolean isWeekDay = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        Date date = null;

        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException var7) {
            var7.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(7) - 1;
        if(week == 0 || week == 6) {
            isWeekDay = true;
        }

        return isWeekDay;
    }

    static {
        SAXReader reader = new SAXReader();

        try {
            File e = ResourceUtils.getFile("classpath:config/aibi_ci/weekDay.xml");
            Document doc = reader.read(e);
            Element root = doc.getRootElement();
            Element weekdayElement = root.element("weekday");
            Element holidayListElement = weekdayElement.element("holiday_list");
            Element holidayValueElement = null;
            Iterator weekendElement = holidayListElement.elementIterator("date");

            while(weekendElement.hasNext()) {
                holidayValueElement = (Element)weekendElement.next();
                weekdayIsHolidayList.add(holidayValueElement.getText());
            }

            Element weekendElement1 = root.element("weekend");
            Element weekdayListElement = weekendElement1.element("weekday_list");
            Element weekdayValueElement = null;
            Iterator i = weekdayListElement.elementIterator("date");

            while(i.hasNext()) {
                weekdayValueElement = (Element)i.next();
                weekendIsWeekdayList.add(weekdayValueElement.getText());
            }
        } catch (Exception var11) {
            log.error("error", var11);
        }

    }
}
