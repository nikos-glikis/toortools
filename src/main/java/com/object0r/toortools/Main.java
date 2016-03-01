package com.object0r.toortools;

import com.object0r.toortools.os.OsHelper;
import com.object0r.toortools.os.RecurringProcessHelper;
import com.object0r.toortools.tor.TorHelper;
import it.sauronsoftware.ftp4j.FTPClient;

public class Main
{

    public static void main(String[] args)
    {
        RecurringProcessHelper.checkAndRun();
        try {
            OsHelper.killProcessByPid(RecurringProcessHelper.getPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Testssad");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Tests");
        RecurringProcessHelper.checkAndRun();
    }
}
