package eu.javaexperience.multithread.notify;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class WaitForSingleEvent
{
	protected final AtomicReference<Semaphore> parker =
			new AtomicReference<Semaphore>(new Semaphore(0)); 
	

	/**
	 * Wait until the anticipated event occurs.
	 * All the waiting thread notified about event,
	 * even on high concurrency, the {@link #evenOcurred()}
	 * returns true
	 * */
	public void waitForEvent() throws InterruptedException
	{
		//returns always true unless the Thread is interrupted
		parker.get().acquire();
	}
	
	/**
	 * returns true if event occurred, false on timeout.
	 * @throws InterruptedException 
	 * */
	public boolean waitForEvent(long timeout, TimeUnit unit) throws InterruptedException
	{
		return parker.get().tryAcquire(timeout, unit);
	}
	
	public void evenOcurred()
	{
		Semaphore release = parker.getAndSet(new Semaphore(0));
		release.release(Integer.MAX_VALUE);
	}
}
