package com.object0r.toortools;


import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    String logFile;

    int VERBOSE_LEVEL;

    public Logger(String logFile, int VERBOSE_LEVEL)
    {
        this.VERBOSE_LEVEL = VERBOSE_LEVEL;
        this.logFile = logFile;
    }
    public synchronized void add(String message, boolean print, int level)
    {
        try
        {
            PrintWriter pr = new PrintWriter(new FileOutputStream(logFile, true));
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String dateTime = dateFormat.format(date); //2014/08/06 15:59:48
            String out = "[*] "+dateTime + "\n      "+message;
            pr.println(out);
            pr.close();

            if (print)
            {
                consolePrint(out, level);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void consolePrint(Object object, int level)
    {
        if (level <= VERBOSE_LEVEL)
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String dateTime = dateFormat.format(date); //2014/08/06 15:59:48
            System.out.println(object.toString());
        }
    }

}