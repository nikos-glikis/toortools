package com.object0r.toortools;

abstract public class CountSpider extends Thread
{

    //must override in Constructor
    public static int THREAD_COUNT ;
    public static int SAVE_EVERY ;
    public static boolean SAVE_EVERY_PRINT_COUNTER ;
    public static String SESSION_NAME ;
    public static int START_FROM ;
    public static boolean INCREASING ;

    public static getNexter nexter;
    static DB db;

    public CountSpider()
    {
        try
        {
            THREAD_COUNT = 10;
            SAVE_EVERY = 1000;
            SAVE_EVERY_PRINT_COUNTER = true;
            SESSION_NAME = "default";
            START_FROM = 0;
            INCREASING = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public DB createNewDatabase(String name)
    {
        return new DB(SESSION_NAME, name);
    }

    public getNexter createGetNexter()
    {
        nexter = new getNexter();
        return nexter;
    }
    public class getNexter extends  BaseGetNexter
    {
        void loadLatestDone()
        {
            counter = START_FROM;
        }

        public getNexter()
        {
            try
            {
                try
                {
                    if (db==null)
                    {
                        db = createNewDatabase("db");
                    }
                    String ret = db.get("latest");

                    if (ret!= null)
                    {
                        counter = Integer.parseInt(ret);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        long counter = START_FROM;
        boolean increasing = INCREASING;
        synchronized Long getNext()
        {
            if (counter % SAVE_EVERY ==0)
            {
                save();
                if (SAVE_EVERY_PRINT_COUNTER)
                {
                    System.out.println("Counter: "+counter);
                }
            }
            if (INCREASING )
            {
                return counter++;
            }
            else
            {
                return counter--;
            }
        }
        void save()
        {
            try
            {
                db.put("latest",counter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void run()
    {
        try
        {
            while (true)
            {
                process(nexter.getNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public abstract void process(long count);
}
