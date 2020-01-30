package eu.javaexperience.multithread;


import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import eu.javaexperience.interfaces.simple.publish.SimplePublish2;

public abstract class RerunnableThread<T>
{
	protected final Semaphore accept = new Semaphore(0);
	protected final Semaphore free = new Semaphore(0);
	protected volatile T param = null;

	protected volatile long lastUsed = 0;
	
	private final Thread worker = new Thread()
	{
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					free.release();
					accept.acquire();
				}
				catch (InterruptedException e)
				{
					return;
				}
				
				try
				{
					runThis(param);
				}
				catch(Throwable e)
				{
					if(e == POISON)
						return;
					
					System.err.println("TOPLEVEL UNCATCHED EXCEPTION");
					e.printStackTrace();
					
					try
					{
						if(onException!=null)
							onException.publish(RerunnableThread.this,e);
					}
					catch(Exception sad)
					{}
				}
				
				param = null;
			}
		}
	};

	public RerunnableThread(boolean daemon)
	{
		worker.setDaemon(daemon);
		worker.start();
	}
	
	public RerunnableThread()
	{
		this(false);
	}
	
	public StackTraceElement[] getStackTraceElements()
	{
		return worker.getStackTrace();
	}
	
	public static final Error POISON = MultithreadingTools.THREAD_SHUTDOWN_POISON;
	
	protected void stopCallerThread()
	{
		throw POISON;
	}
	
	public boolean isDaemon()
	{
		return worker.isDaemon();
	}
	
	public void waitFree(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException
	{
		if(free.tryAcquire(timeout, unit))
		{
			free.release();
			return;
		}
		throw new TimeoutException();
	}
	
	public long getLastUsed()
	{
		return lastUsed;
	}
	
	public boolean tryRerun(T param)
	{
		if(free.tryAcquire())
		{
			this.param = param;
			lastUsed = System.currentTimeMillis();
			accept.release();
			return true;
		}
		return false;
	}
	
	public boolean isFree()
	{
		return 0 != free.availablePermits();
	}

	public T getParam()
	{
		return param;
	}

	public abstract void runThis(T param) throws Throwable;
	
	protected SimplePublish2<RerunnableThread<T>,Throwable> onException = null;
	
	@Override
	public void finalize()
	{
		worker.interrupt();
	}
}