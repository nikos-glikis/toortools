package com.object0r.toortools.helpers;


import org.apache.commons.lang.time.DurationFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelper
{
    public static long getDateDiff(Date startTime, Date endTime, TimeUnit timeUnit)
    {
        DateTime startDateTime = new DateTime(startTime);
        DateTime endDateTime = new DateTime(endTime);
        return timeUnit.convert(endDateTime.getMillis() - startDateTime.getMillis(), TimeUnit.MILLISECONDS);
    }

    public static long getDateDiff(Calendar startTime, Calendar endTime, TimeUnit timeUnit)
    {
        DateTime startDateTime = new DateTime(startTime.getTime());
        DateTime endDateTime = new DateTime(endTime.getTime());
        return timeUnit.convert(endDateTime.getMillis() - startDateTime.getMillis(), TimeUnit.MILLISECONDS);
    }

    public static Date addDaysToDate(Date date, int days)
    {
        Date d = new Date(date.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public long getCurrentUnixTime()
    {
        return System.currentTimeMillis() / 1000L;
    }

    public static  String getMsDurationInHumanReadable(long ms)
    {
        return getMsDurationInHumanReadable(ms, true, true);
    }

    public static String getMsDurationInHumanReadable(long ms, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements)
    {
        return DurationFormatUtils.formatDurationWords(ms, suppressLeadingZeroElements, suppressTrailingZeroElements);
    }
}
