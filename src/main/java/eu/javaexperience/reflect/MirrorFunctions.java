package eu.javaexperience.reflect;

import java.lang.reflect.Field;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class MirrorFunctions
{
	public static <T> GetBy1<T, Object> createGetter(final Field f)
	{
		return new GetBy1<T, Object>()
		{
			@Override
			public T getBy(Object a)
			{
				try
				{
					return (T) f.get(a);
				}
				catch(Exception e)
				{
					return null;
				}
			}
		};
	}
}
