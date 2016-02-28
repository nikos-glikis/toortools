package com.toortools.db;
import java.util.ArrayList;
import java.util.HashMap;

public interface KeyValueDatabaseInterface
{
    void put(String key, String value);
    void delete(String key);
    String getAsString(String key);
    byte[] get(String key);
    void close();
    void copyTo(KeyValueDatabaseInterface destination);
    void deleteAll();
    void batchPut(HashMap p);
    void batchDelete(ArrayList<String> p);
}
