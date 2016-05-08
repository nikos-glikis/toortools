package com.object0r.toortools.http;

import java.net.Proxy;
import java.util.HashMap;


public class HttpRequestInformation
{
    public final String METHODS_POST = "POST";
    public final String METHODS_GET = "GET";
    public final String METHODS_PUT = "PUT";
    public final String METHODS_DELETE = "DELETE";
    public final String METHODS_PATCH = "PATCH";
    public final String METHODS_LINK = "LINK";

    String method = METHODS_GET;
    String url;
    String body;
    Proxy proxy = Proxy.NO_PROXY;
    int timeoutSeconds = 0;
    HashMap<String, String> headers = new HashMap<String, String>();
    boolean followRedirects = true;

    public void setCookie(String cookie)
    {
        this.setHeader("Cookie", cookie.trim());
    }

    public int getTimeoutSeconds()
    {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds)
    {
        this.timeoutSeconds = timeoutSeconds;
    }

    public boolean hasTimeOut()
    {
        if (timeoutSeconds == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean isMethodPost()
    {
        return method.equals(METHODS_POST);
    }

    public boolean isMethodGet()
    {
        return method.equals(METHODS_GET);
    }

    public boolean isMethodDelete()
    {
        return method.equals(METHODS_DELETE);
    }

    public boolean isMethodPut()
    {
        return method.equals(METHODS_PUT);
    }

    public boolean isMethodPatch()
    {
        return method.equals(METHODS_PATCH);
    }

    public boolean isMethodLink()
    {
        return method.equals(METHODS_LINK);
    }

    public HttpRequestInformation setMethodPost()
    {
        this.method = METHODS_POST;
        return this;
    }

    public HttpRequestInformation setMethodGet()
    {
        this.method = METHODS_GET;
        return this;
    }

    public HttpRequestInformation setMethodPut()
    {
        this.method = METHODS_PUT;
        return this;
    }

    public HttpRequestInformation setMethodDelete()
    {
        this.method = METHODS_DELETE;
        return this;
    }

    public HttpRequestInformation setMethodPatch()
    {
        this.method = METHODS_PATCH;
        return this;
    }

    public HttpRequestInformation setMethodLink()
    {
        this.method = METHODS_LINK;
        return this;
    }

    public HttpRequestInformation setHeader(String key, String value)
    {
        headers.put(key, value);
        return this;
    }

    public HashMap<String, String> getHeaders()
    {
        return headers;
    }

    public String getHeader(String key)
    {
        return headers.get(key);
    }

    public String getUrl()
    {
        return url;
    }

    public HttpRequestInformation setUrl(String url)
    {
        this.url = url;
        return this;
    }

    public String getBody()
    {
        return body;
    }

    public HttpRequestInformation setBody(String body)
    {
        this.body = body;
        return this;
    }

    public boolean hasHeader(String key)
    {
        String header = headers.get(key);

        if (header == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Proxy getProxy()
    {
        return proxy;
    }

    public HttpRequestInformation setProxy(Proxy proxy)
    {
        this.proxy = proxy;
        return this;
    }

    public String getMethod()
    {
        return method;
    }

    public boolean isFollowRedirects()
    {
        return followRedirects;
    }

    public HttpRequestInformation setFollowRedirects(boolean followRedirects)
    {
        this.followRedirects = followRedirects;
        return this;
    }
}
