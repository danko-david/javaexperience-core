package eu.javaexperience.multithread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import eu.javaexperience.reflect.Mirror;

public class TaskExecutorPool implements Executor
{
	protected final Collection<RerunnableThread<Runnable>> execs = new ArrayList<>();
	
	protected RerunnableThread<Runnable> executorCreator()
	{
		RerunnableThread<Runnable> ret = new RerunnableThread<Runnable>()
		{
			@Override
			public void runThis(Runnable param) throws Throwable
			{
				param.run();
			}
		};
		try
		{
			ret.waitFree(1, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			Mirror.propagateAnyway(e);
		}
		return ret;
	}
	
	public synchronized void execute(Runnable exec)
	{
		for(RerunnableThread<Runnable> e:execs)
		{
			if(e.tryRerun(exec))
			{
				return;
			}
		}
		
		RerunnableThread<Runnable> r = executorCreator();
		r.tryRerun(exec);
		execs.add(r);
	}

	public synchronized int getThreadsCount()
	{
		return execs.size();
	}
}