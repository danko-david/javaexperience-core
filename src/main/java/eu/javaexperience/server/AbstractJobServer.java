package eu.javaexperience.server;

import java.util.HashMap;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.collection.map.RWLockMap;
import eu.javaexperience.io.IOStream;
import eu.javaexperience.io.IOStreamServer;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.multithread.ScalableBlockingJobExecutor;
import eu.javaexperience.multithread.ScalableThreadpoolManageStrategy;
import eu.javaexperience.settings.TunableVariable;

public abstract class AbstractJobServer<T extends IOStream> extends ScalableBlockingJobExecutor<T>
{
	protected final IOStreamServer<T> srv;
	
	protected RWLockMap<String, TunableVariable<?>> serverConfigVairables = 
		new RWLockMap<String, TunableVariable<?>>(new HashMap<String, TunableVariable<?>>());
	
	protected static Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("AbstractJobServer"));
	
	public AbstractJobServer(IOStreamServer<T> srv, ScalableThreadpoolManageStrategy poolStrategy)
	{
		super(poolStrategy);
		AssertArgument.assertNotNull(this.srv = srv, "server");
	}
	
	public AbstractJobServer(IOStreamServer<T> srv, int initialWorkerCount)
	{
		this(srv, getDefault(initialWorkerCount));
	}
}
