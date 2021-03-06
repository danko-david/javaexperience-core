package eu.javaexperience.proxy;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.io.IOTools;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.StringTools;

public class TorProxySpawner
{
	public final String root;
	
	/**
	 * @throws IOException 
	 * 
	 * */
	public TorProxySpawner(String root) throws IOException
	{
		AssertArgument.assertNotNull(this.root = new File(root).getCanonicalFile().toString(), "root_working_directory_path");
	}
	
	private static String proto =
	"# This file was generated by Tor; if you edit it, comments will not be preserved\n"+
	"# The old torrc file was renamed to torrc.orig.1 or similar, and Tor will ignore it\n"+
	""+
	"ControlSocket $root$/$off$/control\n"+
	"DataDirectory $root$/$off$/data\n"+
	"\n"+
	"#9030+offset\n"+
	"DirPort $dp$\n"+
	"\n"+
	"DirReqStatistics 0\n"+
	"ExitPolicy reject *:*\n"+
	"Log notice stdout\n"+
	"Nickname Unnamed\n"+
	"\n"+
	"#9001+offset\n"+
	"ORPort $op$\n"+

	"#9050+offset\n"+
	"SOCKSPort $sp$\n"+
	
	"\n"+
	//disable publishing on pub lic tor list
	"PublishServerDescriptor 0\n"+
	//disable exit traffic
	//"ExitRelay 0\n"+
	//disable UPnP
	"PortForwarding 0\n"+

	"RelayBandwidthBurst 524288000\n"+
	"RelayBandwidthRate 524288000\n";
	
	public TorProxy spawnWithOffset(int off) throws InterruptedException, IOException
	{
		TorProxy tp = new TorProxy();
		tp.offset = off;
		tp.setup();
		tp.start();
		
		if(tp.waitPortOpen(9050+off, 30))
		{
			tp.updateAccessTimeNow();
			return tp;
		}
		else
		{
			return null;
		}
	}
	
	public static final Process exec(boolean wait, String... args) throws IOException, InterruptedException
	{
		ProcessBuilder pb = new ProcessBuilder(args);
		Process p = pb.start();
		if(wait)
		{
			p.waitFor();
		}
		
		return p;
	}
	
	public static Proxy proxyForLocalPort(int port, boolean http)
	{
		return new Proxy(http?Proxy.Type.HTTP:Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("127.0.0.1", port));
	}
	
	public static interface ProxySource
	{
		public String getAddress();
		public int getPort();
		public Proxy.Type getType();
		public Proxy getProxy();
	}
	
	public static abstract class AbstractProxySource implements ProxySource
	{
		@Override
		public Proxy getProxy()
		{
			return new Proxy(getType(), InetSocketAddress.createUnresolved(getAddress(), getPort()));
		}
		
		public static ProxySource createLocalSocksProxy(final int port)
		{
			return new AbstractProxySource()
			{
				@Override
				public Type getType()
				{
					return Type.SOCKS;
				}
				
				@Override
				public int getPort()
				{
					return port;
				}
				
				@Override
				public String getAddress()
				{
					return "127.0.0.1";
				}
			};
		}
		
	}
	
	public class TorProxy extends AbstractProxySource
	{
		protected int offset;
		protected long lastAccess = 0;
		
		public long getLastAccessTime()
		{
			return lastAccess;
		}
		
		public void updateAccessTimeNow()
		{
			lastAccess = System.currentTimeMillis();
		}
		
		public void setup() throws IOException, InterruptedException
		{
			String conf = StringTools.replaceAllStrings(proto, "$off$", String.valueOf(offset));
			/*conf = Strings.replaceAllStrings(conf, "$dp$", String.valueOf(9030+offset));
			conf = Strings.replaceAllStrings(conf, "$op$", String.valueOf(9001+offset));
			conf = Strings.replaceAllStrings(conf, "$sp$", String.valueOf(9050+offset));*/

			
			conf = StringTools.replaceAllStrings(conf, "$dp$", String.valueOf(10001+offset));
			conf = StringTools.replaceAllStrings(conf, "$op$", String.valueOf(12001+offset));
			conf = StringTools.replaceAllStrings(conf, "$sp$", String.valueOf(9050+offset));
			
			conf = StringTools.replaceAllStrings(conf, "$root$", root);
			IOTools.putFileContent(root+"/cfg"+offset, false, conf.getBytes());
			
			File r = new File(root+"/"+offset);
			r.mkdirs();
			exec(true, "chmod", "700", r.toString());
			new File(root+"/"+offset+"/data").mkdir();
		}
		
		protected Process process = null;
		
		public void start() throws IOException, InterruptedException
		{
			if(null != process)
			{
				throw new RuntimeException("Tor process alredy started");
			}
			process = exec(false, "tor", "-f", root+"/cfg"+offset);
		}
		
		public void stop() throws InterruptedException
		{
			process.destroy();
			process.waitFor();
			process = null;
		}
		
		public boolean waitPortOpen(int port, int max_try) throws InterruptedException
		{
			boolean conn = false;
			int i=0;
			while(!conn && i++ < max_try)
			{
				try
				{
					Socket s = new Socket("127.0.0.1", port);
					conn = true;
					s.close();
					System.out.println("proxy ready");
					break;
				}
				catch(Exception e)
				{
					System.out.println("wait");
				}
				Thread.sleep(500);
			}
			
			System.out.println(port+". Tor proxy started: "+conn);
			return conn;
		}
		
		public void withinTimeOrRestart(long milisecs) throws IOException, InterruptedException
		{
			if(lastAccess + milisecs < System.currentTimeMillis())
			{
				//reboot
				stop();
				start();
				waitPortOpen(9050+offset, 30);
			}
		}
		
		@Override
		public Proxy getProxy()
		{
			return proxyForLocalPort(9050+offset, false);
		}

		@Override
		public int getPort()
		{
			return 9050+offset;
		}

		@Override
		public String getAddress()
		{
			return "127.0.0.1";
		}

		@Override
		public Type getType()
		{
			return Type.SOCKS;
		}
	}

	public static TorProxySpawner runtimeThrowCreate(String path)
	{
		try
		{
			return new TorProxySpawner(path);
		}
		catch (IOException e)
		{
			Mirror.throwSoftOrHardButAnyway(e);
			return null;
		}
	}
}