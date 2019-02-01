package eu.javaexperience.collection.map;

import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class ConcurrentMapTools
{
	public static <R,P> R getOrCreate
	(
		ConcurrentMap<P,R> map,
		P key,
		GetBy1<R,P> factory
	)
	{
		R ret = map.get(key);
		if(null == ret)
		{
			R cre = factory.getBy(key);
			if(null == cre)
			{
				return null;
			}
			
			ret = map.putIfAbsent(key, cre);
			if(null != ret)
			{
				return ret;
			}
			
			return cre;
		}
		
		return ret;
	}
}
