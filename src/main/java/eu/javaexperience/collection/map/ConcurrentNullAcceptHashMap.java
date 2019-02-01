package eu.javaexperience.collection.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Viselkedésre ugyanaz mint a ConcurrentHashMap annyi különbséggel
 * hogy ez elfogad null kulcsot is
 * */
public class ConcurrentNullAcceptHashMap<K,V> extends ConcurrentHashMap<K, V>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AtomicReference<V> forNull = new AtomicReference<>();
	
	@Override
	public V get(Object key)
	{
		if(key == null)
			return forNull.get();
		
		return super.get(key);
	}

	@Override
	public V put(K key, V value)
	{
		if(key == null)
		{
			V ret = forNull.get();
			forNull.set(value);
			return ret;
		}
		
		return super.put(key, value);
	}

	@Override
	public V putIfAbsent(K key, V fmm)
	{
		if(key == null)
		{
			V ret = forNull.get(); 
			if(ret == null)
			{
				forNull.set(fmm);
				return null;
			}
			return ret;
		}
		
		return super.putIfAbsent(key, fmm);
	}
	
	@Override
	public boolean remove(Object k, Object v)
	{
		if(k == null)
			return forNull.compareAndSet((V) v, (V) null);
		else
			return super.remove(k, v);
	}
	
	@Override
	public boolean replace(K k, V vo, V vn)
	{
		if(k == null)
			return forNull.compareAndSet((V) vo, (V) vn);
		else
			return super.replace(k, vo, vn);
	}
	
	@Override
	public V replace(K k, V v)
	{
		if(k == null)
		{
			//ha már van érték akkor cseréljük le
			while(true)
			{
				V ret = forNull.get();
				if(ret == null)
					return null;
				
				if(forNull.compareAndSet(ret, v))
					return ret;
			}
		}
		else
			return super.replace(k, v);
	}
}