package eu.javaexperience.multithread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;

public class TaskExecutorPool implements Executor
{
	protected final Collection<RerunnableThread<Runnable>> execs = new ArrayList<>();
	
	protected RerunnableThread<Runnable> executorCreator()
	{
		return new RerunnableThread<Runnable>()
		{
			@Override
			public void runThis(Runnable param) throws Throwable
			{
				param.run();
			}
		};
	}
	
	public synchronized void execute(Runnable exec)
	{
		for(RerunnableThread<Runnable> e:execs)
		{
			if(e.rerunIfFreeWithParam(exec))
			{
				return;
			}
		}
		
		RerunnableThread<Runnable> r = executorCreator();
		r.rerunWithParam(exec);
		execs.add(r);
	}
}