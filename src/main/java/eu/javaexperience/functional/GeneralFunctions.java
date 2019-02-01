package eu.javaexperience.functional;

import eu.javaexperience.annotation.FunctionDescription;
import eu.javaexperience.annotation.FunctionVariableDescription;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;

public class GeneralFunctions
{
	@FunctionDescription
	(
		functionDescription = "N szeri ismételt végrehajtás",
		parameters = 
		{
			@FunctionVariableDescription(description = "Ismétlések száma", mayNull = false, paramName = "N", type = Object.class),
			@FunctionVariableDescription(description = "Műveletek", mayNull = false, paramName = "ops", type = Object.class)
		},
		returning = @FunctionVariableDescription(description="Összefogó 1 db művelet",mayNull=false,paramName="",type=Object.class) 
	)
	public static SimplePublish1<Map<String,Object>> repeatTimes
	(
		final int times,
		final SimplePublish1<Map<String,Object>>... ops
	)
	{
		return new SimplePublish1<Map<String, Object>>()
		{
			@Override
			public void publish(Map<String, Object> a)
			{
				
				for(int i=0;i<times;++i)
				{
					for(SimplePublish1 op:ops)
					{
						op.publish(a);
					}
				}
			}
		};
	}
	
	protected static int getStorageSize(Object o)
	{
		if(null == o)
		{
			return 0;
		}
		else if(o instanceof Collection)
		{
			return ((Collection)o).size();
		}
		else if(o instanceof Map)
		{
			return ((Map)o).size();
		}
		else if(o.getClass().isArray())
		{
			return Array.getLength(o);
		}
		else
		{
			return 0;
		}
	}
	
	public static SimplePublish1<Map<String,Object>> repeatUntilStorageSizeNotChange
	(
		final String storage_under_key,
		final SimplePublish1<Map<String,Object>>... ops
	)
	{
		return new SimplePublish1<Map<String,Object>>()
		{
			@Override
			public void publish(Map<String,Object> a)
			{
				Object storage = a.get(storage_under_key);
				int prev = getStorageSize(storage);
				int crnt = 0;
				do
				{
					prev = crnt;
					for(SimplePublish1 op:ops)
					{
						op.publish(a);
					}
					crnt = getStorageSize(storage);
				}
				while(prev != crnt);
			}
		};
	}
	
	
	public static <R, P> void do_parallel
	(
		final SimplePublish1<R> put_result,
		final Collection<P> src_coll_get,
		final GetBy1<R, P> process_single,
		final int concurrency
	)
	{
		final BlockingQueue<P> urls_queue = new LinkedBlockingQueue<>();
		
		urls_queue.addAll(src_coll_get);
		
		final AtomicInteger nums = new AtomicInteger(concurrency);
		
		for(int i=0;i<concurrency;++i)
		{
			new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						P todo = null;
						while(null != (todo = urls_queue.poll()))
						{
							put_result.publish(process_single.getBy(todo));
						}
					}
					catch(Throwable e)
					{
						e.printStackTrace();
					}
					
					synchronized (nums)
					{
						nums.decrementAndGet();
						nums.notifyAll();
					}
					
				};
			}.start();
		}
		
		synchronized (nums)
		{
			while(0 != nums.get())
			{
				try
				{
					nums.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@FunctionDescription
	(
		functionDescription = "Műveletek soros végrehajása",
		parameters = {@FunctionVariableDescription(description = "Műveletek", mayNull = false, paramName = "sequence", type = Object.class)},
		returning = @FunctionVariableDescription(description="Összefogó 1 db művelet",mayNull=false,paramName="",type=Object.class) 
	)
	public static SimplePublish1<Map<String, Object>> sequence(final SimplePublish1<Map<String, Object>>... seq)
	{
		return new SimplePublish1<Map<String,Object>>()
		{
			@Override
			public void publish(Map<String, Object> a)
			{
				for(SimplePublish1<Map<String, Object>> s:seq)
				{
					if(null != s)
					{
						s.publish(a);
					}
				}
			}
		};
	}
	
	public static <T> SimplePublish1<Map<String, Object>> assign
	(
		final String name,
		final T value
	)
	{
		return new SimplePublish1<Map<String, Object>>()
		{
			@Override
			public void publish(Map<String, Object> a)
			{
				a.put(name, value);
			}
		};
	}
	
	@FunctionDescription
	(
		functionDescription = "Az első nem null érték felhasználása",
		parameters = {@FunctionVariableDescription(description = "Értékek", mayNull = false, paramName = "sequence", type = Object.class)},
		returning = @FunctionVariableDescription(description="első nem null érték",mayNull=false,paramName="",type=Object.class) 
	)
	public static <T> T firstNonNull(T... values)
	{
		for(T v:values)
		{
			if(null != v)
			{
				return v;
			}
		}
		
		return null;
	}
}
