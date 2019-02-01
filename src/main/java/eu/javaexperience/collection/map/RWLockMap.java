package eu.javaexperience.collection.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import eu.javaexperience.asserts.AssertArgument;

public class RWLockMap<K,V> implements ConcurrentMap<K, V>, ImprovedMap<K, V>
{
	public RWLockMap(Map<K,V> map)
	{
		AssertArgument.assertNotNull(this.map = map, "map");
	}
	
	protected final Map<K,V> map;
	
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	protected ReadLock rl = lock.readLock();
	protected WriteLock wl = lock.writeLock();
	
	/**
	 * Only for advanced usage
	 * */
	@Deprecated
	public ReadLock getReadLock()
	{
		return rl;
	}

	/**
	 * Only for advanced usage
	 * */
	@Deprecated
	public WriteLock getWriteLock()
	{
		return wl;
	}

	/**
	 * Only for advanced usage
	 * */
	@Deprecated
	public Map<K,V> getBackendMap()
	{
		return map;
	}
	
	@Override
	public int size()
	{
		rl.lock();
		try
		{
			return map.size();
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public boolean isEmpty()
	{
		rl.lock();
		try
		{
			return map.isEmpty();
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public boolean containsKey(Object key)
	{
		rl.lock();
		try
		{
			return map.containsKey(key);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public boolean containsValue(Object value)
	{
		rl.lock();
		try
		{
			return map.containsValue(value);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public V get(Object key)
	{
		rl.lock();
		try
		{
			return map.get(key);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public V put(K key, V value)
	{
		wl.lock();
		try
		{
			return map.put(key, value);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public V remove(Object key)
	{
		wl.lock();
		try
		{
			return map.remove(key);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		wl.lock();
		try
		{
			map.putAll(m);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public void clear()
	{
		wl.lock();
		try
		{
			map.clear();
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public Set<K> keySet()
	{
		rl.lock();
		try
		{
			return map.keySet();
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public Collection<V> values()
	{
		rl.lock();
		try
		{
			return map.values();
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		rl.lock();
		try
		{
			return map.entrySet();
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public V putIfAbsent(K key, V value)
	{
		wl.lock();
		try
		{
			/* This class is basically a wrapper for non ConcurrentMaps
			if(map instanceof ConcurrentMap)
			{
				return (V) ((ConcurrentMap)map).putIfAbsent(key, value);
			}
			else*/
			{
				if(!map.containsKey(key))
				{
					return map.put(key, value);
				}
				else
				{
					return null;
				}
			}
		}
		finally
		{
			wl.unlock();
		}
	}
	@Override
	public boolean remove(Object key, Object value)
	{
		wl.lock();
		try
		{
			Object in = map.get(key);
			if(null != value && value.equals(in))
			{
				map.remove(key);
				return true;
			}
			else
			{
				return false;
			}
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue)
	{
		wl.lock();
		try
		{
			Object in = map.get(key);
			if(null != in && in.equals(oldValue))
			{
				map.put(key, newValue);
				return true;
			}
			else
			{
				return false;
			}
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public V replace(K key, V value)
	{
		wl.lock();
		try
		{
			if(map.containsKey(key))
			{
				return map.put(key, value);
			}
			else
			{
				return null;
			}
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public int copyAll(Map<? super K, ? super V> dst)
	{
		rl.lock();
		try
		{
			int len = dst.size();
			dst.putAll(map);
			return dst.size() - len;
		}
		finally
		{
			rl.unlock();
		}
	}
	
	public String toString()
	{
		return "ReentrantMap:"+MapTools.toStringMultiline(this);
	}
}