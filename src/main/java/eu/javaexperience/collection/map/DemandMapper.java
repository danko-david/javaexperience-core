package eu.javaexperience.collection.map;

import java.util.concurrent.ConcurrentHashMap;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class DemandMapper<K,V> extends ConcurrentHashMap<K, V>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected final GetBy1<V, K> get;

	public DemandMapper(GetBy1<V, K> get)
	{
		AssertArgument.assertNotNull(this.get = get, "get");
	}
	
	@Override
	public V get(Object inkey)
	{
		K key = (K) inkey;
		V ret = super.get(key);
		if(ret == null)
		{
			V getted = get.getBy(key);
			if(getted == null)
				return null;
			
			V in = super.putIfAbsent(key, getted);
			if(in != null)
				return in;
			
			return getted;
		}
		
		return ret;
	}
}
