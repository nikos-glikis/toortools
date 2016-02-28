package com.object0r.toortools.tor;

import com.object0r.toortools.ProxyHelper;
import com.object0r.toortools.Utilities;
import com.object0r.toortools.os.OsHelper;

public class TorHelper
{

    static TorConnection torConnection;
    //to provide unique tor for each process
    static int defaultPortOffset = 0;

    static void dieWithError(String error)
    {
        System.out.println(error);
        System.exit(0);
    }

    /**
     * Sets the Tor proxy settings. In case of error (or if the external ip doesn't change) false is returned.
     * @param dieOnError - If true, when the ip doesn't change it then the system exits.
     * @return
     */
    public static boolean torifySimple(boolean dieOnError)
    {
        try
        {
            String oldIp = Utilities.getIp();


            if (oldIp == null)
            {
                if (dieOnError)
                {
                    dieWithError("I can't verify old ip.");
                }
                return false;
            }
            System.out.println("Old Ip: " + oldIp);

            System.setProperty("proxySet", "true");
            System.setProperty("socksProxyHost", "localhost");
            System.setProperty("socksProxyPort", "9050");

            String newIp = Utilities.getIp();
            if (newIp  == null)
            {
                if (dieOnError)
                {
                    dieWithError("I can't verify new ip.");
                }
                return false;
            }

            System.out.println("New Ip: "+newIp);

            if (oldIp.equals(newIp))
            {
                if (dieOnError)
                {
                    dieWithError("Old ip is the same as new ip.");
                }
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (dieOnError)
        {
            dieWithError("I can't verify old ip.");
        }
        return false;
    }

    static public boolean torify( )
    {
        return torify(defaultPortOffset);
    }
    static public boolean torify( int portOffset )
    {
        try
        {
            if (torConnection == null)
            {
                torConnection = new TorConnection(portOffset);
            }
            String oldIp = Utilities.getIp();

            if (oldIp == null)
            {
                return false;
            }
            System.out.println("Old Ip: " + oldIp);
            //System.out.println("Socks port: "+ torConnection.getSocksPort());
            ProxyHelper.setGlobalProxy("localhost", torConnection.getSocksPort());

            String newIp = Utilities.getIp();
            if (newIp  == null)
            {
                return false;
            }

            System.out.println("New Ip: "+newIp);

            if (oldIp.equals(newIp))
            {
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static void changeIp()
    {
        torConnection.changeIp();
    }

    public static boolean restartTor()
    {
        try
        {
            if (OsHelper.isWindows())
            {
                OsHelper.runCommand("net stop tor");
                OsHelper.runCommand("net start tor");
            }
            else if (OsHelper.isLinux())
            {
                OsHelper.runCommand("service tor restart");
            }
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
