package com.zhou.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public final class CalendarUtil
{
  public static final long SECOND =  1000l;
  public static final long MINUTE =  60 * SECOND;
  public static final long HOUR =  60 * MINUTE;
  public static final long DAY =  24 * HOUR;
  private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
  private static SimpleDateFormat formatter4 = new SimpleDateFormat("MM-dd");
  private static SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
  private static Calendar calendar = Calendar.getInstance();
  private static TimeZone timeZone = calendar.getTimeZone();
  private static SimpleDateFormat formatter5 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  public static synchronized void setDateFormat(String paramString)
  {
    formatter = new SimpleDateFormat(paramString);
  }

  public static synchronized String format3(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    return formatter3.format(calendar.getTime());
  }

  public static synchronized String format(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatSimpleDate(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    return formatter2.format(calendar.getTime());
  }

  public static synchronized String formatSimpleMonthDate(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    return formatter4.format(calendar.getTime());
  }

  public static synchronized String formatAddYear(long paramLong, int paramInt)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.add(1, paramInt);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatAddMonth(long paramLong, int paramInt)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.add(2, paramInt);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatAddDay(long paramLong, int paramInt)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.add(5, paramInt);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatAddHour(long paramLong, int paramInt)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.add(11, paramInt);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatAddMinute(long paramLong, int paramInt)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.add(12, paramInt);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatAddSecond(long paramLong, int paramInt)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.add(13, paramInt);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatDayStart(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.set(11, 0);
    calendar.set(12, 0);
    calendar.set(13, 0);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatDayEnd(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.set(11, 23);
    calendar.set(12, 59);
    calendar.set(13, 59);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatMonthStart(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.set(5, 1);
    calendar.set(11, 0);
    calendar.set(12, 0);
    calendar.set(13, 0);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatMonthEnd(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.set(5, calendar.getActualMaximum(5));
    calendar.set(11, 23);
    calendar.set(12, 59);
    calendar.set(13, 59);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatYearStart(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.set(2, 0);
    calendar.set(5, 1);
    calendar.set(11, 0);
    calendar.set(12, 0);
    calendar.set(13, 0);
    return formatter.format(calendar.getTime());
  }

  public static synchronized String formatYearEnd(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    calendar.set(2, 11);
    calendar.set(5, 31);
    calendar.set(11, 23);
    calendar.set(12, 59);
    calendar.set(13, 59);
    return formatter.format(calendar.getTime());
  }

  public static synchronized long leftSecondsToNextHour(long paramLong)
  {
    calendar.setTimeInMillis(paramLong);
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.add(11, 1);
    return TimeUnit.MILLISECONDS.toSeconds(localCalendar.getTimeInMillis() - calendar.getTimeInMillis());
  }

  public static synchronized long convert(String paramString)
  {
    try
    {
      calendar.setTime(formatter.parse(paramString));
      return calendar.getTimeInMillis();
    }
    catch (ParseException localParseException)
    {
      localParseException.printStackTrace();
    }
    return 0L;
  }

  public static synchronized long convert3(String paramString)
  {
    try
    {
      calendar.setTime(formatter3.parse(paramString));
      return calendar.getTimeInMillis();
    }
    catch (ParseException localParseException)
    {
      localParseException.printStackTrace();
    }
    return 0L;
  }

  public static synchronized long convert5(String paramString)
  {
    try
    {
      calendar.setTime(formatter5.parse(paramString));
      return calendar.getTimeInMillis();
    }
    catch (ParseException localParseException)
    {
      localParseException.printStackTrace();
    }
    return 0L;
  }

  public static long getLocalTimeZoneRawOffset()
  {
    return timeZone.getRawOffset();
  }

  public static long getTimeInMillisWithoutDay(long paramLong)
  {
    return (paramLong + getLocalTimeZoneRawOffset()) % 86400000L;
  }
  
  
	public static int getSecondsToClock(int hour){
		return getSecondsToClock(hour,0);
	}
  
  /**
	 * 取得当前时间距离之后的某一时、分的秒数（例如8:30点）
	 * @return
	 */
	public static int getSecondsToClock(int hour, int minute){
		return getSecondsToClock(hour, minute, 0);
	}
	
	public static int getSecondsToClock(int hour, int minute,int second){
		long temp = (CalendarUtil.getTimeInMillisWithoutDay(System.currentTimeMillis())) / SECOND;
		int destSeconds = hour * 60 * 60 + minute * 60 + second;
		if(temp < destSeconds){
			return (int)(destSeconds - temp);
		}else{
			return (int)((DAY / SECOND) - (temp - destSeconds));
		}
	}

	/**
	 * 取得现在距离        某年-某月-某日         某时:某分:某秒          的秒数
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static long getSecondesToDayAndClock(int year, int month, int day, int hour, int minute, int second) {
		//取得今日的月、日， 比较
		Calendar c = Calendar.getInstance();
		long nowseconds = c.getTimeInMillis();
		c.set(year, month-1, day, hour, minute, second);
		nowseconds = c.getTimeInMillis() - nowseconds;
		c = Calendar.getInstance();
		return nowseconds/1000;
	}

	public int getNextIntegralPointHour() {
		Calendar ca = Calendar.getInstance();
		int hour = ca.get(Calendar.HOUR_OF_DAY) + 1;
//		System.err.println("getNextIntegralPointHour :" + (hour >= 24 ? hour - 24 : hour));
		return hour >= 24 ? hour - 24 : hour;
	}
	
	public static boolean isTheSameDay(long date1,long date2) {
		if (Math.abs(date1-date2) > (DAY + HOUR)) return false;
		return getDateField(date1, Calendar.DAY_OF_YEAR) == getDateField(date2, Calendar.DAY_OF_YEAR);
	}
	
	public static int getDateField(long date,int field) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		return c.get(field);
	}
}



/* Location:           D:\online_project\cs_3d_exp\lib\joy.jar
 * Qualified Name:     com.joymeng.services.utils.CalendarUtil
 * JD-Core Version:    0.6.0
 */