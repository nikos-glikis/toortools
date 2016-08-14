package com.object0r.toortools.os;

import com.object0r.toortools.Utilities;
import org.hyperic.sigar.Sigar;

import java.io.File;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.util.Calendar;

public class RecurringProcessHelper
{
    static String defaultSession = "default";
    static String defaultStateDirectory = "_state";
    static String pidF = "pid";
    static String timeF = "time";
    final static int SLEEP_BETWEEN_WRITES_SECONDS = 10;


    public static void checkAndRun()
    {
        checkAndRun(defaultSession, defaultStateDirectory);
    }

    public static void checkAndRun(String session)
    {
        checkAndRun(session, defaultStateDirectory);
    }

    public static void checkAndRun(String session, String stateDirectory)
    {
        if (checkIfRunning(session, stateDirectory))
        {
            System.out.println(session + " is already running.");
            System.exit(0);
        }
        markAsRunningThread(session, stateDirectory);
    }

    public static boolean checkIfRunning()
    {
        return checkIfRunning(defaultSession, defaultStateDirectory);
    }

    public static boolean checkIfRunning(String session)
    {
        return checkIfRunning(session, defaultStateDirectory);
    }

    public static boolean checkIfRunning(String session, String stateDirectory)
    {
        try
        {
            if (!new File(stateDirectory + "/" + session + "/").exists() || !new File(stateDirectory + "/" + session + "/").isDirectory())
            {
                return false;
            }
            else
            {
                String pidFile = stateDirectory + "/" + session + "/" + pidF;
                String timeFile = stateDirectory + "/" + session + "/" + timeF;
                if (!new File(pidFile).exists() || !new File(timeFile).exists())
                {
                    return false;
                }
                else
                {
                    //Both files exist.
                    try
                    {
                        String pidString = Utilities.readFile(pidFile);
                        if (pidString == null)
                        {
                            return false;
                        }
                        if (pidString.equals(""))
                        {
                            return false;
                        }
                        long pid = Long.parseLong(pidString.trim());
                        if (OsHelper.isPidRunning(pid))
                        {
                            //check for timeout
                            String timestampString = Utilities.readFile(timeFile);
                            long timestamp = Long.parseLong(timestampString);

                            if (OsHelper.getTimestampSeconds() < timestamp)
                            {
                                System.out.println("Current timestamp is less than saved one, possibly a bug.");
                                return false;
                            }
                            else
                            {
                                if (timestamp + SLEEP_BETWEEN_WRITES_SECONDS * 5 > OsHelper.getTimestampSeconds())
                                {
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    //Mark as running.
    public static void markAsRunning()
    {
        markAsRunning(defaultSession, defaultStateDirectory);
    }

    public static void markAsRunning(String session)
    {
        markAsRunning(session, defaultStateDirectory);
    }

    public static void markAsRunningThread()
    {
        markAsRunningThread(defaultSession, defaultStateDirectory);
    }

    public static void markAsRunningThread(String session)
    {
        markAsRunningThread(session, defaultStateDirectory);
    }

    public static void markAsRunningThread(final String session, final String stateDirectory)
    {
        new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        markAsRunning(session, stateDirectory);
                        Thread.sleep(SLEEP_BETWEEN_WRITES_SECONDS * 1000);
                        System.out.println("Thread Count: " + java.lang.Thread.activeCount());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        try
        {
            Thread.sleep(2000);
        }
        catch (Exception e)
        {
        }
    }

    public static void markAsRunning(String session, String stateDirectory)
    {
        try
        {
            long pid = getPid();

            //System.out.println("Pid is: " + pid);
            String directory = stateDirectory + "/" + session + "/";
            if (new File(directory).exists() && !new File(directory).isDirectory())
            {
                System.out.println("pid file exists but not a directory");
                System.exit(0);
            }

            if (!new File(directory).exists())
            {
                new File(directory).mkdirs();
            }

            PrintWriter pr = new PrintWriter(directory + pidF);
            pr.print(pid);
            pr.close();

            pr = new PrintWriter(directory + timeF);
            pr.print(OsHelper.getTimestampSeconds());
            pr.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void exitAfterSeconds(final long sleepSeconds)
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(sleepSeconds * 1000);
                    System.out.println("Exiting because " + sleepSeconds + " have passed.");
                    System.exit(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
        ;
    }

    public static long getPid() throws Exception
    {
        // Note: may fail in some JVM implementations
        // therefore fallback has to be provided

        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');

        if (index < 1)
        {
            // part before '@' empty (index = 0) / '@' not found (index = -1)
            throw new Exception("Error with pid detection");
        }

        try
        {
            return Long.parseLong(jvmName.substring(0, index));
        }
        catch (NumberFormatException e)
        {
            // ignore
            throw new Exception("Error with pid detection");
        }
    }
}


