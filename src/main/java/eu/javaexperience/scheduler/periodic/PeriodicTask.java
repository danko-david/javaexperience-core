package eu.javaexperience.scheduler.periodic;


import java.util.concurrent.atomic.AtomicReference;

import eu.javaexperience.Global.LazySingletons;
import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimpleCall;

public class PeriodicTask
{
	public class PendingTask
	{
		protected boolean mayRun = true;
		public final long runOnTime;
		
		protected PendingTask(long runOnTime)
		{
			this.runOnTime = runOnTime;  
		}
		
		public SimpleCall getCallable()
		{
			return call;
		}
	}
	
	protected AtomicReference<PendingTask> nextTask = new AtomicReference<>(); 
	
	protected volatile boolean run = false;
	protected int interval;
	
	protected final SimpleCall call;
	
	public PeriodicTask(SimpleCall call,int milisec)
	{
		AssertArgument.assertNotNull(this.call = call, "call");
		AssertArgument.assertGreaterThan(this.interval = milisec, 0, "interval in milisec");
	}
	
	public boolean isRunning()
	{
		return run;
	}
	
	public void start()
	{
		if(run)
			throw new IllegalStateException("Periodic task is already started");
		
		PendingTask task = nextTask.get();
		if(task != null)
			task.mayRun = false;
		
		run = true;
		
		enqueueNew();
	}
	
	protected void enqueueNew()
	{
		enqueueNewWithInterval(interval);
	}
	
	protected void enqueueNewWithInterval(int interval)
	{
		final PendingTask ptask = new PendingTask(System.currentTimeMillis()+interval);
		nextTask.set(ptask);
		LazySingletons.timeoutJobPool().putTimoutJob(interval, new SimpleCall()
		{
			@Override
			public void call()
			{
				if(ptask.mayRun)
				{
					try
					{
						ptask.getCallable().call();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					enqueueNew();
				}
			}
		});
	}
	
	public void stop()
	{
		if(!run)
			throw new IllegalStateException("Periodic task is already stopped");
		
		run = false;
		
		PendingTask task = nextTask.get();
		if(task != null)
			task.mayRun = false;
	}
	
	/**
	 * Ha rövidebb időt adunk meg mint az előző és már letelt az előző ütemezés + interval
	 * akkor most azonnal (de másik szálon) lefuttatjuk és utána az új intervallum lesz érvényben.
	 * 
	 * Ha hosszabb időt adunk meg, akkor még a következőt feladatot is tovább várakoztatjuk.
	 * 
	 * Minden beavatkozás nélkül visszatér ha a milisec < 0 vagy ugyanaz mint amennyi most be van állítva.
	 * */
	public void adjustInterval(int milisec)
	{
		if(milisec < 0 || interval == milisec)
			return;
		
		PendingTask task = nextTask.get();
		if(task != null)
		{
			long commandTime = task.runOnTime - interval;
			interval = milisec;
			if(commandTime <= System.currentTimeMillis())
			{
				LazySingletons.timeoutJobPool().putTimoutJob(0, new SimpleCall()
				{
					@Override
					public void call()
					{
						call.call();
					}
				});
				enqueueNew();
			}
			else
			{
				long inter = (commandTime + milisec) - System.currentTimeMillis();
				enqueueNewWithInterval((int)(inter < 0? 0:inter));
			}
		}
		else if(run)
			enqueueNew();
	}
}