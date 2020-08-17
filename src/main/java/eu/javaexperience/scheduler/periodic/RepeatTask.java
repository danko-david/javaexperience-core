package eu.javaexperience.scheduler.periodic;

import eu.javaexperience.interfaces.simple.SimpleCall;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingTools;

public class RepeatTask
{
	protected static Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("RepeatAction"));
	
	protected SimpleCall action;
	protected volatile int interval = 1000;
	protected volatile boolean running = false;
	protected volatile long prevAction = 0;
	protected volatile long nextAction = 0;
	protected volatile Thread runner;
	
	public RepeatTask(SimpleCall action)
	{
		this.action = action;
	}
	
	public void setInterval(int interval)
	{
		if(interval < 0)
		{
			interval = 0;
		}
		
		this.interval = interval;
		nextAction = prevAction + interval;
	}
	
	public int getInterval()
	{
		return interval;
	}
	
	protected boolean needExit(Thread t)
	{
		return !running || this.runner != t;
	}
	
	protected Thread createRunnerThread()
	{
		return new Thread()
		{
			@Override
			public void run()
			{
				while(true)
				{
					if(needExit(this))
					{
						return;
					}
					try
					{
						action.call();
					}
					catch(Exception e)
					{
						LoggingTools.tryLogFormatException(LOG, LogLevel.ERROR, e, "Exception occurred while executing action. `");
					}

					if(needExit(this))
					{
						return;
					}

					prevAction = System.currentTimeMillis();
					nextAction = prevAction+interval;
					while(System.currentTimeMillis() < nextAction)
					{
						long sleep = Math.min(500, nextAction-System.currentTimeMillis());
						try
						{
							if(sleep > 0)
							{
								Thread.sleep(sleep);
							}
							if(needExit(this))
							{
								return;
							}
						}
						catch (InterruptedException e)
						{
						}
					}
				}
			}
		};
	}
	
	public boolean start()
	{
		if(running)
		{
			return false;
		}
		prevAction = System.currentTimeMillis();
		nextAction = System.currentTimeMillis()+interval;
		running = true;
		runner = createRunnerThread();
		runner.start();
		return true;
	}
	
	public boolean stop()
	{
		if(!running)
		{
			return false;
		}
		runner = null;
		running = false;
		return true;
	}
	
	public boolean isRunning()
	{
		return running;
	}
}