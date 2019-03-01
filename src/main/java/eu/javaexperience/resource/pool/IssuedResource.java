package eu.javaexperience.resource.pool;

import java.io.Closeable;
import java.io.IOException;

public class IssuedResource<T> implements Closeable
{
	protected final TrackedResourcePool<T> pool;
	protected T resource;
	protected boolean issued = true;
	
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
		synchronized(this)
		{
			if(!issued)
			{
				throw new RuntimeException("Resource double release!");
			}
			pool.releaseResource(this);
			resource = null;
			issued = false;
		}
		
	}
	
	public T getResource()
	{
		return resource;
	}

	@Override
	public void close() throws IOException
	{
		release();
	}
}