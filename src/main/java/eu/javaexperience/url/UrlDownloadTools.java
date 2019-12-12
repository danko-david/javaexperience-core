package eu.javaexperience.url;

import static eu.javaexperience.log.LogLevel.*;
import static eu.javaexperience.log.LoggingTools.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.proxy.ProxyStorage;
import eu.javaexperience.proxy.TorProxySpawner.ProxySource;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.semantic.references.MayNull;

public class UrlDownloadTools
{
	protected static final Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("UrlDownloadTools"));
	
	public static byte[] download(String url) throws MalformedURLException, IOException
	{
		return download(url, null);
	}
	
	public static byte[] download(String url, @MayNull Map<String,String> headers) throws MalformedURLException, IOException
	{
		return download(url, headers, null);
	}
	
	public static byte[] download(Proxy proxy, String url, @MayNull Map<String,String> headers) throws MalformedURLException, IOException
	{
		return download(proxy, url, headers, null);
	}
	
	
	public static byte[] download(String url, @MayNull Map<String,String> headers, String post_data) throws MalformedURLException, IOException
	{
		return download(null, new URL(url), headers, -1, null == post_data?null: post_data.getBytes());
	}
	
	
	public static byte[] download(Proxy proxy, String url, @MayNull Map<String,String> headers, String post_data) throws MalformedURLException, IOException
	{
		return download(proxy, new URL(url), headers, -1, null == post_data?null: post_data.getBytes());
	}
	
	
	
	public static byte[] download(URL url, Map<String,String> headers) throws IOException
	{
		return download(null, url, headers, -1, null);
	}
	
	public static byte[] download(Proxy proxy, URL url, Map<String,String> headers) throws IOException
	{
		return download(proxy, url, headers, -1, null);
	}
	
	public static byte[] download(Proxy proxy, URL url, Map<String,String> headers, byte[] POST_data) throws IOException
	{
		return download(proxy, url, headers, 60_000, POST_data);
	}
	
	public static byte[] download(Proxy proxy, URL url, Map<String,String> headers, int timeoutMs, byte[] POST_data) throws IOException
	{
		URLConnection connection = null;
		
		if(null == proxy)
		{
			connection = url.openConnection();
		}
		else
		{
			connection = url.openConnection(proxy);
		}
		
		if(timeoutMs > 0)
		{
			connection.setConnectTimeout(timeoutMs);
		}

		if(null != headers)
		{
			for(Entry<String, String> header:headers.entrySet())
			{
				if(null != header.getValue())
				{
					connection.addRequestProperty(header.getKey(), header.getValue());
				}
			}
		}
		
		if(null != POST_data)
		{
			connection.addRequestProperty("Content-Length", String.valueOf(POST_data.length));
			connection.setDoOutput(true);
			try
			(
				OutputStream os = connection.getOutputStream();
			)
			{
				if(null != POST_data)
				{
					os.write(POST_data);
					os.flush();
				}
			}
		}
		
		try(InputStream is = connection.getInputStream())
		{
			int ep = 0;
			int read = 0;
			byte[] ret = new byte[10240];
			
			while((read = is.read(ret, ep, ret.length-ep)) >= 0)
			{
				if(ep + read == ret.length)
				{
					ret = Arrays.copyOf(ret, ret.length*2);
				}
				
				ep+= read;
			}
			
			HttpURLConnection conn = ((HttpURLConnection) connection);
			int status = conn.getResponseCode();
			if(status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
			{
				return download(proxy, new URL(conn.getHeaderField("Location")), headers, timeoutMs, POST_data);
			}

			return Arrays.copyOf(ret, ep);
		}
	}
	
	public static HttpRequestResult httpDownload(Proxy proxy, URL url, Map<String,String> headers, byte[] POST_data) throws IOException
	{
		return httpDownload(proxy, url, headers, 0, POST_data);
	}
	
	
	public static HttpRequestResult httpDownload(Proxy proxy, URL url, Map<String,String> headers, int timeoutMs, byte[] POST_data) throws IOException
	{
		URLConnection connection = null;
		
		if(null == proxy)
		{
			connection = url.openConnection();
		}
		else
		{
			connection = url.openConnection(proxy);
		}
		
		if(timeoutMs > 0)
		{
			connection.setConnectTimeout(timeoutMs);
		}
		
		//((HttpURLConnection) connection).setInstanceFollowRedirects(true);
		
		if(null != headers)
		{
			for(Entry<String, String> header:headers.entrySet())
			{
				if(null != header.getValue())
				{
					connection.addRequestProperty(header.getKey(), header.getValue());
				}
			}
		}
		
		if(null != POST_data)
		{
			connection.addRequestProperty("Content-Length", String.valueOf(POST_data.length));
			connection.setDoOutput(true);
			try
			(
				OutputStream os = connection.getOutputStream();
			)
			{
				if(null != POST_data)
				{
					os.write(POST_data);
					os.flush();
				}
			}
		}
		
		HttpURLConnection httpConn = (HttpURLConnection) connection; 
		
		HttpRequestResult res = new HttpRequestResult();
		
		res.responseCode = httpConn.getResponseCode();
		
		res.headers = httpConn.getHeaderFields();
		
		try
		{
			res.responseStatus = res.headers.get(null).get(0);
		}
		catch(Exception e){}
		
		try(InputStream is = 200 == res.responseCode?httpConn.getInputStream():httpConn.getErrorStream())
		{
			if(null != is)
			{
				int ep = 0;
				int read = 0;
				byte[] ret = new byte[10240];
				
				while((read = is.read(ret, ep, ret.length-ep))>0)
				{
					if(ep + read == ret.length)
					{
						ret = Arrays.copyOf(ret, ret.length*2);
					}
					
					ep+= read;
				}
				
				res.data = Arrays.copyOf(ret, ep);
			}
			else
			{
				res.data = Mirror.emptyByteArray;
			}
		}
		
		return res;
	}
	
	public static class HttpRequestResult
	{
		public String responseStatus;
		public int responseCode;
		public byte[] data;
		public Map<String, List<String>> headers;
	}
	
	public static void downloadPagesIntoParallelWithProxies
	(
		final Map<String, byte[]> dst,
		Collection<String> src,
		final ProxyStorage spawnerStorage,
		final int proxies,
		final int concurrency,
		final boolean skip_exists
	)
	{
		downloadPagesIntoParallelWithProxies(null, dst, src, null, spawnerStorage, proxies, concurrency, skip_exists);
	}
	
	public static void downloadPagesIntoParallelWithProxies
	(
		final @MayNull GetBy2<byte[], Proxy, URL> downloader,
		final Map<String, byte[]> dst,
		Collection<String> src,
		@MayNull Map<String, String> headers,
		final ProxyStorage spawnerStorage,
		final int proxies,
		final int concurrency,
		final boolean skip_exists
	)
	{
		final BlockingQueue<String> urls_queue = new LinkedBlockingQueue<>();
		urls_queue.addAll(src);
		
		final AtomicInteger nums = new AtomicInteger(concurrency);
		
		for(int i=0;i<concurrency;++i)
		{
			final int thread_ordinal = i;
			new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						String toDownload = null;
						while(null != (toDownload = urls_queue.poll()))
						{
							if(skip_exists)
							{
								if(dst.containsKey(toDownload))
								{
									continue;
								}
							}
							
							try
							{
								long t0 = System.currentTimeMillis();
								byte[] data = null;
								
								final int try_count = 10;
								for(int i=0;;++i)
								{
									try
									{
										ProxySource tp = spawnerStorage.getAtOffset(thread_ordinal % proxies);
										Proxy p = tp.getProxy();
										
										if(null == downloader)
										{
											data = download(p, toDownload, headers);
										}
										else
										{
											data = downloader.getBy(p, new URL(toDownload));
										}
										if(null != data)
										{
											break;
										}
									}
									catch(Exception e)
									{
										if(try_count == i)
										{
											tryLogFormat(LOG, WARNING, "Can't download URL \"%s\" %s", toDownload, e.getMessage());
											break;
											//throw e;
										}
									}
								}
								
								if(null != data)
								{
									tryLogFormat(LOG, MEASURE, "Url download \"%s\" took %s ms", toDownload, System.currentTimeMillis()-t0);
									dst.put(toDownload, data);
								}
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					catch(Throwable e)
					{
						e.printStackTrace();
					}
					
					synchronized (nums)
					{
						nums.decrementAndGet();
						nums.notifyAll();
					}
					
				};
			}.start();
		}
		
		synchronized (nums)
		{
			while(0 != nums.get())
			{
				try
				{
					nums.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void disableSslVerification()
	{
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
			};

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}
}
