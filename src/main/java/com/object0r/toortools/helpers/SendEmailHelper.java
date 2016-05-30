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
            OsCommandOutput osCommandOutput = OsHelper.runCommandAndGetOutput("echo \""+body.replace("\"","\\\"")+"\" | tee | mail -s \""+subject.replace("\"","\\\"")+"\" \""+to+"\"");
            System.out.println(osCommandOutput.getStandardOutput());
            System.out.println(osCommandOutput.getErrorOutput());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
