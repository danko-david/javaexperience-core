package eu.javaexperience.multithread.notify;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import eu.javaexperience.interfaces.simple.SimpleCall;

public class WaitForEvents implements Serializable/*Nem sok értelme van,de legalább amiben ez benne van nem nyíg*/, SimpleCall
{
	private static final long serialVersionUID = 1L;

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
		synchronized (waitLock)
		{
			try
			{
				while(numActual < numRequired)
					waitLock.wait();
			}
			catch (Exception e)
			{}

		}
		
	}
	
	public void waitForNextEvent()
	{
		synchronized (nextWaitLock)
		{
			try
			{
				nextWaitLock.wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			};
		}
	}

	@Override
	public void call()
	{
		inc();
	}
}