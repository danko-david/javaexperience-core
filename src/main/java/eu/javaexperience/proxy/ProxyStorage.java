package eu.javaexperience.proxy;

import java.io.IOException;

import eu.javaexperience.proxy.TorProxySpawner.ProxySource;

public interface ProxyStorage
{
	public ProxySource getAtOffset(int i) throws IOException;
	public int size();
}
