package eu.javaexperience.resource.pool;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.semantic.references.MayNull;

public class TrackedInfiniteResourcePool<T> implements TrackedResourcePool<T>
{
	protected final ConcurrentLinkedQueue<IssuedResource<T>> free = new ConcurrentLinkedQueue<>();
	
	protected final AtomicInteger issued = new AtomicInteger();
	
	protected final SimpleGet<T> factory;
	
	public int getIssuedResourcesCount()
	{
		return issued.get();
	}
	
	public TrackedInfiniteResourcePool(SimpleGet<T> factory)
	{
		AssertArgument.assertNotNull(this.factory = factory, "factory");
	}
	
	public IssuedResource<T> acquireResource()
	{
		IssuedResource<T> ret = free.poll();
		if(ret == null)
		{
			ret = new IssuedResource<T>(this, factory.get());
		}	
			ret.issued.set(true);

		issued.incrementAndGet();
		return ret;
	}

	public void releaseResource(IssuedResource<T> resource)
	{
		AssertArgument.assertNotNull(resource, "resource");
		if(resource.pool != this)
			throw new IllegalArgumentException("The resource acquired from other resource pool");
		
		if(!resource.issued.getAndSet(false))
			throw new RuntimeException("Resource double release!");
		
		free.add(resource);
		issued.decrementAndGet();
	}
	
	public int getFreeResourcesCount()
	{
		return free.size();
	}

	@Override
	public int getResourceLimitCount()
	{
		return -1;
	}

	@Override
	public @MayNull	IssuedResource<T> tryAcquireResource(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
	{
		return acquireResource();
	}

	@Override
	public @MayNull IssuedResource<T> pollResource()
	{
		IssuedResource<T> ret = free.poll();
		if(ret == null)
			return null;
		
		issued.incrementAndGet();
		return ret;
	}
	

	@Override
	public void destroyResource(IssuedResource<T> resource) {
		// TODO Auto-generated method stub
		
	}
}