package eu.javaexperience.multithread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.multithread.notify.WaitForEvents;

public class MultithreadingTools
{
	
	/**
	 * Used for implement thread shutdown request.
	 * 
	 * Used (must be used) in ALL multithreading uniliys which cooperates with threads or thread pools,
	 * or manages any {@link Thread}.
	 * 
	 * clients: throw this exception to indicate for current thread (even if job is issed to a thread pool)
	 * that it must be shutdown.
	 *		desing hint: never catch Throwables, just Exceptions (RuntimeException is a descendant)
	 *		but if you have to, catch the throwable and if value is identically this value then throw again it.
	 *
	 *	
	 * maintainer: if this value is catched on the top level, shutdown the thread!
	 * 
	 * */
	public static final Error THREAD_SHUTDOWN_POISON = new Error();
	
	public static <E> Job<Queue<E>> processAll(final Job<E> job)
	{
		return new Job<Queue<E>>()
		{
			@Override
			public void exec(Queue<E> param) throws Throwable
			{
				E elem = null;
				while((elem = param.poll()) != null)
				{
					job.exec(elem);
				}
			}
		};
	}
	
	public static <E> Job<BlockingQueue<E>> processInfinite(final Job<E> job)
	{
		return new Job<BlockingQueue<E>>()
		{
			@Override
			public void exec(BlockingQueue<E> param) throws Throwable
			{
				E elem = null;
				while((elem = param.take()) != null)
				{
					job.exec(elem);
				}
			}
		};
	}

	public static void topLevelException(Throwable t)
	{
		System.err.println("=== TOP LEVEL UNCATCHED EXCEPTION");
		t.printStackTrace();
	}

	public static <T> void adhocMultiProcessAll
	(
		Queue<T> units,
		int concurrency,
		SimplePublish1<T> exec
	)
	{
		for(int i=0;i<concurrency;++i)
		{
			new Thread()
			{
				public void run()
				{
					T elem = null;
					while(null != (elem = units.poll()))
					{
						exec.publish(elem);
					}
				};
			}.start();
		}
	}

	public static <P,R> List<R> processAllConcurrently
	(
		int maxConcurrency,
		Collection<P> toProc,
		GetBy1<R, P> processor
	)
	{
		WaitForEvents w = new WaitForEvents(toProc.size());
		List<R> ret = new ArrayList<>();
		
		Queue<P> params = new LinkedBlockingQueue<>();
		params.addAll(toProc);
		
		int c = Math.min(maxConcurrency, toProc.size());
		for(int i=0;i<c;++i)
		{
			new Thread()
			{
				public void run()
				{
					P p = null;
					while(null != (p = params.poll()))
					{
						try
						{
							R proc = processor.getBy(p);
							synchronized(ret)
							{
								ret.add(proc);
							}
						}
						catch(Throwable t)
						{
							topLevelException(t);
						}
						finally
						{
							w.call();
						}
					}
				};
			}.start();
		}
		
		w.waitForAllEvent();
		return ret;
	}
}
