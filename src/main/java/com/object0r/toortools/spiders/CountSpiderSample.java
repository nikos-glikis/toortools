package com.object0r.toortools.spiders;
import com.object0r.toortools.CountSpider;

public class CountSpiderSample extends CountSpider
{
    static int THREAD_COUNT = 100;
    static String SESSION_NAME = "spiderSample";
    CountSpiderSample()
    {
        super();
    }

    String getSessionName()
    {
        return "sample";
    }

    public static void main(String args[])
    {
        try
        {
            new CountSpiderSample().createGetNexter();
            for (int i = 0; i<THREAD_COUNT; i++)
            {
                new CountSpiderSample().start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void process(long count)
    {
        try
        {
            System.out.println(count);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
