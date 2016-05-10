package com.object0r.toortools;

import com.object0r.toortools.os.OsHelper;
import com.object0r.toortools.os.RecurringProcessHelper;
import com.object0r.toortools.tor.TorHelper;
import it.sauronsoftware.ftp4j.FTPClient;

import java.net.Proxy;

public class Main
{

    public static void main(String[] args)
    {
        System.out.println(OsHelper.getPidInformationLine(9308));
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
