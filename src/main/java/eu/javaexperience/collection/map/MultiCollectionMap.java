package eu.javaexperience.collection.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.set.ArrayListSeemsSet;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.interfaces.simple.SimpleGetFactory;

public class MultiCollectionMap<K,V> implements Map<K,V>
{
	protected final Map<K, Collection<V>> back;
	protected final SimpleGet<Collection<V>> collectionCreator;
	
	public MultiCollectionMap(Map<K, Collection<V>> back, SimpleGet<Collection<V>> listCreator)
	{
		this.back = back;
		this.collectionCreator = listCreator;
	}
	
	@Override
	public int size()
	{
		return back.size();
	}

	@Override
	public boolean isEmpty()
	{
		return back.isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return back.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		for(Entry<K, V> kv: entrySet())
		{
			V v = kv.getValue();
			if(null != v)
			{
				if(v.equals(value))
				{
					return true;
				}
			}
		}

		return false;
	}
	
	@Override
	public V get(Object key)
	{
		Collection<V> get = back.get(key);
		if(null != get && get.size() > 0)
		{
			return CollectionTools.tryGetFirst(get);
		}
		else
		{
			return null;
		}
	}

	@Override
	public V put(K key, V value)
	{
		Collection<V> get = back.get(key);
		if(null == get)
		{
			get = collectionCreator.get();
			back.put(key, get);
		}
		get.add(value);
		
		//we always add new value, so old value is always null
		return null;
	}
	
	public V preserv(K key)
	{
		Collection<V> get = back.get(key);
		if(null == get)
		{
			get = collectionCreator.get();
			back.put(key, get);
		}
		
		//we always add new value, so old value is always null
		return null;
	}

	@Override
	public V remove(Object key)
	{
		Collection<V> ret = back.remove(key);
		
		if(null != ret && ret.size() > 0)
		{
			return CollectionTools.tryGetFirst(ret);
		}
		else
		{
			return null;
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(java.util.Map.Entry<? extends K, ? extends V>  kv:m.entrySet())
		{
			put(kv.getKey(), kv.getValue());
		}
	}

	@Override
	public void clear()
	{
		back.clear();
	}

	@Override
	public Set<K> keySet()
	{
		return back.keySet();
	}

	@Override
	public Collection<V> values()
	{
		Collection<Collection<V>> get = back.values();
		
		if(null == get)
		{
			return null;
		}
		
		ArrayList<V> ret = new ArrayList<>();
		
		for(Collection<V> vs:get)
		{
			if(null != vs)
			{
				for(V v:vs)
				{
					ret.add(v);
				}
			}
		}
		
		return ret;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		ArrayListSeemsSet<Entry<K,V>> ret = new ArrayListSeemsSet<>();
		for(Entry<K,Collection<V>> kvvs:back.entrySet())
		{
			K k = kvvs.getKey();
			Collection<V> vs = kvvs.getValue();
			if(null != vs)
			{
				for(V v:vs)
				{
					ret.add(new KeyVal(k, v));
				}
			}
		}
		
		return ret;
	}

	public Set<java.util.Map.Entry<K, Collection<V>>> multiEntrySet()
	{
		return back.entrySet();
	}
	
	public Collection<V> getCollection(Object key)
	{
		return back.get(key);
	}
	
}
