package com.toortools;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * Created by User on 3/5/2015.
 */
public abstract class UsernamesCollector extends CountSpider
{
    public void init()
    {
        createUsernamesDatabase();
        createGetNexter();
    }
    public static DB usernames ;
    public void createUsernamesDatabase()
    {
        usernames = createNewDatabase("usernames");
    }

    public void exportUsernamesToTxt()
    {
        exportUsernamesToTxt("sessions/"+SESSION_NAME+"/usernames.txt");
    }

    void exportUsernamesToTxt(String filename)
    {
        createUsernamesDatabase();
        try
        {
            Vector<String> values = usernames.getAllKeys();
            PrintWriter pr = new PrintWriter(new FileOutputStream(filename));
            for(int i = 0; i<values.size(); i++)
            {
                pr.println(values.get(i));
            }
            pr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
