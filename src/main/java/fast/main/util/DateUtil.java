package fast.main.util;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.ParseException;


/**
 * Created by IntelliJ IDEA.
 * User: phoenix
 * Date: 2004-2-23
 * Time: 17:28:00
 * 日期型变量操作函数类
 */
public class DateUtil {
	public static final int TYPE_YEAR = 1;
	public static final int TYPE_MONTH = 2;
	public static final int TYPE_DAY = 3;
	public static final int TYPE_HOUR = 4;
	public static final int TYPE_MINUTE = 5;

	/**
	 * 返回当前的日期时间的字符表示
	 * 默认使用 yyyy-MM-dd HH:mm:ss 格式
	 * @return  String  当前时间("yyyy-MM-dd HH:mm:ss" 格式)
	 */
	public static String getNowString(){
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	/**
	 * 返回当前的日期时间的字符表示
	 * @param  format   日期格式
	 * @return  String  当前时间
	 */
	public static String getNowString(String format){
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	/**
	 * 返回当前的日期时间的 java.sql.Date 表示
	 * @return  java.sql.Date  当前时间
	 */
	public static java.sql.Date getNowSqlDate() {
		Date currentTime = new Date();
		return new java.sql.Date(currentTime.getTime());
	}

	/**
	 * 日期型变量由 String 转换成 java.util.Date
	 * @param date  String 型变量("yyyy-MM-dd" 格式)
	 * @return  java.util.Date 型变量

	 */
	public static Date parseStringToUtil(String date) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dtResult = null;
		try {
			dtResult = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dtResult;
	}

	/**
	 * 日期型变量由 String 转换成 java.util.Date
	 * @param date      String 型变量

	 * @param format    日期格式
	 * @return  java.util.Date 型变量

	 */
	public static Date parseStringToUtil(String date, String format) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		Date dtResult = null;
		try {
			dtResult = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dtResult;
	}

	/**
	 * 日期型变量由 java.sql.Date 转换成 java.util.Date
	 * @param  date     java.util.Date 型变量

	 * @return  java.sql.Date 型变量

	 */
	public static java.sql.Date parseUtilToSql(Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 日期型变量由 java.util.Date 转换成 java.sql.Date
	 * @param  date     java.sql.Date 型变量

	 * @return  java.util.Date 型变量

	 */
	public static Date parseSqlToUtil(java.sql.Date date) {
		return new Date(date.getTime());
	}

	/**
	 * 日期型变量由 java.util.Date 转换成 String
	 * 默认使用 yyyy-MM-dd HH:mm:ss 格式
	 * @param  date     java.util.Date 型变量

	 * @return  String 型日期("yyyy-MM-dd HH:mm:ss" 格式)
	 */
	public static String parseUtilToString(Date date) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (null != date) {
			return formatter.format(date);
		} else {
			return null;
		}
	}

	/**
	 * 日期型变量由 java.util.Date 转换成 String
	 * @param  date     java.util.Date 型变量

	 * @param  format   日期格式
	 * @return  String 型日期

	 */
	public static String parseUtilToString(Date date, String format) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		if (null != date) {
			return formatter.format(date);
		} else {
			return null;
		}
	}

	/**
	 * 日期型变量由 java.sql.Date 转换成 String
	 * 默认使用 yyyy-MM-dd HH:mm:ss 格式
	 * @param  date     java.sql.Date 型变量

	 * @return  String 型日期("yyyy-MM-dd HH:mm:ss" 格式)
	 */
	public static String parseSqlToString(java.sql.Date date) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (null != date) {
			return formatter.format(date);
		} else {
			return null;
		}
	}

	/**
	 * 日期型变量由 java.sql.Date 转换成 String
	 * @param  date     java.sql.Date 型变量

	 * @param  format   日期格式
	 * @return  String 型日期

	 */
	public static String parseSqlToString(java.sql.Date date, String format) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		if (null != date) {
			return formatter.format(date);
		} else {
			return null;
		}
	}

	/**
	 * 格式化日期

	 * @param  date     String 型日期变量

	 * @param  format   日期格式
	 * @return  String 型日期

	 */
	public static String parseSimpleToChn(String date, String format) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		return formatter.format(parseStringToUtil(date));
	}

	/**
	 * 格式化日期

	 * @param  date     String 型日期变量

	 * @return  String 型日期

	 */
	public static String parseChnToSimple(String date) {
		date = StringUtil.stringReplace(date, "年", "-");
		date = StringUtil.stringReplace(date, "月", "-");
		date = StringUtil.stringReplace(date, "日", " ");
		date = StringUtil.stringReplace(date, "时", ":");
		date = StringUtil.stringReplace(date, "分", ":");
		date = StringUtil.stringReplace(date, "秒", "");
		return date.trim();
	}

	/**
	 * 通过字符串型的日期取得年份

	 * @param date      字符串型日期
	 * @return  年份
	 */
	public static int getYear(String date) {
		int intIndex1 = date.indexOf("-");
		String strYear = date.substring(0, intIndex1);
		return Integer.parseInt(strYear, 10);
	}

	/**
	 * 通过字符串型的日期取得月份

	 * @param date      字符串型日期
	 * @return  月份
	 */
	public static int getMonth(String date) {
		int intIndex1 = date.indexOf("-");
		int intIndex2 = date.indexOf("-", intIndex1+1);
		String strMonth = date.substring(intIndex1+1, intIndex2);
		return Integer.parseInt(strMonth, 10);
	}

	/**
	 * 通过字符串型的日期取得日期

	 * @param date      字符串型日期
	 * @return  日期
	 */
	public static int getDay(String date) {
		int intIndex2 = date.lastIndexOf("-");
		String strDay = "";
		if (date.indexOf(" ") == -1)
			strDay = date.substring(intIndex2+1);
		else
			strDay = date.substring(intIndex2+1, date.indexOf(" "));
		return Integer.parseInt(strDay, 10);
	}

	/**
	 * 通过字符串型的日期取得季度

	 * @param date      字符串型日期
	 * @return  季度
	 */
	public static int getQuarter(String date) {
		String strModel = "0111222333444";
		int intMonth = getMonth(date);
		return Integer.parseInt(String.valueOf(strModel.charAt(intMonth)), 10);
	}

	/**
	 * 计算两日期之间差值

	 * @param type  比较类型
	 * @param date1 开始日期

	 * @param date2 截止
	 * @return  差值

	 */
	public static long getDateOffset(int type, Date date1, Date date2) {
		long lngDate1 = date1.getTime();
		long lngDate2 = date2.getTime();

		long MinMilli = 1000 * 60;
		long HrMilli = MinMilli * 60;
		long DyMilli = HrMilli * 24;

		long lngTemp, lngResult;
		switch (type) {
		case DateUtil.TYPE_MINUTE:
			lngTemp = lngDate2 - lngDate1;
			lngResult = lngTemp / MinMilli;
			break;
		case DateUtil.TYPE_HOUR:
			lngTemp = lngDate2 - lngDate1;
			lngResult = lngTemp / HrMilli;
			break;
		case DateUtil.TYPE_DAY:
			lngTemp = lngDate2 - lngDate1;
			lngResult = lngTemp / DyMilli;
			break;
		case DateUtil.TYPE_MONTH:
			lngTemp = getYear(parseUtilToString(date2)) - getYear(parseUtilToString(date1));
			lngResult = lngTemp * 12;
			break;
		case DateUtil.TYPE_YEAR:
			lngResult = getYear(parseUtilToString(date2)) - getYear(parseUtilToString(date1));
			break;
		default:
			lngResult = 0;
		}
		return lngResult;
	}

	/**
	 * 日期增减函数
	 * @param type      比较类型
	 * @param date      原日期

	 * @param offset    差值

	 * @return  结果日期
	 */
	public static Date setDateOffset(int type, Date date, long offset) {
		long lngDate = date.getTime();

		long MinMilli = 1000 * 60;
		long HrMilli = MinMilli * 60;
		long DyMilli = HrMilli * 24;

		long lngTemp, lngResult;
		switch (type) {
		case DateUtil.TYPE_MINUTE:
			lngTemp = offset * MinMilli;
			lngResult = lngDate + lngTemp;
			break;
		case DateUtil.TYPE_HOUR:
			lngTemp = offset * HrMilli;
			lngResult = lngDate + lngTemp;
			break;
		case DateUtil.TYPE_DAY:
			lngTemp = offset * DyMilli;
			lngResult = lngDate + lngTemp;
			break;
		case DateUtil.TYPE_MONTH:
			lngResult = lngDate;
			break;
		case DateUtil.TYPE_YEAR:
			lngResult = lngDate;
			break;
		default:
			lngResult = lngDate;
		}
		return new Date(lngResult);
	}

	//    /**
	//     * 取指定日期为星期几

	//     * @return 星期几

	//     */
	//    public static long getWeek() {
	//        Date now = new Date();
	//        long lngDateTime = now.getTime();
	//
	//        long MinMilli = 1000 * 60;
	//	    long HrMilli = MinMilli * 60;
	//	    long DyMilli = HrMilli * 24;
	//
	//        //取与1970-1-1日相差几天，1970-1-1日为星期四

	//        long lngDayFrom = lngDateTime / DyMilli;
	//        return (lngDayFrom % 7) + 4;
	//    }
	//
	//    /**
	//     * 取指定日期为星期几

	//     * @param date  指定日期
	//     * @return 星期几

	//     */
	//    public static long getWeek(Date date) {
	//        long lngDateTime = date.getTime();
	//
	//        long MinMilli = 1000 * 60;
	//	    long HrMilli = MinMilli * 60;
	//	    long DyMilli = HrMilli * 24;
	//
	//        //取与1970-1-1日相差几天，1970-1-1日为星期四

	//        long lngDayFrom = lngDateTime / DyMilli;
	//        return (lngDayFrom % 7) + 4;
	//    }

	/**
	 * 取指定日期为星期几

	 * @return 星期几

	 */
	public static long getWeek() {;
	Calendar calendar = new GregorianCalendar();
	int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	if (intTemp == 0) intTemp = 7;
	return intTemp;
	}

	/**
	 * 取指定日期为星期几

	 * @param date  指定日期
	 * @return 星期几

	 */
	public static long getWeek(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (intTemp == 0) intTemp = 7;
		return intTemp;
	}

	/**
	 * 取得一个月有几天

	 * @param year  年份
	 * @param month 月份
	 * @return 天数
	 */
	public static long getDayCountInMonth(int year, int month) {
		Date firstDay = parseStringToUtil(String.valueOf(year) + "-" + String.valueOf(month) + "-1", "yyyy-MM-dd");
		Date nextFirstDay = null;
		if (month == 12)
			nextFirstDay = parseStringToUtil(String.valueOf(year+1) + "-1-1", "yyyy-MM-dd");
		else
			nextFirstDay = parseStringToUtil(String.valueOf(year) + "-" + String.valueOf(month+1) + "-1", "yyyy-MM-dd");
		return getDateOffset(TYPE_DAY, firstDay, nextFirstDay);
	}
}

