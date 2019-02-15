package eu.javaexperience.proxy;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.proxy.TorProxySpawner.ProxySource;
import eu.javaexperience.proxy.TorProxySpawner.TorProxy;
import eu.javaexperience.reflect.Mirror;

public class TorSpawnerStorage implements ProxyStorage, Closeable
{
	protected TorProxySpawner spawner;
	protected int maxProxies;
	protected ConcurrentMap<Integer, TorProxy> proxies = new ConcurrentHashMap<Integer, TorProxySpawner.TorProxy>();
	protected long maxIdle = -1;
	
	
	public TorSpawnerStorage(TorProxySpawner spawn, int max, long maxIdleMs)
	{
		this.spawner = spawn;
		this.maxProxies = max;
		this.maxIdle = maxIdleMs;
	}
	
	public TorSpawnerStorage(TorProxySpawner spawn, int max)
	{
		this(spawn, max, -1);
	}
	
	@Override
	public synchronized ProxySource getAtOffset(int i) throws IOException
	{
		TorProxy tp = proxies.get(i);
		if(null == tp)
		{
			try
			{
				tp = spawner.spawnWithOffset(i);
			}
			catch (InterruptedException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
			}
			proxies.put(i, tp);
		}
		else
		{
			if(maxIdle > 0)
			{
				try
				{
					tp.withinTimeOrRestart(maxIdle);
				}
				catch (InterruptedException e)
				{
					Mirror.propagateAnyway(e);
				}
			}
		}
		
		return tp;
	}

	@Override
	public int size()
	{
		return maxProxies;
	}

	@Override
	public void close() throws IOException
	{
		for(Entry<Integer, TorProxy> kv:proxies.entrySet())
		{
			try
			{
				kv.getValue().stop();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
/*	public static void main(String[] args) throws Throwable
	{
		TODO make restart test
		new File("/tmp/tors").mkdirs();
		TorSpawnerStorage storage = new TorSpawnerStorage(TorProxySpawner.runtimeThrowCreate("/tmp/tors"), 10, 20000);
		TorProxy ps = (TorProxy) storage.getAtOffset(0);
		ps.waitPortOpen(9050, 10);
		
		ps.stop();
		while(true)
		{
			try
			{
				Socket s = new Socket("127.0.0.1", 9050);
				s.close();
				System.out.println("poll");
			}
			catch(Exception e)
			{
				System.out.println("miss");
			}
			Thread.sleep(200);
		}
		
	}*/
	
}
