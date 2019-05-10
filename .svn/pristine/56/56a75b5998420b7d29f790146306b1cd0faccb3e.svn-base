package cn.appoa.aframework.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final long INTERVAL_IN_MILLISECONDS = 30000L;

    public static String getTimestampString(Date date) {
        if (date == null)
            return "";
        String str1 = null;
        String str2 = Locale.getDefault().getLanguage();
        boolean bool = str2.startsWith("zh");
        long l = date.getTime();
        if (isSameDay(l)) {
            // 相差的秒数
            long time = (Calendar.getInstance().getTimeInMillis() - date.getTime()) / 1000;
            StringBuffer sb = new StringBuffer();
            if (bool) {
                if (time > 0 && time < 60) {
                    return sb.append(time + "秒前").toString();
                } else if (time > 60 && time < 3600) {
                    return sb.append(time / 60 + "分钟前").toString();
                } else if (time >= 3600 && time < 3600 * 24) {
                    return sb.append(time / 3600 + "小时前").toString();
                } else {
                    str1 = "aa hh:mm";
                }
            } else {
                if (time > 0 && time < 60) {
                    return sb.append(time + "second before").toString();
                } else if (time > 60 && time < 3600) {
                    return sb.append(time / 60 + "minute ago").toString();
                } else if (time >= 3600 && time < 3600 * 24) {
                    return sb.append(time / 3600 + "hour ago").toString();
                } else {
                    str1 = "hh:mm aa";
                }
            }
        } else if (isYesterday(l)) {
            if (bool)
                str1 = "昨天aa hh:mm";
            else
                return "Yesterday " + new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(date);
        } else if (isBeforeYesterday(l)) {
            if (bool)
                str1 = "前天aa hh:mm";
            else
                return "Before Yesterday " + new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(date);
        } else if (bool)
            str1 = "MM月dd日aa hh:mm";
        else
            str1 = "MMM dd hh:mm aa";
        if (bool)
            return new SimpleDateFormat(str1, Locale.CHINESE).format(date);
        return new SimpleDateFormat(str1, Locale.ENGLISH).format(date);
    }

    public static String getTimestampString(String time, String format) {
        Date date = StringToDate(time, format);
        return getTimestampString(date);
    }

    public static String getTimestampString(String time) {
//		标准定义表
//		字母 	日期或时间元素 				表示 					示例
//		G	Era 标志符				Text				AD
//		y	年						Year				1996; 96
//		M	年中的月份					Month				July; Jul; 07
//		w	年中的周数					Number				27
//		W	月份中的周数				Number				2
//		D	年中的天数					Number				189
//		d	月份中的天数				Number				10
//		F	月份中的星期				Number				2
//		E	星期中的天数				Text				Tuesday; Tue
//		a	Am/pm 标记				Text				PM
//		H	一天中的小时数（0-23）		Number				0
//		k	一天中的小时数（1-24）		Number				24
//		K	am/pm 中的小时数（0-11）	Number				0
//		h	am/pm 中的小时数（1-12）	Number				12
//		m	小时中的分钟数				Number				30
//		s	分钟中的秒数				Number				55
//		S	毫秒数					Number				978
//		z	时区						General time zone	Pacific Standard Time; PST; GMT-08:00
//		Z	时区						RFC 822 time zone	-0800
        Date date = StringToDate(time, "yyyy-MM-dd HH:mm:ss");
        return getTimestampString(date);
    }

    public static boolean isCloseEnough(long time1, long time2) {
        long l = time1 - time2;
        if (l < 0L)
            l = -l;
        return l < INTERVAL_IN_MILLISECONDS;
    }

    private static boolean isSameDay(long time) {
        TimeInfo localTimeInfo = getTodayStartAndEndTime();
        return (time > localTimeInfo.getStartTime()) && (time < localTimeInfo.getEndTime());
    }

    private static boolean isYesterday(long time) {
        TimeInfo localTimeInfo = getYesterdayStartAndEndTime();
        return (time > localTimeInfo.getStartTime()) && (time < localTimeInfo.getEndTime());
    }

    private static boolean isBeforeYesterday(long time) {
        TimeInfo localTimeInfo = getBeforeYesterdayStartAndEndTime();
        return (time > localTimeInfo.getStartTime()) && (time < localTimeInfo.getEndTime());
    }

    @SuppressLint({"SimpleDateFormat"})
    public static Date StringToDate(String time, String format) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = localSimpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @SuppressWarnings("unused")
    @SuppressLint({"DefaultLocale"})
    public static String toTime(int time) {
        time /= 1000;
        int i = time / 60;
        int j = 0;
        if (i >= 60) {
            j = i / 60;
            i %= 60;
        }
        int k = time % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(k)});
    }

    @SuppressWarnings("unused")
    @SuppressLint({"DefaultLocale"})
    public static String toTimeBySecond(int second) {
        int i = second / 60;
        int j = 0;
        if (i >= 60) {
            j = i / 60;
            i %= 60;
        }
        int k = second % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(k)});
    }

    public static TimeInfo getYesterdayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(5, -1);
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(5, -1);
        localCalendar2.set(11, 23);
        localCalendar2.set(12, 59);
        localCalendar2.set(13, 59);
        localCalendar2.set(14, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static TimeInfo getTodayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.set(11, 23);
        localCalendar2.set(12, 59);
        localCalendar2.set(13, 59);
        localCalendar2.set(14, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static TimeInfo getBeforeYesterdayStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(5, -2);
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(5, -2);
        localCalendar2.set(11, 23);
        localCalendar2.set(12, 59);
        localCalendar2.set(13, 59);
        localCalendar2.set(14, 999);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static TimeInfo getCurrentMonthStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.set(5, 1);
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static TimeInfo getLastMonthStartAndEndTime() {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(2, -1);
        localCalendar1.set(5, 1);
        localCalendar1.set(11, 0);
        localCalendar1.set(12, 0);
        localCalendar1.set(13, 0);
        localCalendar1.set(14, 0);
        Date localDate1 = localCalendar1.getTime();
        long l1 = localDate1.getTime();
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(2, -1);
        localCalendar2.set(5, 1);
        localCalendar2.set(11, 23);
        localCalendar2.set(12, 59);
        localCalendar2.set(13, 59);
        localCalendar2.set(14, 999);
        localCalendar2.roll(5, -1);
        Date localDate2 = localCalendar2.getTime();
        long l2 = localDate2.getTime();
        TimeInfo localTimeInfo = new TimeInfo();
        localTimeInfo.setStartTime(l1);
        localTimeInfo.setEndTime(l2);
        return localTimeInfo;
    }

    public static String getTimestampStr() {
        return Long.toString(System.currentTimeMillis());
    }
}
