package com.object0r.toortools.providers.ip.implementation;

import com.object0r.toortools.providers.InvalidProviderException;
import com.object0r.toortools.providers.ip.AbstractIpProvider;

import java.net.Proxy;

public class IcanhazipIpProvider extends AbstractIpProvider
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
            String ip=   readUrl("http://icanhazip.com/", proxy, connectTimeoutSeconds, readTimeoutSeconds);

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
            System.out.println(e);
            throw new InvalidProviderException(e.toString());
        }
    }
}
