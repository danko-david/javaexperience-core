package eu.javaexperience.resource.pool;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.semantic.references.MayNull;

public class SimplifiedResourcePool<T> implements ResourcePool<T>
{
	protected final ConcurrentLinkedQueue<T> free = new ConcurrentLinkedQueue<>();
	
	protected final SimpleGet<T> factory;
	
	protected final AtomicInteger issued = new AtomicInteger();
	
	public SimplifiedResourcePool(SimpleGet<T> factory)
	{
		AssertArgument.assertNotNull(this.factory = factory, "factory");
	}
	
	@Override
	public T acquireResource()
	{
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
	public void releaseResource(T resource)
	{
		AssertArgument.assertNotNull(resource, "resource");
		free.add(resource);
		issued.decrementAndGet();
	}
	
	@Override
	public void destroyResource(T resource)
	{
		AssertArgument.assertNotNull(resource, "resource");
		//we just do put it back.
		issued.decrementAndGet();
	}
	
	/**
	 * Note that this counter can be negative,
	 * that's mean someone poisons this pool with "releasing" externally created instances.
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

	@Override
	public int getResourceLimitCount()
	{
		return -1;
	}

	@Override
	public @MayNull T tryAcquireResource(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
	{
		return acquireResource();
	}
}