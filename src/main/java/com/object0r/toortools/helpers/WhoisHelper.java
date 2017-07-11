package com.object0r.toortools.helpers;

import com.object0r.toortools.Utilities;
import com.object0r.toortools.os.OsCommandOutput;
import com.object0r.toortools.os.OsHelper;

public class WhoisHelper
{
    public static String getWhoIs(String domain) throws Exception
    {
        try
        {
            OsCommandOutput osCommandOutput;
            String command = "";

            if (OsHelper.isWindows())
            {
                command = "whois.exe " + domain;
            }
            else
            {
                command = "whois " + domain;
            }
            osCommandOutput = OsHelper.runCommandAndGetOutput(command);
            return osCommandOutput.getStandardOutput();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static int getRenewalYear(String domain)
    {
        try
        {
            String page = getWhoIs(domain);
            if (page.contains("Updated Date: "))
            {
                return Integer.parseInt(Utilities.cut("Updated Date: ", "-", page));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;

    }
}
