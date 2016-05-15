package com.object0r.toortools.helpers;


import org.joda.time.DateTime;
import org.joda.time.Period;

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
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
