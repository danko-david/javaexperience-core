package eu.javaexperience.collection.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.collection.OneShotCollection;
import eu.javaexperience.collection.set.OneShotSet;

public class OneShotMap<K, V> implements ConcurrentMap<K, V>
{
	protected final KeyVal<K, V> kv;
	
	public OneShotMap(K k,V v)
	{
		kv = new KeyVal<>(k, v);
	}
	
	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public boolean containsKey(Object key)
	{
		return kv.isKeyEquals(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return kv.isValueEquals(value);
	}

	@Override
	public V get(Object key)
	{
		return kv.isKeyEquals(key)?kv.v:null;
	}

	@Override
	public V put(K key, V value)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(Object key)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet()
	{
		return new OneShotSet<K>(kv.k);
	}

	@Override
	public Collection<V> values()
	{
		return new OneShotCollection<V>(kv.v);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return new OneShotSet<Map.Entry<K,V>>(kv);
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
