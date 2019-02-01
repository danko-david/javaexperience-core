package eu.javaexperience.collection.map;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChainedMap<K, V> extends AbstractMap<K, V> implements Map<K, V>
{
	public ChainedMap(Map<K, V>... maps)
	{
		backs = maps;
	}
	
	protected Map<K, V>[] backs;
	
	@Override
	public boolean containsKey(Object key)
	{
		for(Map<K, V> m:backs)
		{
			if(m.containsKey(key))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value)
	{
		for(Map<K, V> m:backs)
		{
			if(m.containsValue(value))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public V get(Object key)
	{
		for(Map<K, V> m:backs)
		{
			V ret =  m.get(key);
			if(null != ret)
			{
				return ret;
			}
		}
		return null;
	}

	@Override
	public V put(K key, V value)
	{
		return backs[0].put(key, value);
	}

	@Override
	public V remove(Object key)
	{
		return backs[0].remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(Entry<? extends K, ? extends V> e:m.entrySet())
		{
			backs[0].put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear()
	{
		backs[0].clear();
		
	}

	@Override
	public Set<K> keySet()
	{
		HashSet<K> keys = new HashSet<>();
		for(Map<K, V> m:backs)
		{
			keys.addAll(m.keySet());
		}
		return keys;
	}
}
