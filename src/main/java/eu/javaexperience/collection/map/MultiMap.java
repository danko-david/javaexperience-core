package eu.javaexperience.collection.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.collection.set.ArrayListSeemsSet;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.interfaces.simple.SimpleGetFactory;

public class MultiMap<K,V> implements Map<K,V>, Serializable
{
	protected final Map<K,List<V>> back;
	protected final SimpleGet<List<V>> listCreator;
	
	public MultiMap(Map<K,List<V>> back, SimpleGet<List<V>> listCreator)
	{
		this.back = back;
		this.listCreator = listCreator;
	}
	
	public MultiMap(Map<K,List<V>> back)
	{
		this(back, (SimpleGet)SimpleGetFactory.getArrayListFactory());
	}
	
	public MultiMap()
	{
		back = new HashMap<>();
		listCreator = (SimpleGet)SimpleGetFactory.getArrayListFactory();
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
	
	public List<V> getKeyList(K key)
	{
		List<V> get = back.get(key);
		if(null == get)
		{
			get = listCreator.get();
			back.put(key, get);
		}
		
		return get;
	}

	@Override
	public V get(Object key)
	{
		List<V> get = back.get(key);
		if(null != get && get.size() > 0)
		{
			return get.get(0);
		}
		else
		{
			return null;
		}
	}

	@Override
	public V put(K key, V value)
	{
		List<V> get = getKeyList(key);
		get.add(value);
		//we always add new value, so old value is always null
		return null;
	}

	@Override
	public V remove(Object key)
	{
		List<V> ret = back.remove(key);
		
		if(null != ret && ret.size() > 0)
		{
			return ret.get(0);
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
		Collection<List<V>> get = back.values();
		
		if(null == get)
		{
			return null;
		}
		
		ArrayList<V> ret = new ArrayList<>();
		
		for(List<V> vs:get)
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
		for(Entry<K,List<V>> kvvs:back.entrySet())
		{
			K k = kvvs.getKey();
			List<V> vs = kvvs.getValue();
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

	public Set<java.util.Map.Entry<K, List<V>>> multiEntrySet()
	{
		return back.entrySet();
	}
	
	public List<V> getList(Object key)
	{
		return back.get(key);
	}

	public void putList(K key, List<? extends V> add)
	{
		getKeyList(key).addAll(add);
	}

	public <M extends Map<K, List<V>>> M toSimpleMap(M dst)
	{
		for(Entry<K, List<V>> m:multiEntrySet())
		{
			dst.put(m.getKey(), m.getValue());
		}
		return dst;
	}
}
