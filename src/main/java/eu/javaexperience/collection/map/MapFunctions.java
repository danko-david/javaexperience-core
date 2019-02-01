package eu.javaexperience.collection.map;

import java.util.Map;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class MapFunctions
{
	public static <K,V> GetBy1<V, Map<K, V>> getByKey(final K key)
	{
		return new GetBy1<V, Map<K,V>>()
		{
			@Override
			public V getBy(Map<K, V> a)
			{
				return a.get(key);
			}
		};
	}
}
