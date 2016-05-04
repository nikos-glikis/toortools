package com.object0r.toortools.providers.ip;

import com.object0r.toortools.providers.InvalidProviderException;

import java.net.Proxy;

class IpifyIpProvider extends AbstractIpProvider
{
    @Override
    public String getIp(Proxy proxy) throws InvalidProviderException
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
