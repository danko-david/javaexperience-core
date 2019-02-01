package eu.javaexperience.multithread;

public class ThreadTool
{
	public static <T> Thread postNewJob(final Job<T> logic, final T param)
	{
		Thread t = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					logic.exec(param);
				}
				catch(Throwable t)
				{
					MultithreadingTools.topLevelException(t);
				}
			}
		};
		t.start();
		
		return t;
	}
}
