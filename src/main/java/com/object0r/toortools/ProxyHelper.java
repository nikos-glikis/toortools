package com.object0r.toortools;

import com.object0r.toortools.tor.TorHelper;

/**
 * Created by User on 18/4/2015.
 */
public class ProxyHelper
{
    static public void setGlobalProxy(String host, int port)
    {
        try
        {
            System.setProperty("proxySet", "true");
            System.setProperty("socksProxyHost", host);
            System.setProperty("socksProxyPort", new Integer(port).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean torify()
    {
        return TorHelper.torify();
    }

}
