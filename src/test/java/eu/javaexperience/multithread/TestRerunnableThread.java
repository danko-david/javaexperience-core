package eu.javaexperience.multithread;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

public class TestRerunnableThread
{
	protected static RerunnableThread<Integer> createSleeper() throws InterruptedException, TimeoutException
	{
		RerunnableThread<Integer> ret = new RerunnableThread<Integer >()
		{
			@Override
			public void runThis(Integer param) throws Throwable
			{
				Thread.sleep(param);
			}
		};
		ret.waitFree(1, TimeUnit.SECONDS);
		return ret;
	}
	
	@Test
	public void testAccept() throws Throwable
	{
		RerunnableThread<Integer> rrt = createSleeper();
		assertTrue(rrt.tryRerun(100));
		assertFalse(rrt.tryRerun(500));
		Thread.sleep(150);
		assertTrue(rrt.tryRerun(100));
	}
}
