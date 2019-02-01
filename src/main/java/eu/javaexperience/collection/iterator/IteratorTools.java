package eu.javaexperience.collection.iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.reflect.PrimitiveTools;

public class IteratorTools
{
	private IteratorTools(){}
	
	public static <T> Object[] arrayFromIterator(Iterator<T> it)
	{
		return asList(it).toArray();
	}

	public static <T> Object[] arrayFromIterator(Iterable<T> it)
	{
		return arrayFromIterator(it.iterator());
	}
	
	public static <T> List<T> asList(Iterable<T> it)
	{
		return asList(it.iterator());
	}
	
	public static <T> List<T> asList(Iterator<T> it)
	{
		ArrayList<T> ret = new ArrayList<>();
		while(it.hasNext())
		{
			ret.add(it.next());
		}
		return ret;
	}

	public static <T> Iterable<T> asList(T[] values)
	{
		ArrayList<T> ret = new ArrayList<>(values.length);
		for(T t:values)
		{
			ret.add(t);
		}
		
		return ret;
	}

	public static Iterable<Integer> asList(int[] values)
	{
		ArrayList<Integer> ret = new ArrayList<>(values.length);
		for(int t:values)
		{
			ret.add(t);
		}
		
		return ret;
	}

	public static <T> Iterator<T> wrapArray(final T[] creators)
	{
		return new Iterator<T>()
		{
			protected int i = 0;
			
			@Override
			public boolean hasNext()
			{
				return i < creators.length;
			}

			@Override
			public T next()
			{
				return creators[i++];
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("iterator remove not supported");
			}
		};
	}

	public static <T> Iterable<T> wrapIterator(final Iterator<T> it)
	{
		return ()->it;
	}

	public static <T> Iterable<T> fromEnumeration(final Enumeration<T> entries)
	{
		return wrapIterator(new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return entries.hasMoreElements();
			}

			@Override
			public T next()
			{
				return entries.nextElement();
			}
		});
	}

	public static <C> Iterable<C> tryIterate(Object content)
	{
		if(null == content)
		{
			return null;
		}
		
		Class<? extends Object> cls = content.getClass();
		
		if(Collection.class.isAssignableFrom(cls))
		{
			return (Iterable<C>) content;
		}
		
		if(Map.class.isAssignableFrom(cls))
		{
			return (Iterable<C>) ((Map)content).entrySet();
		}
		
		if(content.getClass().isArray())
		{
			//primitive or Object array?
			if(PrimitiveTools.isPrimitiveClass(cls))
			{
				return (Iterable<C>) wrapIterator(wrapArray(ArrayTools.extractPrimitiveArray(content)));
			}
			else
			{
				return (Iterable<C>) wrapIterator(wrapArray((Object[]) content));
			}
		}
		
		return null;
	}
}
