package com.toortools.db;


import java.util.Map;

final class KeyValueDatabaseEntry<K, V> implements Map.Entry<K, V>
{
    private final K key;
    private V value;

    public KeyValueDatabaseEntry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey()
    {
        return key;
    }

    public V getValue()
    {
        return value;
    }

    public V setValue(V value)
    {
        V old = this.value;
        this.value = value;
        return old;
    }
}
