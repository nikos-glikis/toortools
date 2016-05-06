package com.object0r.toortools.providers.ip.implementation;

import com.object0r.toortools.providers.InvalidProviderException;
import com.object0r.toortools.providers.ip.AbstractIpProvider;

import java.net.Proxy;

public class IpifyIpProvider extends AbstractIpProvider
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
            String ip =   readUrl("https://api.ipify.org/?format=txt", proxy);

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
