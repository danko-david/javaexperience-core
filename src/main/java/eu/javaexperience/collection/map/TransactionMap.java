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
	
	@Deprecated
	public Map<K, V> getAccessMap()
	{
		return access;
	}
	
	public Map<K, V> getDiffMap()
	{
		return diff;
	}
	
	public Map<K, V> getModifiyableMap()
	{
		return modifiable;
	}
	
	protected static final Object NULL_VALUE = new Object();
	
	protected V touchValue(K key)
	{
		{
			Object in = access.get(key);
			if(null != in)
			{
				return modifiable.get(key);
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
		touchValue(key);
		return modifiable.put(key, value);
	}

	@Override
	public V remove(Object key)
	{
		touchValue((K) key);
		return modifiable.remove(key);
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
		return modifiable.keySet();
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
}
