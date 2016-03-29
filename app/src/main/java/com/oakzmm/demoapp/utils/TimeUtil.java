package com.oakzmm.demoapp.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static final String patternMSDate = "yyyyMMddHHmmssSSS";
	public static final String patternFileName = "yyyyMMddHHmmss";
	public static final String patternSQLDate = "yyyy-MM-dd HH:mm:ss";
	public static final String patternMdHmsTime = "yyyy-MM-dd";
//	public static final String patternGetDay = "yyyy-MM-dd";
	public static final String patternGetDay = "MM-dd";
//	public static final String patternGetTime = "HH:mm:ss";
	public static final String patternGetTime = "HH:mm";
	public static final String patternGetMonth = "yyyy-MM";

	public static long getDateString2Long(String time, String pattern) {
		return getDateTimeString2Long(time, pattern);
	}

	public static String getDateLong2String(long time, String pattern) {
		return getDateTimeLong2String(time, pattern);
	}

	public static String getCurrentSQLDateTimeString() {
		return getSQLDateTimeString(new Date(), patternSQLDate);
	}

	public static String getCurrentDateTimeString() {
		return getSQLDateTimeString(new Date(), patternFileName);
	}

	public static String getCurrentDateTimeForMSString() {
		return getSQLDateTimeString(new Date(), patternMSDate);
	}

	public static String getSQLDateTimeString(Date date) {
		if (date == null)
			date = new Date();
		return getSQLDateTimeString(date, patternSQLDate);
	}

	public static String getDateTimeString(Date date) {
		if (date == null)
			date = new Date();
		return getSQLDateTimeString(date, patternFileName);
	}

	public static String getCurrentDayTimeString(String date) {
		return getStringTimeByDateTimeString(date, patternGetDay);
	}

	public static String getCurrentDayTimeString(Date date) {
		return getStringTimeByDateTimeString(date, patternGetDay);
	}

	public static String getCurrentMdHmsTimeString(String date) {
		return getStringTimeByDateTimeString(date, patternMdHmsTime);
	}

	public static String getCurrentMdHmsTimeString(Date date) {
		return getStringTimeByDateTimeString(date, patternMdHmsTime);
	}

	public static String getCurrentTimeString(String date) {
		return getStringTimeByDateTimeString(date, patternGetTime);
	}

	public static String getCurrentTimeString(Date date) {
		return getStringTimeByDateTimeString(date, patternGetTime);
	}

	public static String getCurrentDayTimeString() {
		return getCurrentDayTimeString(new Date());
	}

	public static String getCurrentDayTimeString(Date date, int count) {
		return getCurrentDayTimeString(date, count);
	}

	public static String getCurrentDayTimeString(int count) {
		return getDayTimeString(new Date(), count);
	}

//	public static String getLocalFormTimeString(Context ctx, String date) {
//		String format = WizSystemSettings.systemTimeFormat(ctx);
//		if (TextUtils.isEmpty(format))
//			format = patternGetDay;
//
//		return getStringTimeByDateTimeString(date, format);
//	}
//	
	// Time Past
	public static long getCurrentSQLDateTimePastLongForWeek(int countWeek) {
		return getCurrentSQLDateTimePastLongForDay(countWeek * 7);
	}

	public static long getCurrentSQLDateTimePastLongForMonth(int countMonth) {
		return getCurrentSQLDateTimePastLongForDay(countMonth * 30);
	}
	
	public static long getCurrentSQLDateTimePastLongForDay(int countDay) {
		String dt = getSQLDateTimeString(new Date(), countDay);
		return getDateTimeString2Long(dt, patternWeeHours);
	}

	// Time Past
	public static String getCurrentSQLDateTimePastStringForWeek(int countWeek) {
		return getCurrentSQLDateTimePastStringForDay(countWeek * 7);
	}

	public static String getCurrentSQLDateTimePastStringForMonth(int countMonth) {
		return getCurrentSQLDateTimePastStringForDay(countMonth * 30);
	}

	public static String getCurrentSQLDateTimePastStringForDay(int countDay) {
		return getSQLDateTimeString(new Date(), countDay);
	}

	public static final String patternWeeHours = "yyyy-MM-dd 00:00:00";
	public static String getSQLDateTimeString(Date dt, int countDay) {
		if (dt == null)
			dt = new Date();
		int day = dt.getDate() - countDay;
		dt.setDate(day);
		SimpleDateFormat date = new SimpleDateFormat(patternWeeHours);
		return date.format(dt);
	}

	public static long getDateTimeString2Long(String dt, String pattern) {
		SimpleDateFormat dateformat = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
		Date date2 = null;
		try {
			date2 = dateformat.parse(dt);// 
		} catch (ParseException e) {
			return 0;
		}
		return date2.getTime();
	}

	public static String getDateTimeLong2String(long dt, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(dt);
	}

	public static String getSQLDateTimeString(Date dt, String pattern) {
		SimpleDateFormat date = new SimpleDateFormat(pattern);
		if (dt == null)
			dt = new Date();
		return date.format(dt);
	}

	public static String getDayTimeString(Date dt, int countDay) {
		SimpleDateFormat date = new SimpleDateFormat(patternGetDay);
		if (dt == null)
			dt = new Date();
		int day = dt.getDate() - countDay;
		dt.setDate(day);
		return date.format(dt);
	}

	public static Date getDateFromSqlDateTimeString(String sqlDateTimeString) {
		try {
			if (TextUtils.isEmpty(sqlDateTimeString))
				throw new Exception("date is Null");
			SimpleDateFormat dateFormatter = new SimpleDateFormat(patternSQLDate);
			return dateFormatter.parse(sqlDateTimeString);
		} catch (ParseException e) {
			return new Date();
		} catch (Exception e) {
			return new Date();
		}
	}

	public static Date getModifiedDateByFile(String file) {
		return getModifiedDateByFile(new File(file));
	}

	@SuppressWarnings("unused")
	public static Date getModifiedDateByFile(File file) {
		long time = file.lastModified();
		Date modified = new Date(time);
		if (modified == null)
			modified = new Date();
		return modified;
	}

	public static String getStringTimeByDateTimeString(String timeString, String pattern) {
		try {
			Date dt = getDateByDateTimeString(timeString, patternSQLDate);
			return getStringTimeByDateTimeString(dt, pattern);
		} catch (ParseException e) {
			return "";
		}
	}

	public static String getStringTimeByDateTimeString(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Date getDateByDateTimeString(String timeString, String pattern)
			throws ParseException {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		return dateFormatter.parse(timeString);
	}

	public static String toValidFileName(String fileName) {
		fileName = fileName.replace(':', '-');
		fileName = fileName.replace('/', '-');
		fileName = fileName.replace('\\', '-');
		fileName = fileName.replace(',', '-');
		fileName = fileName.replace('?', '-');
		fileName = fileName.replace('.', '-');
		fileName = fileName.replace('!', '-');
		fileName = fileName.replace('\'', '-');
		fileName = fileName.replace('"', '-');
		fileName = fileName.replace('`', '-');
		fileName = fileName.replace('\r', '-');
		fileName = fileName.replace('\n', '-');
		if (fileName.length() > 100)
			fileName = fileName.substring(0, 100);
		//
		return fileName;
	}

	public static String getDateTimeFileTitle() {
		Date dt = new Date();
		return getDateTimeFileTitle(dt);
	}

	public static String getDateTimeFileTitle(Date dt) {
		String fileName = dt.toLocaleString();//
		return toValidFileName(fileName);//
	}

	public static String formatInt(int n, int width) {
		String str = Integer.toString(n);
		int count = width - str.length();
		for (int i = 0; i < count; i++) {
			str = "0" + str;
		}
		return str;
	}
}
