package eu.javaexperience.collection.map;

import java.util.Map;
import java.util.Set;

public class TransactionMap<K, V> extends AbstractMap<K, V>
{
	protected Map<K, V> modifiable;
	
	protected Map<K, V> diff;
	
	protected Map<K, Boolean> contains;
	protected Map<K, V> access;
	
	protected Map<K, V> origin;
	
	public TransactionMap(Map<K, V> origin)
	{
		this.origin = origin;
		
		this.diff = new SmallMap<>();
		this.access = new SmallMap<>();
		this.contains = new SmallMap<>();
		
		this.modifiable = new ChainedMap<>(diff, access, origin);
	}
	
	public Map<K, V> getOriginalMap()
	{
		return origin;
	}
	
	public Map<K, V> getAccessMap()
	{
		return access;
	}
	
	public Map<K, V> getDiffMap()
	{
		return diff;
	}
	
	public Map<K, V> getModifiableMap()
	{
		return modifiable;
	}
	
	public static final Object NULL_VALUE = new Object();
	
	protected V touchValue(K key)
	{
		{
			Object in = access.get(key);
			if(null != in)
			{
				in = modifiable.get(key);
				if(NULL_VALUE == in)
				{
					return null;
				}
				else
				{
					return (V) in;
				}
			}
		}
		
		V re = origin.get(key);
		if(null == re)
		{
			access.put(key, (V)NULL_VALUE);
			return null;
		}
		else
		{
			access.put(key, re);
			return (V) re;
		}
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return null != touchValue((K) key);
	}

	@Override
	public V get(Object key)
	{
		return touchValue((K) key);
	}

	@Override
	public V put(K key, V value)
	{
		V o = touchValue(key);
		modifiable.put(key, value);
		return o;
	}

	@Override
	public V remove(Object key)
	{
		V o = touchValue((K) key);
		modifiable.put((K) key, (V) NULL_VALUE);
		return o;
	}

	@Override
	public void clear()
	{
		for(K k:keySet())
		{
			remove(k);
		}
	}

	@Override
	public Set<K> keySet()
	{
		Set<K> ret = modifiable.keySet();
		for(K k:ret)
		{
			touchValue(k);
		}
		return ret;
	}

	public V getAccessTimeValue(K key)
	{
		V in = access.get(key);
		if(NULL_VALUE == in)
		{
			return null;
		}
		
		return in;
	}
	
	@Override
	public int size()
	{
		int ret = 0;
		for(V v:values())
		{
			if(null != v)
			{
				++ret;
			}
		}
		return ret;
	}

}
