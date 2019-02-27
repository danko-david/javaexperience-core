package eu.javaexperience.multithread.notify;


import eu.javaexperience.interfaces.simple.SimpleCall;
import eu.javaexperience.reflect.Mirror;

public class WaitForEvents implements SimpleCall
{
	private Object waitLock = new Object();
	
	private Object nextWaitLock = new Object();
	
	private int numRequired = 0;
	
	private int numActual = 0;
	
	public WaitForEvents(int num)
	{
		numRequired = num;
	}
	
	public synchronized void resetCounter()
	{
		numActual = 0;
	}
	
	public synchronized int getTargetEventsNumber()
	{
		return numRequired;
	}

	public synchronized int getCurrentEventsNumbers()
	{
		return numActual;
	}
	
	public synchronized void endWait()
	{
		numActual = Integer.MAX_VALUE;
		synchronized (nextWaitLock)
		{
			nextWaitLock.notifyAll();
		}
		
		synchronized (waitLock)
		{
			waitLock.notifyAll();
		}
	}
	
	private synchronized void inc()
	{
		synchronized (nextWaitLock)
		{
			numActual++;
			nextWaitLock.notifyAll();
	
			if(numActual == numRequired)
				synchronized (waitLock)
				{
					waitLock.notifyAll();
				}
		}
	}

	/*@Override
	public void actionPerformed(ActionEvent e)
	{
		inc();
	}*/
	
	public void waitForAllEvent()
	{
		waitForAllEvent(0);
	}
	
	public void waitForAllEvent(long msTimeout)
	{
		synchronized (waitLock)
		{
			try
			{
				while(numActual < numRequired)
					waitLock.wait(msTimeout);
			}
			catch (Exception e)
			{}
		}
		
	}
	
	public void waitForNextEvent()
	{
		waitForNextEvent(0);
	}
	
	public void waitForNextEvent(long msTimeout)
	{
		synchronized (nextWaitLock)
		{
			try
			{
				nextWaitLock.wait(msTimeout);
			}
			catch (Exception e)
			{
				Mirror.propagateAnyway(e);
			}
		}
	}

	@Override
	public void call()
	{
		inc();
	}
}