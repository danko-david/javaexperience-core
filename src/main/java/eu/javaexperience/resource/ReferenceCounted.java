package eu.javaexperience.resource;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ReferenceCounted<T> implements AutoCloseable
{
	public ReferenceCounted(T subject, int refcount)
	{
		this.subject = subject;
		this.refcount.set(refcount);
	}
	
	public T getSubject()
	{
		return subject;
	}
	
	protected T subject;
	
	protected AtomicInteger refcount = new AtomicInteger(0);
	
	protected abstract void onFree();
	
	public int acquire()
	{
		return refcount.incrementAndGet();
	}
	
	public int release()
	{
		int ret = refcount.decrementAndGet();
		if(ret <= 0)
		{
			onFree();
		}
		return ret;
	}
	
	@Override
	public void close()
	{
		release();
	}
}
