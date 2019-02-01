package eu.javaexperience.collection.map;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.interfaces.simple.WrapUnwrap;

public class MappedMap<K, V> extends AbstractMap<K, V> implements Map<K, V>
{
	protected Map<K, V> map;
	protected WrapUnwrap<K, K> mapper;
	
	public MappedMap(Map<K, V> map, WrapUnwrap<K, K> mapper)
	{
		this.map = map;
		this.mapper = mapper;
	}

	@Override
	public boolean containsKey(Object key)
	{
		K k = mapper.wrap((K)key);
		if(null != k)
		{
			return map.containsKey(k);
		}
		return false;
	}

	@Override
	public V get(Object key)
	{
		K k = mapper.wrap((K)key);
		if(null != k)
		{
			return map.get(k);
		} 
		return null;
	}

	@Override
	public V put(K key, V value)
	{
		K k = mapper.wrap((K)key);
		if(null != k)
		{
			return map.put(k, value);
		}
		return null;
	}

	@Override
	public V remove(Object key)
	{
		K k = mapper.wrap((K)key);
		if(null != k)
		{
			return map.remove(k);
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(Entry<? extends K, ? extends V> kv:m.entrySet())
		{
			K k = mapper.wrap(kv.getKey());
			if(null != k)
			{
				map.put(k, kv.getValue());
			}
		}
	}

	@Override
	public void clear()
	{
		for(K k:keySet())
		{
			map.remove(k);
		}
	}

	@Override
	public Set<K> keySet()
	{
		HashSet<K> keys = new HashSet<>();
		for(K k:map.keySet())
		{
			k = mapper.unwrap(k);
			if(null != k)
			{
				keys.add(k);
			}
		}
		return keys;
	}
}