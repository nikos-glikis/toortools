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
        Date start = new Date(25, 12 , 4);
        Date end = new Date(26, 12 , 3);
        Assert.assertEquals(DateHelper.getDateDiff(start, end, TimeUnit.DAYS),364);
        Assert.assertEquals(DateHelper.getDateDiff(start, end, TimeUnit.HOURS),8736);
        Assert.assertEquals(DateHelper.getDateDiff(start, end, TimeUnit.MINUTES),524160);
        Assert.assertEquals(DateHelper.getDateDiff(start, end, TimeUnit.SECONDS),31449600);

        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 1925);
        startDate.set(Calendar.MONTH, 12);
        startDate.set(Calendar.DAY_OF_MONTH, 4);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, 1926);
        endDate.set(Calendar.MONTH, 12);
        endDate.set(Calendar.DAY_OF_MONTH, 3);

        Assert.assertEquals(DateHelper.getDateDiff(startDate, endDate, TimeUnit.DAYS),364);
        Assert.assertEquals(DateHelper.getDateDiff(startDate, endDate, TimeUnit.HOURS),8736);
        Assert.assertEquals(DateHelper.getDateDiff(startDate, endDate, TimeUnit.MINUTES),524160);
        Assert.assertEquals(DateHelper.getDateDiff(startDate, endDate, TimeUnit.SECONDS),31449600);


    }
}