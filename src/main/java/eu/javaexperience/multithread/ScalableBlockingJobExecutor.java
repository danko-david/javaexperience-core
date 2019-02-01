package eu.javaexperience.multithread;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingTools;

public abstract class ScalableBlockingJobExecutor<T>
{
	protected ScalableThreadpoolManageStrategy poolStrategy;
	
	protected static Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("ScalableBlockingJobExecutor"));
	
	protected ScalableThreadpool<SimpleGet<BlockingJob<T>>> threadPool = new ScalableThreadpool
	(
		new SimpleGet<BlockingJob<T>>()
		{
			@Override
			public BlockingJob<T> get()
			{
				return blockingJob;
			}
		}
	);
	
	protected BlockingJob<T> blockingJob;
	
	public ScalableBlockingJobExecutor(ScalableThreadpoolManageStrategy poolStrategy)
	{
		AssertArgument.assertNotNull(this.poolStrategy = poolStrategy, "poolStrategy");
	}
	
	public ScalableBlockingJobExecutor(int initialWorketCount)
	{
		poolStrategy = getDefault(initialWorketCount);
	}
	
	@Deprecated
	public ScalableThreadpool<SimpleGet<BlockingJob<T>>> getThreadPool()
	{
		return threadPool;
	}
	
	public ScalableThreadpoolManageStrategy getStrategy()
	{
		return poolStrategy;
	}
	
	protected abstract BlockingJob<T> initalizeJob();
	
	public void start()
	{
		AssertArgument.assertNotNull(blockingJob = initalizeJob(), "blockingJob");
		poolStrategy.initialize(threadPool);
		threadPool.start();
	}
	
	protected void manageLoad()
	{
		poolStrategy.manageLoad(threadPool);
	}
	
	public void setPoolStrategy(ScalableThreadpoolManageStrategy poolStrategy)
	{
		this.poolStrategy = poolStrategy;
	}
	
	public static ScalableThreadpoolManageStrategy getDefault(final int initialWorkerCount)
	{
		//return getDefault(initialWorkerCount, 0.25, 0.75);
		return getDefault(initialWorkerCount, 0.15, 0.75, 0.4);
	}
	
	public static ScalableThreadpoolManageStrategy getDefault(final int initialWorkerCount, final double shrinkLoad, final double growLoad, final double targetLoad)
	{
		return new ScalableThreadpoolManageStrategy()
		{
			@Override
			public void manageLoad(ScalableThreadpool<?> pool)
			{
					//if busy is eq or more than 75 % we increase the workers count
					//to busy to 50 %
					//synchronized(pool)
					{	
						double num = pool.getNumberOfWorkers();
						double busy = pool.getNumberOfWorkingWorkers();
						
						double load = busy/num;
						if(load >= growLoad || load <= shrinkLoad)
						{
							handle(pool);
						}
					}
			}
			
			public void handle(ScalableThreadpool<?> pool)
			{
				//synchronized (this)
				{
					int num = pool.getNumberOfWorkers();
					int busy = pool.getNumberOfWorkingWorkers();
					
					double load = ((double)busy)/num;
					
					int newCount = num;
					
					if(load >= growLoad || load <= shrinkLoad)
					{
						newCount = (int) (busy * (1.0/targetLoad));
					}
					
					/*if()
					{
						newCount = (int) (busy * shrinkLoad);
					}*/
					
					if(newCount < initialWorkerCount)
					{
						newCount = initialWorkerCount;
					}
					
					if(num == newCount)
					{
						return;
					}
					
					//scale up
					//scale down
					
					LoggingTools.tryLogFormat(LOG, LogLevel.INFO, "Scaling pool: threads: %d, busy: %d, newThreadCount: %d", num, busy, newCount);
					
					try
					{
						pool.setWorkerCount(newCount);
						//System.out.println("New worker pool size is: "+newCount);
					}
					catch (InterruptedException e)
					{
						LoggingTools.tryLogSimple(LOG, LogLevel.WARNING, e);
					}
				}
			}
			
			@Override
			public void initialize(ScalableThreadpool<?> pool)
			{
				pool.setNumberOfWorkers(initialWorkerCount);
			}
		};
	}

	public void stop()
	{
		threadPool.stop();
	}
}
