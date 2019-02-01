package eu.javaexperience.collection.map;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.collection.iterator.NotNullIterable;
import eu.javaexperience.collection.set.ArrayAsSetRO;
import eu.javaexperience.interfaces.simple.publish.SimplePublish3;
import eu.javaexperience.reflect.Mirror;

public class SmallMap<K,V> implements Map<K,V>, Cloneable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Object[] keys;
	Object[] vals;
	int ep  = 0;
	int kvs = 0;
	
	public SmallMap()
	{
		keys = Mirror.emptyObjectArray;
		vals = Mirror.emptyObjectArray;
	}
	
	public SmallMap(Map<K, V> from)
	{
		int initalLength = from.size();
		keys = new Object[initalLength];
		vals = new Object[initalLength];
		
		for(java.util.Map.Entry<K, V> kv:from.entrySet())
		{
			put(kv.getKey(), kv.getValue());
		}
	}

	
	public SmallMap(int initalLength)
	{
		keys = new Object[initalLength];
		vals = new Object[initalLength];
	}
	
	@Override
	public int size()
	{
		return kvs;
	}

	@Override
	public boolean isEmpty()
	{
		return kvs == 0;
	}

	@Override
	public boolean containsKey(Object paramObject)
	{
		for(int i=0;i<ep;i++)
			if(paramObject.equals(keys[i]))
				return true;
		
		return false;
	}

	@Override
	public boolean containsValue(Object paramObject)
	{
		for(int i=0;i<ep;i++)
			if(paramObject.equals(vals[i]))
				return true;
		
		return false;
	}

	@Override
	public V get(Object paramObject)
	{
		if(paramObject == null)
			return null;
		
		for(int i=0;i<ep;i++)
			if(paramObject.equals(keys[i]))
				return (V) vals[i];
		
		return null;
	}

	private void inc()
	{
		if(ep == keys.length)
		{
			int len = keys.length*2;
			if(0 == len)
			{
				len = 4;
			}
			
			keys = Arrays.copyOf(keys, len);
			vals = Arrays.copyOf(vals, len);
		}
	}
	
	@Override
	public V put(K paramK, V paramV)
	{
		//ha már benne van érték felülírás
		for(int i=0;i<ep;i++)
			if(paramK.equals(keys[i]))
			{
				Object ret = vals[i];
				vals[i] = paramV;
				return (V) ret;
			}

		//ha a kulcsok végpontja és a tömb végpontja ugyanaz, akkor nincs benne null, a végére hozzáadjuk
		if(ep == kvs)
		{
			inc();
			keys[kvs++] = paramK;
			vals[ep++] = paramV;
		}//köztes beszúrás
		else
			for(int i=0;i<ep;i++)
				if(keys[i] == null)
				{
					keys[i] = paramK;
					vals[i] = paramV;
					kvs++;
					inc();
					break;
				}

		return null;
	}

	@Override
	public V remove(Object paramObject)
	{
		for(int i=0;i<ep;i++)
			if(paramObject.equals(keys[i]))
			{
				Object ret = vals[i];
				keys[i] = null;
				vals[i] = null;
				
				//csönkkent a kulcs-érték párok mérete
				kvs--;
				
				//ha a legvégéről lett kitörölve akkor a vége mutatót visszább húzzuk
				if(i == ep-1)
					ep--;

				return (V) ret;
			}	
	
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> paramMap)
	{
		for(java.util.Map.Entry<? extends K, ? extends V> kv:paramMap.entrySet())
			put(kv.getKey(), kv.getValue());
	}

	@Override
	public void clear()
	{
		for(int i=0;i<ep;i++)
			keys[i] = vals[i] = null;
		
		ep = 0;
		kvs = 0;
	}

	@Override
	public Set<K> keySet()
	{
		Object[] ret = new Object[kvs];
		int cep = 0;
		for(int i = 0;i < ep;i++)
			if(keys[i] != null)
				ret[cep++] = keys[i];
		
		if(cep == ret.length)
		{
			return new ArrayAsSetRO(ret);
		}
		else
		{
			return new ArrayAsSetRO(Arrays.copyOf(ret, cep));
		}
	}

	@Override
	public Collection<V> values()
	{
		Object[] ret = new Object[kvs];
		int aep = 0;
		for(int i=0;i<ep;i++)
		{
			if(vals[i] != null)
				ret[aep++] = vals[i];
		}
		
		if(aep == ret.length)
		{
			return new ArrayAsSetRO(ret);
		}
		else
		{
			return new ArrayAsSetRO(Arrays.copyOf(ret, aep));
		}
	}

	public K getKeyByValue(V val)
	{
		for(int i=0;i<ep;i++)
			if(val.equals(vals[i]))
				return (K) keys[i];
		
		return null;
	}
	
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		KeyVal<K,V>[] ret = new KeyVal[kvs];
		int cep = 0;
		for(int i=0;i<ep;i++)
		{
			if(keys[i] != null)
			{
				ret[cep++] = new KeyVal<K, V>((K)keys[i],(V) vals[i]);
			}
		}
		return new ArrayAsSetRO(ret);
	}
	
	public SmallMap<K,V> clone()
	{
		SmallMap<K,V> ret = new SmallMap();
		ret.keys = Arrays.copyOf(keys, keys.length);
		ret.vals = Arrays.copyOf(vals, vals.length);
		ret.kvs = kvs;
		ret.ep = ep;
		
		return ret;
	}
	
	public Iterable<K> getKeyIterator()
	{
		return (Iterable<K>) NotNullIterable.whitoutNulls(keys);
	}
	
	/**
	 * go trough all key and value and publish with the specified
	 * SimplePublis, you can specify an extra parameter which will be also
	 * published.
	 * 	
	 *  Purpose:
	 *  you need only one SimplePublish instance to process all key and value.
	 *  this technique doesn't need extra instance creation
	 *  (set and entries)
	 * */
	public <T> void each(SimplePublish3<K, V, T> pub, T param)
	{
		for(int i=0;i<ep;++i)
		{
			pub.publish((K) keys[i], (V) vals[i], param);
		}
	}
	
	
	public String toString()
	{
		return MapTools.toStringMultiline(this);
	}
}
