package eu.javaexperience.resource.pool;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.semantic.references.MayNull;

public class TrackedLimitedResourcePool<T> implements TrackedResourcePool<T>
{
	protected final ConcurrentLinkedQueue<IssuedResource<T>> free = new ConcurrentLinkedQueue<>();
	
	protected final AtomicInteger issued = new AtomicInteger();
	
	protected final SimpleGet<T> factory;
	
	protected final int limit;
	
	protected final Semaphore limiter;
	
	@Override
	public int getIssuedResourcesCount()
	{
		return issued.get();
	}
	
	public TrackedLimitedResourcePool(SimpleGet<T> factory, int limit)
	{
		AssertArgument.assertNotNull(this.factory = factory, "factory");
		AssertArgument.assertGreaterOrEqualsThan(this.limit = limit, 0, "limit");
		limiter = new Semaphore(limit);
	}
	
	@Override
	public IssuedResource<T> acquireResource() throws InterruptedException
	{
		return tryAcquireResource(0, null);
	}
	
	@Override
	public void releaseResource(IssuedResource<T> resource)
	{
		AssertArgument.assertNotNull(resource, "resource");
		if(resource.pool != this)
			throw new IllegalArgumentException("The resource acquired from other resource pool");
		
		if(!resource.issued.getAndSet(false))
			throw new RuntimeException("Resource double release!");
		
		free.add(resource);
		issued.decrementAndGet();
		limiter.release();
	}
	
	@Override
	public int getFreeResourcesCount()
	{
		return free.size();
	}

	@Override
	public int getResourceLimitCount()
	{
		return limit;
	}

	@Override
	public @MayNull IssuedResource<T> tryAcquireResource(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
	{
		if(paramTimeUnit == null)
			limiter.acquire();
		else if(!limiter.tryAcquire(paramLong, paramTimeUnit))
			return null;
		
		IssuedResource<T> ret = free.poll();
		if(ret == null)
		{
			ret = new IssuedResource<T>(this, factory.get());
		}

		ret.issued.set(true);
		
		issued.incrementAndGet();
		return ret;
	}

	@Override
	public @MayNull IssuedResource<T> pollResource()
	{
		IssuedResource<T> ret = free.poll();
		if(null == ret)
		{
			return null;
		}
		
		ret.issued.set(true);
		issued.incrementAndGet();
		return ret;
	}

	@Override
	public void destroyResource(IssuedResource<T> resource)
	{
		if(!resource.issued.get())
		{
			throw new RuntimeException("Resource released (not acquired), can't be destroyed");
		}
		
		
		issued.decrementAndGet();
	}
}