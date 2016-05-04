package com.object0r.toortools.providers.ip;


import com.object0r.toortools.providers.InvalidProviderException;
import org.apache.commons.io.IOUtils;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class AbstractIpProvider implements IpProvider
{
    public String getIp() throws InvalidProviderException
    {
        return this.getIp(Proxy.NO_PROXY);
    }

    public String readUrl(String url, Proxy proxy) throws Exception
    {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(proxy);
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(15000);
        StringWriter writer = new StringWriter();
        IOUtils.copy(connection.getInputStream(), writer, "UTF-8");
        return writer.toString();
    }

    public static boolean isValidIp(String ip)
    {
        Pattern p = Pattern.compile("^\\d+\\.\\d+\\.\\d+\\.\\d+$");
        Matcher m = p.matcher(ip);

        if (ip.length() > 13 || ip.length() < 5 || !m.find())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
