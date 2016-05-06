package com.object0r.toortools.providers.ip.implementation;

import com.object0r.toortools.providers.InvalidProviderException;
import com.object0r.toortools.providers.ip.AbstractIpProvider;

import java.net.Proxy;

public class MyExternalIpIpProvider extends AbstractIpProvider
{
    @Override
    public String getIp(Proxy proxy) throws InvalidProviderException
    {
        return getIp(proxy, defaultConnectTimeout, defaultReadTimeout);
    }

    @Override
    public String getIp(Proxy proxy, int connectTimeoutSeconds, int readTimeoutSeconds) throws InvalidProviderException
    {
        try
        {
            String ip =   readUrl("http://myexternalip.com/raw", proxy);

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
