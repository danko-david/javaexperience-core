package eu.javaexperience.pdw;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import eu.javaexperience.interfaces.simple.getBy.GetBy3;


/**
 * Create data accessor from Beam Interfaces:
 * 
 * public DA
 * {
 * 	public String getName();
 * 	public DAProcess getProcess();//get the key named process and creates a new proxy with the DAProcess interface  
 * }
 * 
 * */
public class ProxyDataWrapperTools
{
	public static <W, R> W wrapAccessor(final R rootObject, Class<W> rootWrapperInterface, final GetBy3<Object, R, Method, Object[]> mapper)
	{
		return (W) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{rootWrapperInterface}, new InvocationHandler()
		{
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
			{
				return mapper.getBy(rootObject, method, args);
			}
		});
	}
}