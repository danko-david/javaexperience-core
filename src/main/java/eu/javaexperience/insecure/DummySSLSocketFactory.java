package eu.javaexperience.insecure;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DummySSLSocketFactory extends SSLSocketFactory
{
	private SSLSocketFactory factory;

	public DummySSLSocketFactory()
	{
		try
		{
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(
					null, // No KeyManager required
					new TrustManager[] { new DummyTrustManager() },
					new java.security.SecureRandom());
			factory = (SSLSocketFactory) sslcontext.getSocketFactory();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void acceptAllSsl() throws NoSuchAlgorithmException, KeyManagementException
	{
		TrustManager[] trustAllCerts = new TrustManager[]
		{
	       new X509TrustManager()
	       {
	          public java.security.cert.X509Certificate[] getAcceptedIssuers()
	          {
	            return null;
	          }

	          public void checkClientTrusted(X509Certificate[] certs, String authType) {}

	          public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	       }
	    };

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
	}

	
	public static SocketFactory getDefault() {
		return new DummySSLSocketFactory();
	}

	public Socket createSocket(Socket socket, String s, int i, boolean flag)
			throws IOException {
		return factory.createSocket(socket, s, i, flag);
	}

	public Socket createSocket(InetAddress inaddr, int i,
			InetAddress inaddr1, int j) throws IOException {
		return factory.createSocket(inaddr, i, inaddr1, j);
	}

	public Socket createSocket(InetAddress inaddr, int i)
			throws IOException {
		return factory.createSocket(inaddr, i);
	}

	public Socket createSocket(String s, int i, InetAddress inaddr, int j)
			throws IOException {
		return factory.createSocket(s, i, inaddr, j);
	}

	public Socket createSocket(String s, int i) throws IOException {
		return factory.createSocket(s, i);
	}

	public String[] getDefaultCipherSuites() {
		return factory.getSupportedCipherSuites();
	}

	public String[] getSupportedCipherSuites() {
		return factory.getSupportedCipherSuites();
	}
	
	public Socket createSocket() throws IOException
	{
		return factory.createSocket();
	};
}