package com.object0r.toortools.providers.ip.implementation;

import com.object0r.toortools.Utilities;
import com.object0r.toortools.http.HTTP;
import com.object0r.toortools.http.HttpRequestInformation;
import com.object0r.toortools.http.HttpResult;
import com.object0r.toortools.providers.InvalidProviderException;
import com.object0r.toortools.providers.ip.AbstractIpProvider;

import java.net.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhatIsMyIpIpProvider extends AbstractIpProvider
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

            HttpRequestInformation httpRequestInformation = new HttpRequestInformation();
            httpRequestInformation
                    .setUrl("http://whatismyip.org/")
                    .setProxy(proxy)
                    .setHeader("User-Agent", Utilities.getBrowserUserAgent());
            HttpResult httpResult = HTTP.request(httpRequestInformation);
            String page = httpResult.getContentAsString();

            Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
            Matcher m = p.matcher(page);
            if (m.find())
            {
                String ip = m.group().trim();

                if (isValidIp(ip))
                {
                    return ip;
                }
                else
                {
                    throw new InvalidProviderException("Ip Format not valid.");
                }
            }
            else
            {
                throw new InvalidProviderException("Ip not found in body.");
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            throw new InvalidProviderException(e.toString());
        }
    }
}
