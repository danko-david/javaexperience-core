package eu.javaexperience.insecure;
import javax.net.ssl.X509TrustManager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;


public class DummyTrustManager implements X509TrustManager, TrustManager
{
	public boolean isClientTrusted( X509Certificate[] cert)
	{
		return true;
	}
	
	public boolean isServerTrusted( X509Certificate[] cert)
	{
		return true;
	}
	
	public X509Certificate[] getAcceptedIssuers()
	{
		return new X509Certificate[ 0];
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
}