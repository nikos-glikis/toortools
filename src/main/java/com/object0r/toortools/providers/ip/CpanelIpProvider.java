package com.object0r.toortools.providers.ip;

import com.object0r.toortools.providers.InvalidProviderException;
import org.apache.commons.io.IOUtils;

import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

class CpanelIpProvider extends AbstractIpProvider
{
    @Override
    public String getIp(Proxy proxy) throws InvalidProviderException
    {
        return getIp(proxy, 15, 15);
    }

    @Override
    public String getIp(Proxy proxy, int connectTimeoutSeconds, int readTimeoutSeconds) throws InvalidProviderException
    {
        try
        {
            String ip=   readUrl("http://cpanel.com/showip.shtml", proxy, connectTimeoutSeconds, readTimeoutSeconds);

            if (isValidIp(ip))
            {
                return ip;
            }
            else
            {
                throw new InvalidProviderException("Ip Format not valid.");
            }
        }
        catch (Exception e)
        {
            throw new InvalidProviderException(e.toString());
        }
    }
}
