package com.object0r.toortools.db;

import org.apache.commons.io.FileUtils;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import static org.fusesource.leveldbjni.JniDBFactory.factory;

public class LevelDb extends AbstractKeyValueDatabase
{
    DB db;
    String DATABASES_PATH = "dbs";
    String filename;
    public LevelDb(String filename)
    {
        this.filename = filename;
        init(filename);
    }

    public LevelDb(String filename, boolean destroy)
    {
        this.filename = filename;
        if (destroy)
        {
            destroy();
        }
        init(filename);
    }

    public void reset()
    {
        this.close();
        destroy();
        init(filename);
    }

    public LevelDb(String directory, String filename, boolean destroy)
    {
        this.filename = filename;
        this.DATABASES_PATH = directory;
        if (destroy)
        {
            destroy();
        }
        init(filename);
    }

    void destroy()
    {
        try
        {
            FileUtils.deleteDirectory(new File(DATABASES_PATH+"/"+filename));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void deleteAll()
    {
        //TODO
    }
    void init(String filename)
    {
        try
        {
            if (!new File(DATABASES_PATH+"/"+filename).isDirectory()) {
                new File(DATABASES_PATH+"/"+filename).mkdirs();
            }
            Options options = new Options();

            options.createIfMissing(true);

            try {

                this.db = factory.open(new File(DATABASES_PATH+"/"+filename), options);

                // Use the db in here....
                    /*System.out.println("db test");
                    db.put(bytes("Tampanew"), bytes("rocks sadsad!! new"));
                    String value = asString(db.get(bytes("Tampa")));
                    System.out.println(value);
                    value = asString(db.get(bytes("Tampanew")));
                    System.out.println(value);
                    System.exit(0);*/

            } finally {
                // Make sure you close the db to shutdown the
                // database and avoid resource leaks.
                //db.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void put(String key, String value)
    {

        this.db.put(bytes(key.toLowerCase()), bytes(value));
    }

    public String getAsString(String key)
    {
        return asString(get(key.toLowerCase()));
    }

    public Map.Entry<String, byte[]> pop() throws Exception
    {
        DBIterator iterator = db.iterator();
        KeyValueDatabaseEntry<String, byte[]> popValue = null;
        try
        {
            int i = 1;
            iterator.seekToFirst();
            Map.Entry<byte[], byte[]> entry = iterator.next();
            if (entry == null) {
                throw new Exception("No value to pop.");
            }
            String key = asString(entry.getKey());
            byte []value = (entry.getValue());
            popValue = new  KeyValueDatabaseEntry<String, byte[]>(key , value);
            delete(key);
            try { iterator.close(); } catch (Exception e) { e.printStackTrace(); }

        } finally
        {
            try { iterator.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return popValue;
    }

    public String popValueAsString() throws Exception
    {
        Map.Entry<String, byte[]> popValue = pop();
        Map.Entry<String,String> popValueString = new KeyValueDatabaseEntry<String,String>(popValue.getKey(), asString(popValue.getValue()));
        return popValueString.getValue();
    }

    public String popKeyAsString() throws Exception
    {
        Map.Entry<String, byte[]> popValue = pop();
        Map.Entry<String,String> popValueString = new KeyValueDatabaseEntry<String,String>(popValue.getKey(), asString(popValue.getValue()));
        return popValueString.getKey();
    }

    public byte[] get(String key)
    {
        return this.db.get(bytes(key.toLowerCase()));
    }
    public  void close()
    {
        try
        {
            this.db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public  void delete(String key)
    {
        try
        {
            this.db.delete(key.toLowerCase().getBytes());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void copyEntriesTo(KeyValueDatabaseInterface destination)
    {
        try
        {
            DBIterator iterator = db.iterator();
            try {
                int i = 1;

                HashMap map = new HashMap();
                for(iterator.seekToFirst(); iterator.hasNext(); iterator.next())
                {
                    String key = asString(iterator.peekNext().getKey());
                    String value = asString(iterator.peekNext().getValue());
                    map.put(key,value);
                    if (i++ % 100000 == 0)
                    {
                        destination.batchPut(map);
                        map = new HashMap();
                        System.out.println(i + " " + key);
                    }
                }
                destination.batchPut(map);
            } finally {
                // Make sure you close the iterator to avoid resource leaks.
                iterator.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void copyTo(KeyValueDatabaseInterface destination)
    {
        copyEntriesTo(destination);
    }

    public int getCount()
    {
        DBIterator iterator = db.iterator();
        int i = 0;
        for(iterator.seekToFirst(); iterator.hasNext(); iterator.next())
        {
            i++;
        }
        return i;
    }

    public void batchPut(HashMap map)
    {
        //TODO better
        WriteBatch batch = db.createWriteBatch();

        try
        {

            String[] values = new String[map.size()*2];
            Iterator it = map.entrySet().iterator();
            int i = 0 ;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                //System.out.println(pair.getKey() + " = " + pair.getValue());
                String key = pair.getKey().toString();
                String value = pair.getValue().toString();

                it.remove(); // avoids a ConcurrentModificationException
                //System.out.println(key);
                values[i++] = key;
                values[i++] = value;
                batch.put(key.toLowerCase().getBytes(), value.getBytes());
                //put(key, value);
                //System.out.println(getAsString(key));
            }
            db.write(batch);
            batch.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void batchDelete(ArrayList<String> list)
    {
        //TODO better
        WriteBatch batch = db.createWriteBatch();
        try
        {
            for (int i = 0; i<list.size(); i++)
            {
                String key = list.get(i);
                batch.delete(key.toLowerCase().getBytes());
            }
            db.write(batch);
            batch.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}