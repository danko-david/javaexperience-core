package eu.javaexperience.collection.queue;

import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import eu.javaexperience.collection.PublisherCollection;


public class QueueAdapter<T> extends PublisherCollection<T> implements Queue<T>
{
	protected Map<String, T> backend;
	
	public QueueAdapter(Map<String, T> backend)
	{
		this.backend = backend;
		for(Entry<String, T> kv:backend.entrySet())
		{
			if(null != kv.getValue())
			{
				int val = Integer.parseInt(kv.getKey());
				lastVal = Math.max(lastVal, val);
				if(firstVal == -1)
				{
					firstVal = val;
				}
				else
				{
					firstVal = Math.min(firstVal, val);
				}
			}
		}
	}
	
	protected int lastVal = 0;
	protected int firstVal = -1;
	
	@Override
	public boolean offer(T e)
	{
		return add(e);
	}

	@Override
	public T remove()
	{
		T ret = poll();
		assertNotEmpty(ret);
		return ret;
	}
	
	@Override
	public T element()
	{
		T ret = peek();
		assertNotEmpty(ret);
		return ret;
	}
	
	protected void assertNotEmpty(T elem)
	{
		if(null == elem)
		{
			throw new IllegalStateException("Queue is empty");
		}
	}
	
	@Override
	public T peek()
	{
		synchronized (backend)
		{
			String key = String.valueOf(firstVal);
			return backend.get(key);
		}
	}
	
	@Override
	public T poll()
	{
		synchronized(backend)
		{
			String key = String.valueOf(firstVal++);
			T ret = backend.get(key);
			backend.remove(key);
			return ret;
		}
	}
	
	@Override
	public boolean add(T obj)
	{
		synchronized (backend)
		{
			backend.put(String.valueOf(lastVal++), obj);
		}
		return true;
	}
}