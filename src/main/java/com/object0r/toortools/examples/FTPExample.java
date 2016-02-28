package com.object0r.toortools.examples;

import it.sauronsoftware.ftp4j.FTPClient;
import java.io.File;

public class FTPExample
{
    public static void main(String[] args)
    {
        FTPClient client = new FTPClient();
        try
        {
            client.connect("domain");
            client.login("username", "password");
            client.noop();
            client.changeDirectory("/remote/dir");
            client.upload(new File("pom.xml"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
