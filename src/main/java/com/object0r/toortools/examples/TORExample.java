package com.object0r.toortools.examples;

import com.object0r.toortools.Utilities;
import com.object0r.toortools.tor.TorHelper;

public class TORExample
{

    public static void main(String[] args)
    {
        //torrify
        /*TorHelper.torify();
        TorHelper.restartTor();
        TorHelper.changeIp();
        System.out.println(Utilities.getIp());
        */

        //torrify, offset, 1-10000
        TorHelper.torify(25);
        TorHelper.restartTor();
        TorHelper.changeIp();
        System.out.println(Utilities.getIp());


    }
}
