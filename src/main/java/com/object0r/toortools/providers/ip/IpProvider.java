package com.object0r.toortools.providers.ip;


import java.net.Proxy;

public interface IpProvider
{
    public String getIp(Proxy proxy) throws com.object0r.toortools.providers.InvalidProviderException;
}
