package com.object0r.toortools;

import com.object0r.toortools.http.HttpHelper;
import com.object0r.toortools.http.HttpRequestInformation;
import com.object0r.toortools.http.HttpResult;
import com.object0r.toortools.os.OsHelper;
import com.object0r.toortools.os.RecurringProcessHelper;


public class Main
{

    public static void main(String[] args)
    {
        Utilities.trustEverybody();
        HttpRequestInformation httpRequestInformation = new HttpRequestInformation();
        httpRequestInformation.setUrl("https://self-signed.badssl.com/");
        httpRequestInformation.setThrowExceptions(true);
        HttpResult httpResult = null;
        try
        {
            httpResult = HttpHelper.request(httpRequestInformation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(httpResult.getContentAsString());
        System.out.println(httpResult.getResponseCode());
        System.exit(0);
        /*
        test proxies
        try
        {
            for (int i = 0; i < 100; i++)
            {
                System.out.println(Utilities.getIp(Proxy.NO_PROXY));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

        RecurringProcessHelper.checkAndRun();

        try
        {
            OsHelper.killProcessByPid(RecurringProcessHelper.getPid());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Testssad");
        try
        {
            Thread.sleep(20000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("Tests");
        RecurringProcessHelper.checkAndRun();
    }
}
