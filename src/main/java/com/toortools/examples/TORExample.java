package com.toortools.examples;

import com.toortools.Utilities;
import com.toortools.tor.TorHelper;

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
