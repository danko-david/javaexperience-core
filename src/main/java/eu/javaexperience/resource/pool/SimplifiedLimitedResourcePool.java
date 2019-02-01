package eu.javaexperience.resource.pool;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.semantic.references.MayNull;

public class SimplifiedLimitedResourcePool<T> implements ResourcePool<T>
{
	protected final ConcurrentLinkedQueue<T> free = new ConcurrentLinkedQueue<>();
	
	protected final SimpleGet<T> factory;
	
	protected final AtomicInteger issued = new AtomicInteger();
	
	protected final int limit;
	
	protected final Semaphore limiter;
	
	public SimplifiedLimitedResourcePool(SimpleGet<T> factory,int limit)
	{
		AssertArgument.assertNotNull(this.factory = factory, "factory");
		AssertArgument.assertGreaterOrEqualsThan(this.limit = limit, 0, "limit");
		limiter = new Semaphore(limit);
	}
	
	public T acquireResource() throws InterruptedException
	{
		return tryAcquireResource(0, null);
	}

	public void releaseResource(T resource)
	{
		AssertArgument.assertNotNull(resource, "resource");
		free.add(resource);
		issued.decrementAndGet();
		limiter.release();
	}
	
	/**
	 * Note that this counter can be negative,
	 * that's mean someone poisons this pool with "releasing" external created instances.
	 * */
	@Override
	public int getFreeResourcesCount()
	{
		return free.size();
	}

	@Override
	public int getIssuedResourcesCount()
	{
		return issued.get();
	}

	/**
	 * note that this value indicated the initially value of limit,
	 * the free + issued count can be higher than the limit if you  
	 * poisons the pool with releasing external created instances.
	 * */
	@Override
	public int getResourceLimitCount()
	{
		return limit;
	}

	@Override
	public @MayNull T tryAcquireResource(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
	{
		if(paramTimeUnit == null)
			limiter.acquire();
		else if(limiter.tryAcquire(paramLong, paramTimeUnit))
			return null;
		
		T ret = free.poll();
		if(ret == null)
			ret = factory.get();
		
		issued.incrementAndGet();
		return ret;
	}

	@Override
	public @MayNull T pollResource()
	{
		T ret = free.poll();
		if(ret == null)
			return null;
		
		issued.incrementAndGet();
		return ret;
	}
	
	@Override
	public void destroyResource(T resource)
	{
		AssertArgument.assertNotNull(resource, "resource");
		//we just do put it back.
		issued.decrementAndGet();
	}
}