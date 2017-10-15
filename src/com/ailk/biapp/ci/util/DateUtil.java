package com.ailk.biapp.ci.util;

import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.string.StringUtil;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DateUtil {
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "HH:mm yyyy-MM-dd";
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String FORMAT_YYYY_MM = "yyyy-MM";
	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String FORMAT_YYYYMM = "yyyyMM";
	public static final int DATETYPE_MONTH = 0;
	public static final int DATETYPE_DAY = 1;
	private static String TABLE_LABEL_STAT = "CI_LABEL_STAT_";
	public static final int UPDATE_CYCLE_DAY = 3;
	public static final int UPDATE_CYCLE_MONTH = 2;

	public DateUtil() {
	}

	public static String string3String(String date) {
		String s = "";
		if(date.indexOf(".") > 0) {
			int pos = date.indexOf(".");
			s = date.substring(0, pos);
		}

		return s;
	}

	public static String string2String2(String date) {
		String s = date;
		if(date != null && !date.equals("")) {
			String result1 = null;

			try {
				SimpleDateFormat e = null;
				if(s.length() > 15) {
					e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				} else if(s.length() > 8) {
					e = new SimpleDateFormat("yyyy-MM-dd");
				} else if(s.length() > 4) {
					e = new SimpleDateFormat("yyyy-MM");
				} else {
					e = new SimpleDateFormat("yyyy");
				}

				result1 = date2String(e.parse(s), "yyyy-MM-dd");
			} catch (Exception var4) {
				var4.printStackTrace();
			}

			return result1;
		} else {
			return null;
		}
	}

	public static String string2StringFormat(String dateStr, String dateStrFormat, String afterFormat) {
		if(dateStr != null && !"".equals(dateStr)) {
			String result = null;

			try {
				SimpleDateFormat e = new SimpleDateFormat(dateStrFormat);
				result = date2String(e.parse(dateStr), afterFormat);
			} catch (ParseException var5) {
				var5.printStackTrace();
			}

			return result;
		} else {
			return null;
		}
	}

	public static String string2String(String date) {
		String s = "";
		if(date != null && !date.equals("")) {
			if(date.indexOf(".") > 0) {
				int result = date.indexOf(".");
				s = date.substring(0, result);
			}

			if(s != null && !s.equals("")) {
				String result1 = null;

				try {
					SimpleDateFormat e = null;
					if(s.length() > 15) {
						e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					} else if(s.length() > 8) {
						e = new SimpleDateFormat("yyyy-MM-dd");
					} else if(s.length() > 4) {
						e = new SimpleDateFormat("yyyy-MM");
					} else {
						e = new SimpleDateFormat("yyyy");
					}

					result1 = date2String(e.parse(s), "HH:mm yyyy-MM-dd");
				} catch (Exception var4) {
					var4.printStackTrace();
				}

				return result1;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String date2String(Date date) {
		if(date == null) {
			return "";
		} else {
			String result = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result = dateFormat.format(date);
			return result;
		}
	}

	public static String date2String(Date date, String formatPra) {
		String format = formatPra;
		if(date == null) {
			return "";
		} else {
			if(formatPra == null || formatPra.equals("")) {
				format = "yyyy-MM-dd HH:mm:ss";
			}

			String result = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			result = dateFormat.format(date);
			return result;
		}
	}

	public static String timeStamp2String(Timestamp date) {
		if(date == null) {
			return "";
		} else {
			String result = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			result = dateFormat.format(date);
			return result;
		}
	}

	public static String timeStamp2String(Timestamp date, String format) {
		if(date == null) {
			return "";
		} else {
			String result = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			result = dateFormat.format(date);
			return result;
		}
	}

	public static Date string2Date(String dateStr, String formatStr) {
		if(dateStr == null) {
			return null;
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
			Date date = null;

			try {
				date = dateFormat.parse(dateStr);
			} catch (ParseException var5) {
				var5.printStackTrace();
			}

			return date;
		}
	}

	public static Timestamp string2Timestamp(String sdate) {
		if(sdate == null) {
			return null;
		} else {
			Timestamp ts = null;

			try {
				ts = Timestamp.valueOf(sdate);
			} catch (Exception var3) {
				;
			}

			return ts;
		}
	}

	public static List<String> getAssignFrontDate(int frontNum, String dataDate, int updateCycle) {
		LinkedList list = new LinkedList();
		String dateStr = "";
		String formatStr = "";
		byte dateType = -1;
		if(2 == updateCycle) {
			formatStr = "yyyyMM";
			dateType = 2;
		} else if(1 == updateCycle) {
			formatStr = "yyyyMMdd";
			dateType = 6;
		}

		SimpleDateFormat format = new SimpleDateFormat(formatStr);

		try {
			Date e = format.parse(dataDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e);
			if(!StringUtil.isEmpty(dataDate)) {
				for(int i = 0; i < frontNum; ++i) {
					dateStr = format.format(calendar.getTime());
					list.add(dateStr);
					calendar.add(dateType, -1);
				}
			}
		} catch (ParseException var11) {
			var11.printStackTrace();
		}

		return list;
	}

	public static List<String> getAssignFrontDate2(String beginDate, String endDate, int updateCycle) {
		LinkedList list = new LinkedList();
		String dateStr = "";
		String formatStr = "";
		byte dateType = -1;
		if(2 == updateCycle) {
			formatStr = "yyyyMM";
			dateType = 2;
		} else if(1 == updateCycle) {
			formatStr = "yyyyMMdd";
			dateType = 6;
		}

		SimpleDateFormat format = new SimpleDateFormat(formatStr);

		try {
			Date e = format.parse(endDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e);
			if(!StringUtil.isEmpty(endDate)) {
				do {
					dateStr = format.format(calendar.getTime());
					list.add(dateStr);
					calendar.add(dateType, -1);
				} while(!dateStr.equals(beginDate));
			}
		} catch (ParseException var10) {
			var10.printStackTrace();
		}

		return list;
	}

	public static List<String> dateList(String beginDate, String endDate) {
		String formatStr = "yyyyMMdd";
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		ArrayList dateList = new ArrayList();

		try {
			Date e = format.parse(beginDate);
			Date end = format.parse(endDate);
			double between = (double)((end.getTime() - e.getTime()) / 1000L);
			double day = between / 86400.0D;

			for(int i = 0; (double)i <= day; ++i) {
				Calendar cd = Calendar.getInstance();
				cd.setTime(format.parse(beginDate));
				cd.add(5, i);
				dateList.add(format.format(cd.getTime()));
			}
		} catch (ParseException var13) {
			var13.printStackTrace();
		}

		return dateList;
	}

	public static String getFrontMonth(int monthNum, String dataDate) {
		String dateStr = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");

		try {
			Date e = format.parse(dataDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e);
			if(!StringUtil.isEmpty(dataDate)) {
				calendar.add(2, -monthNum);
				dateStr = format.format(calendar.getTime());
			}
		} catch (ParseException var6) {
			var6.printStackTrace();
		}

		return dateStr;
	}

	public static String getFrontDate(int frontNum, String dataDate, int updateCycle) {
		String dateStr = "";
		String formatStr = "";
		byte dateType = -1;
		if(2 == updateCycle) {
			formatStr = "yyyyMM";
			dateType = 2;
		} else if(1 == updateCycle) {
			formatStr = "yyyyMMdd";
			dateType = 6;
		}

		SimpleDateFormat format = new SimpleDateFormat(formatStr);

		try {
			Date e = format.parse(dataDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e);
			if(!StringUtil.isEmpty(dataDate)) {
				calendar.add(dateType, -frontNum);
				dateStr = format.format(calendar.getTime());
			}
		} catch (ParseException var9) {
			var9.printStackTrace();
		}

		return dateStr;
	}

	public static String getCustomFrontDate(int frontNum, String dataDate, int updateCycle) {
		String dateStr = "";
		String formatStr = "";
		byte dateType = -1;
		if(2 == updateCycle) {
			formatStr = "yyyyMM";
			dateType = 2;
		} else if(3 == updateCycle) {
			formatStr = "yyyyMMdd";
			dateType = 6;
		} else if(1 == updateCycle) {
			return dataDate;
		}

		SimpleDateFormat format = new SimpleDateFormat(formatStr);

		try {
			Date e = format.parse(dataDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e);
			if(!StringUtil.isEmpty(dataDate)) {
				calendar.add(dateType, -frontNum);
				dateStr = format.format(calendar.getTime());
			}
		} catch (ParseException var9) {
			var9.printStackTrace();
		}

		return dateStr;
	}

	public static String getLastMonthStr() {
		Calendar c = Calendar.getInstance();
		c.add(2, -1);
		Date dataDate = c.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		String dataTime = dateFormat.format(dataDate);
		return dataTime;
	}

	public static String getFrontDay(int dayNum, String dataDate) {
		String dateStr = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		try {
			Date e = format.parse(dataDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e);
			if(!StringUtil.isEmpty(dataDate)) {
				calendar.add(5, -dayNum);
				dateStr = format.format(calendar.getTime());
			}
		} catch (ParseException var6) {
			var6.printStackTrace();
		}

		return dateStr;
	}

	public static String getFrontDay(int dayNum, String dataDate, String dateFormat) {
		String dateStr = "";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);

		try {
			Date e = format.parse(dataDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e);
			if(!StringUtil.isEmpty(dataDate)) {
				calendar.add(5, -dayNum);
				dateStr = format.format(calendar.getTime());
			}
		} catch (ParseException var7) {
			var7.printStackTrace();
		}

		return dateStr;
	}

	public static Date lastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int value = cal.getActualMaximum(5);
		cal.set(5, value);
		return cal.getTime();
	}

	public static String lastDayOfMonth(String dataDate) {
		String dayStr = "";
		SimpleDateFormat month_format = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat day_format = new SimpleDateFormat("yyyyMMdd");

		try {
			Date e = month_format.parse(dataDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(e);
			int value = calendar.getActualMaximum(5);
			calendar.set(5, value);
			dayStr = day_format.format(calendar.getTime());
		} catch (ParseException var7) {
			var7.printStackTrace();
		}

		return dayStr;
	}

	public static Date firstDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int value = cal.getActualMinimum(5);
		cal.set(5, value);
		return cal.getTime();
	}

	public static String getLastDate(String currentDay, int num) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		String dateStr = null;

		try {
			date = df.parse(currentDay);
			Calendar e = Calendar.getInstance();
			e.setTime(date);
			int day = e.get(5);
			e.set(5, day - num);
			Date lastDate = e.getTime();
			dateStr = df.format(lastDate);
		} catch (ParseException var8) {
			var8.printStackTrace();
		}

		return dateStr;
	}

	public static String getLastDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(5);
		calendar.set(5, day - 1);
		Date lastDate = calendar.getTime();
		String dateStr = df.format(lastDate);
		return dateStr;
	}

	public static String getOffsetDate(int offset, int dateType) {
		String offsetDate = "";
		Calendar c = Calendar.getInstance();
		switch(dateType) {
			case 0:
				c.add(2, offset);
				SimpleDateFormat yyyyMMDateFormat = new SimpleDateFormat("yyyyMM");
				offsetDate = yyyyMMDateFormat.format(c.getTime());
				break;
			case 1:
				c.add(5, offset);
				SimpleDateFormat yyyyMMdateFormat = new SimpleDateFormat("yyyyMMdd");
				offsetDate = yyyyMMdateFormat.format(c.getTime());
		}

		return offsetDate;
	}

	public static String getOffsetDateByDate(String date, int offset, int dateType) {
		if(date != null && !"".equals(date)) {
			String offsetDate = "";
			Calendar c = Calendar.getInstance();

			try {
				switch(dateType) {
					case 0:
						SimpleDateFormat e = new SimpleDateFormat("yyyyMM");
						Date m_date = e.parse(date);
						c.setTime(m_date);
						c.add(2, offset);
						offsetDate = e.format(c.getTime());
						break;
					case 1:
						SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyyMMdd");
						Date d_date = yyyyMMddDateFormat.parse(date);
						c.setTime(d_date);
						c.add(5, offset);
						offsetDate = yyyyMMddDateFormat.format(c.getTime());
				}
			} catch (ParseException var9) {
				var9.printStackTrace();
			}

			return offsetDate;
		} else {
			return null;
		}
	}

	public static String getOffsetDateByDate(String date, int offset, int dateType, String fmt) {
		if(date != null && !"".equals(date)) {
			String offsetDate = "";
			Calendar c = Calendar.getInstance();

			try {
				switch(dateType) {
					case 2:
						SimpleDateFormat e = new SimpleDateFormat(fmt);
						Date m_date = e.parse(date);
						c.setTime(m_date);
						c.add(2, offset);
						offsetDate = e.format(c.getTime());
						break;
					case 3:
						SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat(fmt);
						Date d_date = yyyyMMddDateFormat.parse(date);
						c.setTime(d_date);
						c.add(5, offset);
						offsetDate = yyyyMMddDateFormat.format(c.getTime());
				}
			} catch (ParseException var10) {
				var10.printStackTrace();
			}

			return offsetDate;
		} else {
			return null;
		}
	}

	public static String getCurrentDay() {
		String date = date2String(new Date(), "yyyy-MM-dd") + " 00:00:00";
		return date;
	}

	public static String getCurrentDayYYYYMMDD() {
		String date = date2String(new Date(), "yyyyMMdd");
		return date;
	}

	public static int getUpdateCycleByDate(String dataDate) {
		byte updateCycle = 2;
		if(!StringUtil.isEmpty(dataDate)) {
			if(dataDate.length() == 6) {
				updateCycle = 2;
			} else if(dataDate.length() > 6) {
				updateCycle = 1;
			}
		}

		return updateCycle;
	}

	public static String getLabelStatTableName(String dataDate) {
		String tableName = "";
		int updateCycle = getUpdateCycleByDate(dataDate);
		String tempDataDate = "";
		if(updateCycle == 2) {
			tempDataDate = CacheBase.getInstance().getNewLabelMonth();
			tableName = TABLE_LABEL_STAT + "MM" + "_" + tempDataDate;
		} else if(updateCycle == 1) {
			tableName = TABLE_LABEL_STAT + "DM" + "_" + dataDate;
		}

		return tableName;
	}

	public static String getLabelTrendTableName(int updateCycle) {
		String tableName = "";
		String dataDate = "";
		if(updateCycle == 2) {
			dataDate = CacheBase.getInstance().getNewLabelMonth();
			tableName = TABLE_LABEL_STAT + "MM" + "_" + dataDate;
		} else if(updateCycle == 1) {
			dataDate = CacheBase.getInstance().getNewLabelDay();
			tableName = TABLE_LABEL_STAT + "DM" + "_" + dataDate;
		}

		return tableName;
	}

	public static String dateFormat(String dataDate) {
		String formatStr = "";
		String resultFormatStr = "";
		String dateStr = "";
		if(null != dataDate && !"".equals(dataDate)) {
			if(dataDate.length() == 6) {
				formatStr = "yyyyMM";
				resultFormatStr = "yyyy-MM";
			}

			if(dataDate.length() == 8) {
				formatStr = "yyyyMMdd";
				resultFormatStr = "yyyy-MM-dd";
			}

			SimpleDateFormat format = new SimpleDateFormat(formatStr);
			SimpleDateFormat resultFormat = new SimpleDateFormat(resultFormatStr);

			try {
				Date e = format.parse(dataDate);
				dateStr = resultFormat.format(e);
			} catch (ParseException var7) {
				var7.printStackTrace();
			}

			return dateStr;
		} else {
			return null;
		}
	}

	public static Date getStartDate(Date date, int latestDays) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(5, -latestDays);
		return cd.getTime();
	}

	public static int dateInterval(String beginDate, String endDate, String formatStr, int updateCycle) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		int interval = 0;
		if(StringUtil.isNotEmpty(beginDate) && StringUtil.isNotEmpty(endDate)) {
			try {
				Date e = format.parse(beginDate);
				Date end = format.parse(endDate);
				if(updateCycle == 2) {
					Calendar beginTime = Calendar.getInstance();
					beginTime.setTime(e);
					int year1 = beginTime.get(1);
					int endTime = beginTime.get(2);
					Calendar calendarEnd = Calendar.getInstance();
					calendarEnd.setTime(end);
					int between = calendarEnd.get(1);
					int month2 = calendarEnd.get(2);
					if(year1 == between) {
						interval = month2 - endTime;
					} else {
						interval = 12 * (between - year1) + month2 - endTime;
					}
				} else if(updateCycle == 3) {
					long beginTime1 = e.getTime();
					long endTime1 = end.getTime();
					long between1 = (endTime1 - beginTime1) / 1000L;
					interval = (int)between1 / 86400;
				}
			} catch (ParseException var14) {
				var14.printStackTrace();
			}
		}

		return interval;
	}

	public static String calculateOffsetDate(String dateStr, int interval, String formatStr, int updateCycle) {
		String resultDate = dateStr;
		if(StringUtil.isNotEmpty(dateStr) && interval != 0) {
			SimpleDateFormat format = new SimpleDateFormat(formatStr);

			try {
				Date e = format.parse(dateStr);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(e);
				if(updateCycle == 2) {
					calendar.add(2, interval);
					resultDate = format.format(calendar.getTime());
				} else if(updateCycle == 3) {
					calendar.add(5, interval);
					resultDate = format.format(calendar.getTime());
				}
			} catch (Exception var8) {
				var8.printStackTrace();
			}
		}

		return resultDate;
	}

	public static Date addYears(Date date, int amount) {
		return add(date, 1, amount);
	}

	public static Date addMonths(Date date, int amount) {
		return add(date, 2, amount);
	}

	public static Date addWeeks(Date date, int amount) {
		return add(date, 3, amount);
	}

	public static Date addDays(Date date, int amount) {
		return add(date, 5, amount);
	}

	public static Date addHours(Date date, int amount) {
		return add(date, 11, amount);
	}

	public static Date addMinutes(Date date, int amount) {
		return add(date, 12, amount);
	}

	public static Date addSeconds(Date date, int amount) {
		return add(date, 13, amount);
	}

	public static Date addMilliseconds(Date date, int amount) {
		return add(date, 14, amount);
	}

	/** @deprecated */
	public static Date add(Date date, int calendarField, int amount) {
		if(date == null) {
			throw new IllegalArgumentException("The date must not be null");
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(calendarField, amount);
			return c.getTime();
		}
	}

	public static String getEarlierDate(String dateAStr, String dateBStr, String format) {
		Date dateA = string2Date(dateAStr, format);
		Date dateB = string2Date(dateBStr, format);
		return dateA.getTime() > dateB.getTime()?dateBStr:dateAStr;
	}

	public static void main(String[] args) {
		System.out.println(getEarlierDate("20150201", "20150202", "yyyyMMdd"));
	}
}
