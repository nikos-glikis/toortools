package com.object0r.toortools.providers.ip;

import com.object0r.toortools.providers.ip.implementation.CpanelIpProvider;
import com.object0r.toortools.providers.ip.implementation.IpifyIpProvider;

import java.util.Vector;

public class IpProvidersHelper
{
    static Vector<IpProvider> ipProviders = null;

    public static Vector<IpProvider> getIpProviders()
    {
        if (ipProviders ==null)
        {
            ipProviders = new Vector<IpProvider>();
            ipProviders.add(new CpanelIpProvider());
            ipProviders.add(new IpifyIpProvider());
        }
        return ipProviders;
    }
}
