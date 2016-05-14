package com.object0r.toortools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DB
{
    private final String dbName;
    Connection dBconnection, dBconnection2;
    private String SESSION_NAME;

    public synchronized void put(Object key, Object value)
    {
        put(key, value, 0);
    }

    public synchronized void put(Object key, Object value, int tries)
    {
        try
        {
            Statement st = dBconnection.createStatement();
            try
            {
                st.execute("INSERT INTO `values` VALUES ('" + key.toString().replace("'", "''") + "', '" + value.toString().replace("'", "''") + "')");
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                //System.exit(0);
                st.close();
                st = dBconnection.createStatement();
            }


            st.execute("UPDATE `values` SET `value` = '" + value.toString().replace("'", "''") + "' WHERE  `key` = '" + key.toString().replace("'", "''") + "'");
            st.close();
            st = dBconnection2.createStatement();

            try
            {

                st.execute("INSERT INTO `values` VALUES ('" + key.toString().replace("'", "''") + "', '" + value.toString().replace("'", "''") + "')");

            }
            catch (Exception e)
            {
                //e.printStackTrace();
                //System.exit(0);
                ;
                st.close();
                st = dBconnection.createStatement();
            }

            st.execute("UPDATE `values` SET `value` = '" + value.toString().replace("'", "''") + "' WHERE  `key` = '" + key.toString().replace("'", "''") + "'");
            st.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();

            if (tries == 0)
            {
                initializeConnections();
                put(key, value, tries + 1);
            }
            else
            {
                System.exit(0);
            }
        }
    }

    public void exportKeysToFile(String filename)
    {
        try
        {

            PrintWriter pr = new PrintWriter(filename);

            HashMap<String, String> allBrands = this.getAll();
            Statement st = dBconnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT `key`,`value` FROM  `values` ");
            while (rs.next())
            {
                pr.println(rs.getString(1));

            }
            pr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void exportValuesToFile(String filename)
    {
        try
        {

            PrintWriter pr = new PrintWriter(filename);

            HashMap<String, String> allBrands = this.getAll();
            Statement st = dBconnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT `key`,`value` FROM  `values` ");
            while (rs.next())
            {
                pr.println(rs.getString(2));
            }
            pr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Vector<String> getAllKeys()
    {
        Vector<String> values = new Vector<String>();
        try
        {
            Statement st = dBconnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT `key` FROM `values`");
            while (rs.next())
            {
                values.add(rs.getString(1));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return values;
    }

    public synchronized String get(int key)
    {
        return this.get(new Integer(key).toString());
    }

    public synchronized String get(String key)
    {
        String value = null;
        Statement st = null;
        try
        {
            st = dBconnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT `value` FROM `values` WHERE `key` = '" + key.replace("'", "''") + "'");

            if (rs.next())
            {
                value = rs.getString(1);
            }
            rs.close();
            st.close();
        }
        catch (Exception e)
        {
            try
            {
                if (st != null && !st.isClosed())
                {
                    st.close();
                }
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }

            e.printStackTrace();
        }
        return value;
    }

    public DB()
    {
        this("DefaultSession", "DefaultDb");
    }

    public synchronized HashMap<String, String> getAll()
    {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Statement st = null;
        try
        {
            st = dBconnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM  `values` ");
            while (rs.next())
            {
                hashMap.put(rs.getString(1), rs.getString(2));
            }
            rs.close();
            st.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                if (st != null && !st.isClosed())
                {
                    st.close();
                }
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
        }
        return hashMap;
    }

    public DB(String SESSION_NAME, String name)
    {
        this.SESSION_NAME = SESSION_NAME;
        this.dbName = name;
        Statement st = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            if (new File("sessions/" + SESSION_NAME + "/databases/" + name).exists() && new File("sessions/" + SESSION_NAME + "/databases/" + name).isDirectory())
            {

            }
            else
            {
                new File("sessions/" + SESSION_NAME + "/databases/" + name).mkdirs();
                new File("sessions/" + SESSION_NAME + "/databases_bak/" + name).mkdirs();
            }

            initializeConnections();

            st = dBconnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='values';");
            if (rs.next())
            {
                if (rs.getInt(1) == 0)
                {
                    st.execute("create table `values`(`key` varchar(50), `value` varchar(50));");
                    st.execute("CREATE UNIQUE INDEX `key` ON `values`(`key`)");

                }
            }

            //db2
            st = dBconnection2.createStatement();
            rs = st.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='values';");
            if (rs.next())
            {
                if (rs.getInt(1) == 0)
                {
                    st.execute("create table `values`(`key` varchar(50), `value` varchar(50));");
                    st.execute("CREATE UNIQUE INDEX `key` ON `values`(`key`)");

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                if (st != null && !st.isClosed())
                {
                    st.close();
                }
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            System.exit(0);
        }
    }

    private void initializeConnections()
    {
        try
        {

            if (dBconnection != null && !dBconnection.isClosed())
            {
                dBconnection.close();
            }
            if (dBconnection2 != null && !dBconnection2.isClosed())
            {
                dBconnection2.close();
            }

            dBconnection = DriverManager.getConnection("jdbc:sqlite:sessions/" + SESSION_NAME + "/databases/" + dbName + "/" + dbName + ".db");
            dBconnection2 = DriverManager.getConnection("jdbc:sqlite:sessions/" + SESSION_NAME + "/databases_bak/" + dbName + "/" + dbName + ".db");
            dBconnection.setAutoCommit(true);
            dBconnection2.setAutoCommit(true);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}