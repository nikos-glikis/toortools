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

public class IpApiComIpProvider extends AbstractIpProvider
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
                    .setUrl("http://ip-api.com/line/?fields=query")
                    .setProxy(proxy)
                    .setHeader("User-Agent", Utilities.getBrowserUserAgent());
            HttpResult httpResult = HTTP.request(httpRequestInformation);

            String ip = httpResult.getContentAsString().trim();

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
