package com.toortools;

import com.toortools.tor.TorHelper;
import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;

public class Main
{

    public static void main(String[] args)
    {
        TorHelper.torify(29);
        TorHelper.restartTor();
        TorHelper.changeIp();

        System.out.println(Utilities.getIp());
        FTPClient client = new FTPClient();
        try
        {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // write your code here
    }
}
