package eu.javaexperience.collection.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import eu.javaexperience.reflect.Mirror;

public abstract class AbstractMap<K, V> implements Map<K, V>
{
	@Override
	public int size()
	{
		return keySet().size();
	}

	@Override
	public boolean isEmpty()
	{
		return 0 == size();
	}
	
	@Override
	public boolean containsValue(Object value)
	{
		for(Entry<K, V> kv:entrySet())
		{
			if(Mirror.equals(value, kv.getValue()))
			{
				return true;
			}
		}
	
		return false;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(Entry<? extends K, ? extends V> kv:m.entrySet())
		{
			put(kv.getKey(), kv.getValue());
		}
	}
	
	@Override
	public Collection<V> values()
	{
		ArrayList<V> vals = new ArrayList<>();
		for(Entry<K, V> kv:entrySet())
		{
			vals.add(kv.getValue());
		}
		return vals;
	}

	@Override
	public Set<Entry<K, V>> entrySet()
	{
		HashSet<Entry<K, V>> ret = new HashSet<>();
		Set<K> keys = keySet();
		for(K s:keys)
		{
			ret.add(new KeyVal<>(s, this.get(s)));
		}
		
		return ret;
	}
}
