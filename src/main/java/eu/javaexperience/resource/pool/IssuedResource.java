package eu.javaexperience.resource.pool;

import java.util.concurrent.atomic.AtomicBoolean;

public class IssuedResource<T>
{
	protected final TrackedResourcePool<T> pool;
	protected final T resource;
	final AtomicBoolean issued = new AtomicBoolean();
	
	public IssuedResource(TrackedResourcePool<T> pool, T resource)
	{
		this.pool = pool;
		this.resource = resource;
	}
	
	public TrackedResourcePool<T> getPool()
	{
		return pool;
	}
	
	public void release()
	{
		pool.releaseResource(this);
	}
	
	public T getResource()
	{
		return resource;
	}
}