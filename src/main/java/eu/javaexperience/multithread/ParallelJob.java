package eu.javaexperience.multithread;

import java.util.Vector;

import eu.javaexperience.multithread.notify.WaitForEvents;

public abstract class ParallelJob<P,R> implements Job<P>
{
	private WaitForEvents wait;
	private Vector<R> ret;
	
	@Override
	public void exec(P param)
	{
		try
		{
			R cucc = execute(param);
			if(cucc != null)
				ret.add(cucc);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			throw t;
		}
		finally
		{
			wait.call();
		}
	}
	
	protected abstract R execute(P param);
	
	ParallelJob<P,R> putParallelInternalArgs(WaitForEvents wait,Vector<R> v)
	{
		this.wait = wait;
		ret = v;
		return this;
	}
}