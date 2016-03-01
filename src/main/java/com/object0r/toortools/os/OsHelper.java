package com.object0r.toortools.os;

import org.apache.commons.exec.*;

import java.io.*;
import java.sql.Time;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 25/4/2015.
 */
public class OsHelper
{
    static int OS_TYPES_UNKNOWN = 0;
    static int OS_TYPES_WINDOWS = 1;
    static int OS_TYPES_LINUX = 2;

    static int getOs()
    {
        String os = System.getProperty("os.name");

        if (os.indexOf("Windows") > -1)
        {
            return OS_TYPES_WINDOWS;
        }
        else if (os.indexOf("Linux") > -1)
        {

            return OS_TYPES_LINUX;
        }
        else
        {
            return OS_TYPES_UNKNOWN;
        }
    }

    public static boolean isWindows()
    {
        return OsHelper.getOs() == OS_TYPES_WINDOWS;
    }

    public static boolean isLinux()
    {
        return OsHelper.getOs() == OS_TYPES_LINUX;
    }

    public static boolean runCommand(String command) throws Exception
    {
        try
        {
            String s = null;
            //System.out.println(command);
            Process p = Runtime.getRuntime().exec(command);

            return true;


        }
        catch (Exception e)
        {
            Exception e2 =new  Exception("Some error happened when trying to run the command.");
            e.printStackTrace();
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }

    }

    public static OsCommandOutput runCommandAndGetOutput(String command) throws Exception
    {
        try
        {
            String s = null;
            Process p = Runtime.getRuntime().exec(command);

            /*BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            StringBuffer sb = new StringBuffer();
            StringBuffer errorBuffer = new StringBuffer();
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
                //System.out.println(s);
            }

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                errorBuffer.append(s);
            }
            */
            return new OsCommandOutput("", "");


        }
        catch (Exception e)
        {
            Exception e2 =new  Exception("Some error happened when trying to run the command.");
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
    }


    public static void deleteFolderContentsRecursive(File folder) throws Exception
    {
        Vector<String> filenames = getDirectoryContents(folder.getAbsolutePath(), true);

        for (String filename : filenames)
        {
            new File(filename).delete();
        }
    }

    /**
     * Deletes all files in directory. Skips folders.
     * @param folder
     * @throws Exception
     */
    public static void deleteFolderContentsRecursive(File folder, boolean includeDirectories) throws Exception
    {
        Vector<String> filenames = getDirectoryContents(folder.getAbsolutePath(), includeDirectories);

        for (String filename : filenames)
        {
            new File(filename).delete();
        }
    }

    public static long getTimestampSeconds()
    {
        return getTimestamp();
    }

    public static long getTimestamp()
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis()/1000;
    }


    static public Vector<String> getFoldersFilesRecursive(String path)
    {
        return getDirectoryContents(path, true, new Vector<String>());
    }

    static public Vector<String> getDirectoryContents(String path, boolean includeDirectories)
    {
        return getDirectoryContents(path, includeDirectories, new Vector<String>());
    }

    static private Vector<String> getDirectoryContents( String path, boolean includeDirectories, Vector<String> listSoFar )
    {
        if (listSoFar == null)
        {
            listSoFar = new Vector<String>();
        }
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return listSoFar;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                if (includeDirectories)
                {
                    listSoFar.add(f.getAbsolutePath());
                }
                getDirectoryContents( f.getAbsolutePath(), includeDirectories, listSoFar);

            }
            else
            {
                listSoFar.add(f.getAbsolutePath());

            }
        }
        return listSoFar;
    }

    /**
     * Returns a list of all files in that folder.
     * Doesn't return folders.
     *
     * @param folder
     * @return
     */
    public static  Vector<String> getFoldersFilesRecursiveOld(File folder)
    {
        Vector<String> filenames = new Vector<String>();
        filenames = getFoldersFileRecursive(folder, filenames);
        return filenames;
    }

    static private Vector<String> getFoldersFileRecursive(File folder, Vector<String> filenames)
    {
        for (final File fileEntry : folder.listFiles())
        {
            if (fileEntry.isDirectory())
            {
                filenames = getFoldersFileRecursive(fileEntry, filenames);
            }
            else
            {
                filenames.add( (folder+"/"+fileEntry.getName() ).replace("\\","/"));
            }
        }
        return filenames;
    }

    public static void killProcessByPid(long pid)
    {
        String cmd = null;
        if (OsHelper.isWindows())
        {
            cmd = "taskkill /F /PID " + pid;
        }
        else
        {
            cmd = "kill -9 "+pid;
        }
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean isPidRunning(long pid) throws Exception
    {
        return isPidRunning(pid, 5, TimeUnit.SECONDS);
    }

    public static boolean isPidRunning(long pid, int timeout, TimeUnit timeunit) throws java.io.IOException
    {
        String line;
        if (OS.isFamilyWindows())
        {
            //tasklist exit code is always 0. Parse output
            //findstr exit code 0 if found pid, 1 if it doesn't
            line = "cmd /c \"tasklist /FI \"PID eq " + pid + "\" | findstr " + pid + "\"";
        }
        else
        {
            //ps exit code 0 if process exists, 1 if it doesn't
            line = "ps -p " + pid;
        }
        CommandLine cmdLine = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        // disable logging of stdout/strderr
        executor.setStreamHandler(new PumpStreamHandler(null, null, null));
        // disable exception for valid exit values
        executor.setExitValues(new int[]{0, 1});
        // set timer for zombie process
        ExecuteWatchdog timeoutWatchdog = new ExecuteWatchdog(timeunit.toMillis(timeout));
        executor.setWatchdog(timeoutWatchdog);
        int exitValue = executor.execute(cmdLine);
        // 0 is the default exit code which means the process exists
        return exitValue == 0;
    }

}
