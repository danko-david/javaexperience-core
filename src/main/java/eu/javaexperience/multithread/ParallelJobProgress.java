package eu.javaexperience.multithread;


import java.io.Serializable;
import java.util.Collection;

import eu.javaexperience.multithread.notify.WaitForEvents;

public class ParallelJobProgress<R> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParallelJobProgress(Collection<R> v,WaitForEvents wait)
	{
		this.wait = wait;
		ret = v;
	}
	
	protected Collection<R> ret;
	protected WaitForEvents wait;
	
	public Collection<R> getResultCollection()
	{
		return ret;
	}
	
	public Collection<R> returnOnEnd()
	{
		wait.waitForAllEvent();
		return ret;
	}
}	