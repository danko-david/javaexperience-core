package eu.javaexperience.pdw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;

import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.exceptions.IllegalOperationException;
import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.generic.annotations.Ignore;
import eu.javaexperience.io.file.FileTools;

public class ProxyDataWrapperTest
{
	public static interface LazyImplInterface
	{
		public String getPath();
		
		public boolean isActive();
		
		public <T extends LazyImplInterface> T cast(Class<T> dst);
		public Object getProp(String key);
		
		public boolean hasProp(String key);
		
		public void setProp(String key, Object value);
	}
	
	public static class LazyImplObject implements LazyImplInterface
	{
		protected String path;
		protected Map<String, Object> values = new SmallMap<>();
		
		public LazyImplObject(String path)
		{
			this.path = path;
		}
		
		public void setProp(String key, Object value)
		{
			values.put(key, value);
		}
		
		public Object getProp(String key)
		{
			return values.get(key);
		}
		
		public boolean hasProp(String key)
		{
			return values.containsKey(key);
		}
		
		@Override
		public String getPath()
		{
			return path;
		}

		@Override
		public boolean isActive()
		{
			return true;
		}

		@Ignore
		@Override
		public <T extends LazyImplInterface> T cast(Class<T> dst)
		{
			return null;
		}
	}
	
	public static interface LazyImplRoot extends LazyImplInterface
	{
		public LazyImplLvl1 getLvl1();
		
		//error
		public LazyImplLvl1 setLvl1();
		
		//error
		public LazyImplLvl1 isLvl1();
		
		public LazyImplLvl1 get();
		
		//error
		public byte[] a();
	}
	
	public static interface LazyImplLvl1 extends LazyImplInterface{}
	
	public static interface LazyImplWildcard extends LazyImplInterface{}
	
	public static ProxyHelpedLazyImplementation<LazyImplInterface, LazyImplObject, LazyImplRoot> create() throws Throwable
	{
		return new ProxyHelpedLazyImplementation<LazyImplInterface, LazyImplObject, LazyImplRoot>(LazyImplInterface.class, new LazyImplObject("/"), LazyImplRoot.class)
		{
			@Override
			public Object handleInterfaceCall(LazyImplObject root, Method method, Object[] params) throws Throwable
			{
				if(method.getName().equals("cast"))
				{
					return wrapWithClass((Class)params[0], root);
				}
				
				if(LazyImplInterface.class.isAssignableFrom(method.getReturnType()))
				{
					String[] cmd = BeanTools.decomposeCommand(method.getName());
					if(cmd.length < 2)
					{
						throw new IllegalOperationException("Plain operations are not implemented.");
					}
					
					if(!"get".equals(cmd[0]))
					{
						throw new UnimplementedCaseException("Only get <T extends LazyImplInterface> implemented.");
					}
					
					return wrapWithClass
					(
						(Class) method.getReturnType(),
						new LazyImplObject(FileTools.normalizeSlashes(root.path+"/"+BeanTools.getCLikeBeanNameFromMethodName(method.getName())))
					);
				}
				
				throw new RuntimeException("This is just a test case. What you've expected? :)");
			}
		};
	}
	
	@Test
	public void testPhli1() throws Throwable
	{
		ProxyHelpedLazyImplementation<LazyImplInterface, LazyImplObject, LazyImplRoot> h = create();
		
		LazyImplRoot root = h.getRoot();
		assertEquals("/", root.getPath());
		assertEquals(true, root.isActive());
		assertTrue(root.cast(LazyImplWildcard.class) instanceof LazyImplWildcard);
		
		LazyImplLvl1 lvl1 = root.getLvl1();
		
		assertTrue(lvl1 instanceof LazyImplLvl1);
		assertTrue(lvl1 instanceof LazyImplInterface);
		assertEquals("/lvl1", lvl1.getPath());
	}
	
	@Test(expected=RuntimeException.class)
	public void testPhli2() throws Throwable
	{
		ProxyHelpedLazyImplementation<LazyImplInterface, LazyImplObject, LazyImplRoot> h = create();
		
		h.getRoot().a();
	}
	
	@Test(expected=UnimplementedCaseException.class)
	public void testPhli3() throws Throwable
	{
		ProxyHelpedLazyImplementation<LazyImplInterface, LazyImplObject, LazyImplRoot> h = create();
		h.getRoot().setLvl1();
	}
	
	@Test(expected=IllegalOperationException.class)
	public void testPhli4() throws Throwable
	{
		ProxyHelpedLazyImplementation<LazyImplInterface, LazyImplObject, LazyImplRoot> h = create();
		h.getRoot().get();
	}
}
