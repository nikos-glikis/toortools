package com.object0r.toortools.providers.ip;

import com.object0r.toortools.providers.ip.implementation.*;

import java.util.Vector;

public class IpProvidersHelper
{
    static Vector<IpProvider> ipProviders = null;

    public static Vector<IpProvider> getIpProviders()
    {
        if (ipProviders ==null)
        {
            //TODO create tests for these.
            ipProviders = new Vector<IpProvider>();
            ipProviders.add(new CpanelIpProvider());
            ipProviders.add(new IcanhazipIpProvider());
            ipProviders.add(new IpifyIpProvider());
            ipProviders.add(new DuckduckgoIpProvider());
            ipProviders.add(new IpofinIpProvider());
            ipProviders.add(new WhatismyipaddresscomBotIpProvider());
            ipProviders.add(new MyExternalIpIpProvider());
            ipProviders.add(new DnsomaticIpIpProvider());
            ipProviders.add(new WhatIsMyIpIpProvider());
            ipProviders.add(new IpEchoIpProvider());
            ipProviders.add(new AmazonAwsIpProvider());





            //doesn't work with tor
            //ipProviders.add(new IpApiComIpProvider());
            //slow
            //ipProviders.add(new CheckIpDynDnsIpProvider());
        }
        return ipProviders;
    }
}
