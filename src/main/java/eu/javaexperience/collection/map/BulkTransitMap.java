package eu.javaexperience.collection.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.collection.set.ArrayListSeemsSet;
import eu.javaexperience.database.JDBC;

/**
 * The only purpose why it's created to fill with data then
 * iterate truth by using entrySet() function.
 * 
 * Strongly used with {@link JDBC#fillIntoMap(java.sql.Connection, String, Map)}
 * */
public class BulkTransitMap<K,V> implements Map<K,V>, Cloneable
{
	protected ArrayListSeemsSet<java.util.Map.Entry<K, V>> records = new ArrayListSeemsSet<>();
	
	@Override
	public int size()
	{
		return records.size();
	}

	@Override
	public boolean isEmpty()
	{
		return records.size() == 0;
	}

	@Override
	public boolean containsKey(Object key)
	{
		if(key == null)
		{
			for(Entry<K, V> kv:records)
				if(kv.getKey() == null)
					return true;
		}
		else
		{
			for(Entry<K, V> kv:records)
				if(key.equals(kv.getKey()))
					return true;
		}
		
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		if(value == null)
		{
			for(Entry<K, V> kv:records)
				if(kv.getValue() == null)
					return true;
		}
		else
		{
			for(Entry<K, V> kv:records)
				if(value.equals(kv.getValue()))
					return true;
		}
		
		return false;
	}

	@Override
	public V get(Object key)
	{
		if(key == null)
		{
			for(Entry<K, V> kv:records)
				if(kv.getKey() == null)
					return kv.getValue();
		}
		else
		{
			for(Entry<K, V> kv:records)
				if(key.equals(kv.getKey()))
					return kv.getValue();
		}
	
		return null;
	}

	@Override
	public V put(K key, V value)
	{
		records.add(new KeyVal<>(key, value));
		return null;
	}

	@Override
	public V remove(Object key)
	{
		if(key == null)
		{
			for(Entry<K, V> kv:records)
				if(kv.getKey() == null)
				{
					V ret = kv.getValue();
					records.remove(kv);
					return ret;
				}
		}
		else
		{
			for(Entry<K, V> kv:records)
				if(key.equals(kv.getKey()))
				{
					V ret = kv.getValue();
					records.remove(kv);
					return ret;
				}
		}
		
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(Entry<? extends K, ? extends V> kv:m.entrySet())
			records.add(new KeyVal(kv.getKey(), kv.getValue()));
	}

	@Override
	public void clear()
	{
		records.clear();
	}

	@Override
	public Set<K> keySet()
	{
		ArrayListSeemsSet<K> keys = new ArrayListSeemsSet<>();
		for(Entry<K, V> kv:records)
			keys.add(kv.getKey());
		
		return keys;
	}

	@Override
	public Collection<V> values()
	{
		ArrayList<V> vals = new ArrayList<>();
		for(Entry<K, V> kv:records)
			vals.add(kv.getValue());
		
		return vals;
	}

	@Override
	public ArrayListSeemsSet<java.util.Map.Entry<K, V>> entrySet()
	{
		return records;
	}
	
	public String toString()
	{
		return MapTools.toStringMultiline(this);
	}
	
	public BulkTransitMap<K, V> clone()
	{
		BulkTransitMap<K, V> ret = new BulkTransitMap<>();
		ret.records.addAll(records);
		return ret;
	}
}
