package eu.javaexperience.server;

import eu.javaexperience.io.IOStream;
import eu.javaexperience.io.IOStreamServer;
import eu.javaexperience.multithread.BlockingJob;
import eu.javaexperience.multithread.ScalableThreadpoolManageStrategy;

public abstract class AbstractServer<S extends IOStream> extends AbstractJobServer<S>
{
	public AbstractServer(IOStreamServer<S> srv, ScalableThreadpoolManageStrategy poolStrategy)
	{
		super(srv, poolStrategy);
	}
	
	public AbstractServer(IOStreamServer<S> srv, int initialWorkerCount)
	{
		super(srv, initialWorkerCount);
	}
	
	@Override
	protected BlockingJob<S> initalizeJob()
	{
		return new BlockingJob<S>()
		{
			@Override
			public S acceptJob() throws Throwable
			{
				S ret = srv.accept();
				manageLoad();
				return ret; 
			}
			
			@Override
			public void exec(S subject) throws Throwable
			{
				execute(subject);
				manageLoad();
			}
		};
	}
	
	protected abstract void execute(S subject);
}
