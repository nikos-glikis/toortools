package com.object0r.toortools.helpers;


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
            OsHelper.runCommand("echo \""+body.replace("\"","\\\"")+"\" | tee | mail -s \""+subject.replace("\"","\\\"")+"\" \""+to+"\"");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
