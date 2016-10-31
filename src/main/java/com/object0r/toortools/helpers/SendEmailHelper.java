package com.object0r.toortools.helpers;


import com.object0r.toortools.Utilities;
import com.object0r.toortools.os.OsCommandOutput;
import com.object0r.toortools.os.OsHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.or.ThreadGroupRenderer;
import org.fusesource.leveldbjni.internal.Util;

import java.io.File;

public class SendEmailHelper
{
    /**
     * Sends an email if you are on a Linux Server.
     *
     * @param to
     * @param subject
     * @param body
     */
    public static void sendEmail(String to, String subject, String body)
    {
        try
        {
            String tmpDir = "/tmp/toortools_mail/";
            if (!new File(tmpDir).exists())
            {
                new File(tmpDir).mkdirs();
            }
            String tmpFile = tmpDir + RandomStringUtils.randomAlphanumeric(8);
            Utilities.writeStringToFile(tmpFile, body);
            sendEmail(to, subject, new File(tmpFile));
            new File(tmpFile).delete();
            //System.out.println(cmd[3]);
            //p.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void sendEmail(String to, String subject, File bodyFile)
    {
        try
        {
            if (!bodyFile.exists())
            {
                throw new Exception("File: " + bodyFile + " does not exist.");
            }
            String[] cmd = {
                    "/bin/sh",
                    "-c",
                    //"echo \""+body.replace("\"","\\\"")+"\" | tee | mail -s \""+subject.replace("\"","\\\"").replace("\n"," ")+"\" \""+to+"\""
                    "cat " + bodyFile.getAbsolutePath() + " | tee | mail -s \"" + subject.replace("\"", "\\\"").replace("\n", " ") + "\" \"" + to + "\""
            };
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            //System.out.println(cmd[3]);
            //p.waitFor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
