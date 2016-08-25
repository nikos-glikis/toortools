package com.object0r.toortools.os;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
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
            Exception e2 = new Exception("Some error happened when trying to run the command.");
            e.printStackTrace();
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
    }

    public static OsCommandOutput runRemoteCommand(String ip, String command, String user, String directory, String privateKeyPath) throws Exception
    {
        return runRemoteCommand(ip, 22, command, user, directory, privateKeyPath);
    }

    public static OsCommandOutput runRemoteCommand(String ip, int port, String command, String user, String directory, String privateKeyPath) throws Exception
    {
        OsCommandOutput osCommandOutput = new OsCommandOutput();
        String remoteCommand = "";

        if (directory != null)
        {

            remoteCommand += "cd " + directory + " ; ";
        }
        remoteCommand += command;

        JSch jsch = new JSch();
        Session session = jsch.getSession(user, ip, port);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        jsch.addIdentity(privateKeyPath);
        session.connect();
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(remoteCommand);

        channel.setInputStream(null);

        ((ChannelExec) channel).setErrStream(System.err);

        InputStream in = channel.getInputStream();

        channel.connect();

        byte[] tmp = new byte[1024];
        StringBuffer outputSb = new StringBuffer();

        while (true)
        {
            while (in.available() > 0)
            {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                //System.out.print(new String(tmp, 0, i));
                outputSb.append(new String(tmp, 0, i));
            }
            if (channel.isClosed())
            {
                if (in.available() > 0) continue;
                int exitStatus = channel.getExitStatus();
                osCommandOutput.setExitCode(exitStatus);
                if (exitStatus == 0)
                {
                    osCommandOutput.setStandardOutput(outputSb.toString());
                }
                else
                {
                    osCommandOutput.setErrorOutput(outputSb.toString());
                }
                break;
            }
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception ee)
            {
            }
        }
        channel.disconnect();
        session.disconnect();

        osCommandOutput.setExitCode(0);
        osCommandOutput.setErrorOutput(outputSb.toString());
        return osCommandOutput;
    }

    public static OsCommandOutput runCommandAndGetOutput(String command) throws Exception
    {
        try
        {
            String s = null;
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            StringBuilder sb = new StringBuilder();
            StringBuilder errorBuffer = new StringBuilder();
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null)
            {
                sb.append(s);
                //System.out.println(s);
            }

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null)
            {
                errorBuffer.append(s);
            }

            return new OsCommandOutput(sb.toString(), errorBuffer.toString());
        }
        catch (Exception e)
        {
            Exception e2 = new Exception("Some error happened when trying to run the command.");
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
     *
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
        return calendar.getTimeInMillis() / 1000;
    }


    static public Vector<String> getFoldersFilesRecursive(String path)
    {
        return getDirectoryContents(path, true, new Vector<String>());
    }

    static public Vector<String> getDirectoryContents(String path, boolean includeDirectories)
    {
        return getDirectoryContents(path, includeDirectories, new Vector<String>());
    }

    static private Vector<String> getDirectoryContents(String path, boolean includeDirectories, Vector<String> listSoFar)
    {
        if (listSoFar == null)
        {
            listSoFar = new Vector<String>();
        }
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return listSoFar;

        for (File f : list)
        {
            if (f.isDirectory())
            {
                if (includeDirectories)
                {
                    listSoFar.add(f.getAbsolutePath());
                }
                getDirectoryContents(f.getAbsolutePath(), includeDirectories, listSoFar);

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
     * @param folder the folder to get the files
     * @return A vector with all files
     */
    public static Vector<String> getFoldersFilesRecursiveOld(File folder)
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
                filenames.add((folder + "/" + fileEntry.getName()).replace("\\", "/"));
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
            cmd = "kill " + pid + " && sleep 5 && kill -9 " + pid;
        }
        try
        {
            Runtime.getRuntime().exec(cmd);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gives the process line by running:
     * ps aux | grep pid  for linux and
     * tasklist | findstr /c:" 56 " for windows
     *
     * @param pid
     * @return Windows: conhost.exe                   5652 Console                    1      5.500 K
     * Linux:
     */
    public static String getPidInformationLine(int pid)
    {
        String command = getPidInfoCommand(pid);
        try
        {
            OsCommandOutput osCommandOutput = OsHelper.runCommandAndGetOutput(command);
            return osCommandOutput.getStandardOutput().replace("PID TTY", "").replace("TIME CMD", "").trim();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isPidRunning(long pid) throws Exception
    {
        return isPidRunning(pid, 5, TimeUnit.SECONDS);
    }

    private static String getPidInfoCommand(long pid)
    {
        if (OS.isFamilyWindows())
        {
            //tasklist exit code is always 0. Parse output
            //findstr exit code 0 if found pid, 1 if it doesn't
            return "cmd /c \"tasklist /FI \"PID eq " + pid + "\" | findstr " + pid + "\"";
        }
        else
        {
            //ps exit code 0 if process exists, 1 if it doesn't
            return "ps -p " + pid;
        }
    }

    public static boolean isPidRunning(long pid, int timeout, TimeUnit timeunit) throws java.io.IOException
    {
        String line = getPidInfoCommand(pid);

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
