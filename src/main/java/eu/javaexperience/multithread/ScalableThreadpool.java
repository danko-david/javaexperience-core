package eu.javaexperience.multithread;

import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimpleGet;

public class ScalableThreadpool<T>
{
	protected final SimpleGet<BlockingJob<T>> blockingJobFactory;
	public ScalableThreadpool(SimpleGet<BlockingJob<T>> blockingJobFactory)
	{
		AssertArgument.assertNotNull(this.blockingJobFactory = blockingJobFactory, "blockingJobFactory");
	}
	
	protected Vector<Thread> workers = new Vector<>();
	
	protected AtomicInteger numberOfWorkers = new AtomicInteger();	
	
	public int getNumberOfWorkers()
	{
		return numberOfWorkers.get();
	}
	
	public void setNumberOfWorkers(int initialWorkerCount)
	{
		numberOfWorkers.set(initialWorkerCount);
	}
	
	protected AtomicInteger waitingWorkers = new AtomicInteger();
	
	public int getNumberOfWaitingWorkers()
	{
		return waitingWorkers.get();
	}
	
	protected AtomicInteger workingWorkers = new AtomicInteger();
	
	public int getNumberOfWorkingWorkers()
	{
		return workingWorkers.get();
	}

	public synchronized void setWorkerCount(int newCount) throws InterruptedException
	{
		int current = numberOfWorkers.get();
		numberOfWorkers.set(newCount);
		if(current == newCount)
		{
			return;
		}
		if(newCount <= 0)
		{
			cleanShutdown();
			return;
		}
		else if(newCount < current)
		{
			shrinkWorkers();
			return;
		}
		else if(newCount > current)
		{
			growthWorkers();
		}
		else
		{
			System.out.println("Impossible: newCount:"+newCount+" current: "+current);
			return;
		}
	}
	
	protected final AtomicReference<Semaphore> semaphoreForShrink = new AtomicReference<>();
	
	protected synchronized void shrinkWorkers()
	{
		Semaphore sem = new Semaphore(numberOfWorkers.get());
		//return only on successfully shrink
		semaphoreForShrink.set(sem);
		semaphoreForShrink.set(null);
	}
	
	protected synchronized void growthWorkers()
	{
		int plus = numberOfWorkers.get() - workers.size();
		for(int i=0;i<plus;++i)
		{
			Thread t = createNewWorker();
			workers.add(t);
			t.start();
		}
	}
	
	public synchronized void start()
	{
		growthWorkers();
	}
	
	public synchronized void cleanShutdown()
	{
		numberOfWorkers.set(0);
		shrinkWorkers();
	}

	protected Thread createNewWorker()
	{
		return new Thread()
		{
			protected final BlockingJob<T> job = blockingJobFactory.get();
			
			@Override
			public void run()
			{
				while(alive)
				{
					Semaphore sem = semaphoreForShrink.get();
					try
					{
						if(null != sem)
						{
							if(!sem.tryAcquire())
							{
								//This thread is out of new pool size, so it should be shuwdown.
								workers.remove(this);
								/*synchronized (sem)
								{
									sem.notifyAll();
								}*/
								return;
							}
						}
						
			 			/*T ret = strategy.accept(SturmWaffe.this, srv);
						Q q = strategy.wrap(SturmWaffe.this, ret);
						q.*/
						T ret = null;
						try
						{
							waitingWorkers.incrementAndGet();
							ret = job.acceptJob();
						}
						catch(Throwable t)
						{
							if(t == MultithreadingTools.THREAD_SHUTDOWN_POISON)
							{
								return;
							}
							
							MultithreadingTools.topLevelException(t);
							continue;
						}
						finally
						{
							waitingWorkers.decrementAndGet();
						}
						
						try
						{
							workingWorkers.incrementAndGet();
							job.exec(ret);
						}
						catch(Throwable t)
						{
							if(t == MultithreadingTools.THREAD_SHUTDOWN_POISON)
							{
								return;
							}
							
							MultithreadingTools.topLevelException(t);
						}
						finally
						{
							workingWorkers.decrementAndGet();
						}
						
					}
					finally
					{
						if(null != sem)
							sem.release();
					}
				}
			}
		};
	}

	@Deprecated
	public void fillThreads(Collection<Thread> poolThreads)
	{
		poolThreads.addAll(workers);
	}
	
	protected boolean alive = true;
	
	public void stop()
	{
		alive = false;
		cleanShutdown();
		for(Thread t:workers)
		{
			t.interrupt();
		}
	}
}