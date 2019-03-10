package eu.javaexperience.pdw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.generic.annotations.Ignore;
import eu.javaexperience.interfaces.simple.getBy.GetBy3;
import eu.javaexperience.reflect.Mirror;

public abstract class ProxyHelpedLazyImplementation<I, B extends I, R extends I> //I => ZkjeInterface, Base => ZookeeperPath
{
	protected final Set<String> backendImplMethodNames = new HashSet<>();
	
	protected Class<I> interfaceClass;
	protected B lazyImplObject;
	protected Class<R> rootInterface;
	protected R root;
	
	public ProxyHelpedLazyImplementation(Class<I> interfaceClass, B lazyImplObject, Class<R> rootObjectInterface) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		AssertArgument.assertTrue(interfaceClass.isInterface(), "interfaceClass must be and interface.");
		AssertArgument.assertNotNull(lazyImplObject, "lazyImplObject");
		AssertArgument.assertTrue(rootObjectInterface.isInterface(), "rootObjectInterface must be and interface.");
		this.interfaceClass = interfaceClass;
		this.rootInterface = rootObjectInterface;
		
		this.lazyImplObject = lazyImplObject;
		//for(Class i:Mirror.getClassData(lazyImplObject.getClass()).getAllSuperClassAndInterfaces())
		{
			for(Method m:lazyImplObject.getClass().getDeclaredMethods())
			{
				if(null == m.getAnnotation(Ignore.class))
				{
					backendImplMethodNames.add(m.getName());
				}
			}
		}
		
		root = wrapWithClass(rootInterface, lazyImplObject);
	}
	
	/**
	 * Relays the call to the "base object"
	 * 
	 * */
	protected final GetBy3<Object, B, Method, Object[]> mapper = (root, b, c)->
	{
		String methodName = b.getName();
		
		try
		{
			//top interface functions:
			if(backendImplMethodNames.contains(methodName))
			{
				return relayApiCall(root, b, c);
			}
			
			if("toString".equals(methodName))
			{
				return "Proxy generated ProxyHelpedLazyImplementation accessor for object: "+lazyImplObject+", with class: "+root.getClass().getSimpleName();
			}
			
			return handleInterfaceCall(root, b, c);
		}
		catch(Throwable e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	};

	public abstract Object handleInterfaceCall(B root, Method method, Object[] params) throws Throwable;

	public <T extends I> T createInstanceWithDefaultConstructor(Class<T> cls, B zkp) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Constructor<?> constr = cls.getConstructor(lazyImplObject.getClass());
		if(null == constr)
		{
			throw new RuntimeException("No constructor "+cls.getSimpleName()+"("+lazyImplObject.getClass().getSimpleName()+" root) defined in the requested class ("+cls.getSimpleName()+").");
		}
		
		return (T) constr.newInstance(zkp);
	}
	
	public <T extends I> T wrapWithClass(Class<T> cls, B elem) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if(cls.isInterface())
		{
			return ProxyDataWrapperTools.wrapAccessor(elem, cls, mapper);
		}
		else
		{
			return createInstanceWithDefaultConstructor(cls, elem);
		}
	}
	
	protected Object relayApiCall(B path, Method b, Object[] c) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		return lazyImplObject.getClass().getDeclaredMethod(b.getName(), b.getParameterTypes()).invoke(path, c);
	}
	
	public R getRoot()
	{
		return root;
	}
}
