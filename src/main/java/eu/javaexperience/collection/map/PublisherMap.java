package eu.javaexperience.collection.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.collection.NullCollection;
import eu.javaexperience.collection.set.NullSet;

public abstract class PublisherMap<K,V> implements Map<K, V>
{
	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public boolean isEmpty()
	{
		return true;
	}

	@Override
	public boolean containsKey(Object key)
	{
		return false;
	}

	@Override
	public boolean containsValue(Object value)
	{
		return false;
	}

	@Override
	public V get(Object key)
	{
		return null;
	}

	@Override
	public abstract V put(K key, V value);
	

	@Override
	public V remove(Object key)
	{
		return null;
	}

	//@Override
	//public abstract void putAll(Map<? extends K, ? extends V> m);
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(Entry<? extends K, ? extends V> kv:m.entrySet())
		{
			put(kv.getKey(), kv.getValue());
		}
	}

	@Override
	public void clear()
	{
		//no effect
	}

	@Override
	public Set<K> keySet()
	{
		return (Set<K>) NullSet.instance;
	}

	@Override
	public Collection<V> values()
	{
		return NullCollection.INSTANCE;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return (Set<java.util.Map.Entry<K, V>>) NullSet.instance;
	}
}
