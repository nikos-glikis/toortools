package com.object0r.toortools.tests;

import com.object0r.toortools.helpers.DateHelper;

import org.junit.Test;
import org.junit.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTest
{

    @Test
    public void testDateDiffDays()
    {
        //getDateDiff Date
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1925);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 4);
        Date start  = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1926);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 3);
        Date end = cal.getTime();

        Assert.assertEquals(DateHelper.getDateDiff(start, end, TimeUnit.DAYS), 364);
        Assert.assertEquals(DateHelper.getDateDiff(start, end, TimeUnit.HOURS), 8736);
        Assert.assertEquals(DateHelper.getDateDiff(start, end, TimeUnit.MINUTES), 524160);
        Assert.assertEquals(DateHelper.getDateDiff(start, end, TimeUnit.SECONDS), 31449600);

        //getDateDiff Calendar
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 1925);
        startDate.set(Calendar.MONTH, 12);
        startDate.set(Calendar.DAY_OF_MONTH, 4);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, 1926);
        endDate.set(Calendar.MONTH, 12);
        endDate.set(Calendar.DAY_OF_MONTH, 3);

        Assert.assertEquals(DateHelper.getDateDiff(startDate, endDate, TimeUnit.DAYS), 364);
        Assert.assertEquals(DateHelper.getDateDiff(startDate, endDate, TimeUnit.HOURS), 8736);
        Assert.assertEquals(DateHelper.getDateDiff(startDate, endDate, TimeUnit.MINUTES), 524160);
        Assert.assertEquals(DateHelper.getDateDiff(startDate, endDate, TimeUnit.SECONDS), 31449600);

        //addDaysToDate
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1925);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 4);

        Date date = cal.getTime();

        Date dateAfter2Days = DateHelper.addDaysToDate(date, 372);

/*        Assert.assertEquals(dateAfter2Days.getYear(),26);
        Assert.assertEquals(dateAfter2Days.getMonth(),11);
        Assert.assertEquals(dateAfter2Days.getDay(),6);
        System.out.println(dateAfter2Days);
        System.out.println(date);*/


    }
}