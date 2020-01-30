package eu.javaexperience.multithread;

import static org.junit.Assert.assertEquals;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

public class TestTaskExecutorPool
{
	@Test
	public void testExecutionPoolMoveObjects() throws InterruptedException
	{
		Queue<String> to = new LinkedBlockingQueue<>();
		TaskExecutorPool pool = new TaskExecutorPool();
		
		for(int i=0;i<10000;++i)
		{
			int j = i;
			pool.execute(()->
			{
				to.add(String.valueOf(j));
			});
		}
		
		System.out.println(pool.getThreadsCount());
		Thread.sleep(500);
		
		assertEquals(10000, to.size());
	}
}
