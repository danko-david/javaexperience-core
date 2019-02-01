package eu.javaexperience.multithreading.pools;


import java.util.LinkedList;

import eu.javaexperience.interfaces.simple.SimpleCall;
import eu.javaexperience.multithread.MultithreadingTools;

public class TimeoutJobPool
{
	protected final Object wakeTimeout = new Object();
	
	protected static class TimeoutJob
	{
		long onTime;
		SimpleCall ac;
		
		public TimeoutJob(int dt, SimpleCall ac)
		{
			this.onTime = System.currentTimeMillis()+dt;
			this.ac = ac;
		}
	}

	protected LinkedList<TimeoutJob> timeoutJobs = new LinkedList<>();
	
	/**
	 * Visszadaja a következő feladatig kivárandó időt vagy {@link Integer#MAX_VALUE}-t
	 * */
	protected long getNextEventTime()
	{
		synchronized (timeoutJobs)
		{
			long max = Long.MAX_VALUE;
			for(TimeoutJob t:timeoutJobs)
				if(t.onTime<max)
					max = t.onTime;
			
			return max;
		}
	}
	
	protected TimeoutJob getNextJobOrNull()
	{
		synchronized (timeoutJobs)
		{
			long t0 = System.currentTimeMillis();
			for(int i=0;i<timeoutJobs.size();i++)
			{
				TimeoutJob t = timeoutJobs.get(i);
				if(t.onTime<=t0)
				{
					timeoutJobs.remove(i);
					return t;
				}
			}
			return null;
		}
	}
	
	public void putTimoutJob(int timeout,SimpleCall ac)
	{
		if(timeout>=0 && ac != null)
		synchronized (timeoutJobs)
		{
			timeoutJobs.add(new TimeoutJob(timeout,ac));
			synchronized(wakeTimeout)
			{
				wakeTimeout.notifyAll();
			}
		}
	}
	
	protected Thread timeoutWorker = new Thread()
	{
		public void run()
		{
			while(true)
			try
			{
				long w = getNextEventTime()-System.currentTimeMillis();
				if(w > 0)
					synchronized (wakeTimeout)
					{
						wakeTimeout.wait(w);
					}	
					
				TimeoutJob j = getNextJobOrNull();
				if(j != null)
					try//le ne tegye a lantot
					{
						j.ac.call();
					}
					catch(Throwable t)
					{
						//TODO jelentő
					}
			}
			catch(Throwable t)
			{
				if(t == MultithreadingTools.THREAD_SHUTDOWN_POISON)
					return;
				
				t.printStackTrace();
			}
		}
	};
	
	{
		timeoutWorker.setDaemon(true);
		timeoutWorker.start();
	}
}