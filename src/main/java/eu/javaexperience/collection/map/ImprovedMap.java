package eu.javaexperience.collection.map;

import java.util.Map;

public interface ImprovedMap<K,V> extends Map<K,V>
{
	public int copyAll(Map<? super K,? super V> dst);
}
