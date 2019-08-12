package eu.javaexperience.cache;

import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class CacheOverTime<E, T> implements SimpleGet<T>
{
	protected E entitiy;
	protected long maxAge;
	protected GetBy1<Long, E> entityTime;
	protected GetBy1<T, E> parser;
	
	protected long lastParsed = -1;
	
	protected T lastValue;
	
	public CacheOverTime
	(
		E entity,
		long maxAge,
		GetBy1<Long, E> entityTime,
		GetBy1<T, E> parser
	)
	{
		this.entitiy = entity;
		this.maxAge = maxAge;
		this.entityTime = entityTime;
		this.parser = parser;
	}
	
	public synchronized T get()
	{
		//when expired
		if(lastParsed + maxAge <= System.currentTimeMillis())
		{
			lastValue = parser.getBy(entitiy);
			lastParsed = entityTime.getBy(entitiy);
		}
		
		return lastValue;
	}
}
