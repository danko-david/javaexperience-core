package eu.javaexperience.collection.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.collection.NullCollection;
import eu.javaexperience.collection.set.NullSet;

public class NullMap<K,V> implements ConcurrentMap<K, V>
{
	public static final NullMap instance = new NullMap<>();
	
	
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
	public V put(K key, V value)
	{
		return null;
	}

	@Override
	public V remove(Object key)
	{
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{}

	@Override
	public void clear()
	{
		//already successfull ;)
	}

	@Override
	public Set<K> keySet()
	{
		return NullSet.instance;
	}

	@Override
	public Collection<V> values()
	{
		return NullCollection.INSTANCE;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return NullSet.instance;
	}

	@Override
	public V putIfAbsent(K key, V value)
	{
		return null;
	}

	@Override
	public boolean remove(Object key, Object value)
	{
		return false;
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue)
	{
		return false;
	}

	@Override
	public V replace(K key, V value)
	{
		return null;
	}
}
