package eu.javaexperience.multithread;


import java.io.PrintStream;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;

public abstract class RerunnableThread<T>
{
	private volatile boolean free = true;
	private final Object mutex = new Object();
	
	protected final Semaphore semaphore = new Semaphore(0); 
	protected volatile T param = null;

	private volatile boolean needwait = true;

	protected volatile long lastUsed = 0;
	
	protected void init()
	{
		free = true;
		needwait = true;
	};
	
	private final Thread worker = new Thread()
	{
		@Override
		public void run()
		{
			while(true)
			{
					synchronized(mutex)
					{
						if(needwait)
						{	
							try
							{
								semaphore.acquire();
								//mutex.wait();
								free = false;
								needwait = true;
							}
							catch (InterruptedException e)
							{
								return;
							}
						}
					}
				try
				{
					runThis(param);//TODO ha megszakították (interruped) akkor állítsa vissza, nehogy a következő feladat arra számítson hogy új szál indul
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
				
				free = true;
				needwait = true;
				
				notifyFree();
			}
		}
	};

	public PrintStream errStream;

	public RerunnableThread(boolean daemon)
	{
		worker.setDaemon(daemon);
		worker.start();
	}
	
	public synchronized void resetIfNeed()
	{
		if(!worker.isAlive() || worker.isInterrupted())
		{
			init();
			worker.start();
		}
	}
	
	public StackTraceElement[] getStackTraceElements()
	{
		return worker.getStackTrace();
	}
	
	public RerunnableThread()
	{
		worker.start();
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
	
	public long getLastUsed()
	{
		return lastUsed;
	}
	
	public void rerunWithParam(T param)
	{
		synchronized(mutex)
		{
			if(!free)
				throw new IllegalStateException("ReuseableThread is in progress, not on rerun point, cannot rerun!");
				
			free = needwait = false;
			this.param = param;
			lastUsed = System.currentTimeMillis();
			semaphore.release();
			//mutex.notifyAll();
		}
		
	}

	public synchronized boolean rerunIfFreeWithParam(T param)
	{
		synchronized(mutex)
		{
			if(!isFree())
				return false;
			
			rerunWithParam(param);
			return true;
		}
	}	
	
	public boolean isFree()
	{
		return free;
	}

	public T getParam()
	{
		return param;
	}

	public abstract void runThis(T param) throws Throwable;
	
	
	protected Set<SimplePublish1<RerunnableThread<T>>> releaseListeners = Collections.newSetFromMap(new ConcurrentHashMap<SimplePublish1<RerunnableThread<T>>,Boolean>());
	
	private void notifyFree()
	{
		for(SimplePublish1<RerunnableThread<T>> pub:releaseListeners)
			try
			{
				pub.publish(this);
			}
			catch(Throwable t)
			{}
	}
	
	public boolean registerReleaseListener(SimplePublish1<RerunnableThread<T>> pub)
	{
		AssertArgument.assertNotNull(pub, "release listener");
		return releaseListeners.add(pub);
	}

	public boolean unregisterReleaseListener(SimplePublish1<RerunnableThread<T>> pub)
	{
		AssertArgument.assertNotNull(pub, "release listener");
		return releaseListeners.remove(pub);
	}	
	
	public boolean isReleaseListenerRegistered(SimplePublish1<RerunnableThread<T>> pub)
	{
		AssertArgument.assertNotNull(pub, "release listener");
		return releaseListeners.contains(pub);
	}	
	
	protected SimplePublish2<RerunnableThread<T>,Throwable> onException = null;
	
	@Override
	public void finalize()
	{
		worker.interrupt();
	}
}