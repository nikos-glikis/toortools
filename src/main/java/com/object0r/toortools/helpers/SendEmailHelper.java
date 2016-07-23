package com.object0r.toortools.helpers;


import com.object0r.toortools.os.OsCommandOutput;
import com.object0r.toortools.os.OsHelper;

public class SendEmailHelper
{
    /**
     * Sends an email if you are on a Linux Server.
     * @param to
     * @param subject
     * @param body
     */
    public static void sendEmail(String to, String subject, String body)
    {
        try
        {
            String[] cmd = {
                    "/bin/sh",
                    "-c",
                    "echo \""+body.replace("\"","\\\"")+"\" | tee | mail -s \""+subject.replace("\"","\\\"")+"\" \""+to+"\""
            };
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println(cmd);
            //p.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
