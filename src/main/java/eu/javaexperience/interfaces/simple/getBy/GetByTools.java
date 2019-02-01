package eu.javaexperience.interfaces.simple.getBy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Properties;

import eu.javaexperience.collection.enumerations.EnumTools;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.reflect.Mirror;

public class GetByTools
{
	public static final GetBy1[] emptyGetBy1Array = new GetBy1[0];

	public static <T, P> GetBy1<T, P> alwaysReturnWith(final T obj)
	{
		return new GetBy1<T, P>()
		{
			@Override
			public T getBy(Object a)
			{
				return obj;
			}
		};
	}
	
	public static <R,P> R tryGet(GetBy1<R, P> getBy, P param)
	{
		if(null == getBy)
		{
			return null;
		}
		else
		{
			return getBy.getBy(param);
		}
	}
	
	private static final GetBy1<Object, Object> PASS_TROUGH = new GetBy1<Object, Object>()
	{
		@Override
		public Object getBy(Object a)
		{
			return a;
		}
	}; 
	
	public static <T> GetBy1 getPassTrought()
	{
		return (GetBy1<T, T>) PASS_TROUGH;
	}
	
	public static <A> GetBy1<A, A> chain(final GetBy1<A, A>... processors)
	{
		return new GetBy1<A, A>()
		{
			@Override
			public A getBy(A a)
			{
				A ret = a;
				for(GetBy1<A, A> proc:processors)
				{
					ret = proc.getBy(ret);
				}
				
				return ret;
			}
		};
	}
	
	public static <R, A, M> GetBy1<R, A> bind(final GetBy1<R, M> a, final GetBy1<M, A> b)
	{
		return new GetBy1<R, A>()
		{
			@Override
			public R getBy(A param)
			{
				return a.getBy(b.getBy(param));
			}
		};
	}
	
	public static <R, A, M, N> GetBy1<R, A> bind(final GetBy1<R, M> a, final GetBy1<M, N> b, final GetBy1<N, A> c)
	{
		return new GetBy1<R, A>()
		{
			@Override
			public R getBy(A param)
			{
				return a.getBy(b.getBy(c.getBy(param)));
			}
		};
	}

	public static <R> GetBy1<R, ?> wrapSimpleGet(final SimpleGet<R> simpleGet)
	{
		return new GetBy1<R, Object>()
		{
			@Override
			public R getBy(Object a)
			{
				return simpleGet.get();
			}
		};
	}

	public static <C, R> GetBy1<R, C> wrapInstancePublicGetterMethod
	(
		Class<C> cls,
		String method
	)
	{
		for(Method m:Mirror.getClassData(cls).getAllMethods())
		{
			if(EnumTools.isSet(m.getModifiers(), Modifier.PUBLIC) && !Mirror.isStatic(m))
			{
				if(m.getName().equals(method))
				{
					final Method tar = m;
					return new GetBy1<R, C>()
					{
						@Override
						public R getBy(C a)
						{
							try
							{
								return (R) tar.invoke(a);
							}
							catch (Exception e)
							{
								Mirror.propagateAnyway(e);
								return null;
							}
						}
					};
				}
			}
		}
		
		return null;
	}

	public static <K,V> GetBy1<V, K> wrapMap(final Map<K, V> map)
	{
		return new GetBy1<V, K>()
		{
			@Override
			public V getBy(K a)
			{
				return map.get(a);
			}
		};
	}
}
