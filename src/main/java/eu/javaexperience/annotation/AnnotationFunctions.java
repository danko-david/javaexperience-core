package eu.javaexperience.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public class AnnotationFunctions
{
	public static <T, C extends Annotation> GetBy1<T, AccessibleObject> examineAnnotationValue(final Class<C> annotationClass, final String fieldMethodName)
	{
		return new GetBy1<T, AccessibleObject>()
		{
			@Override
			public T getBy(AccessibleObject a)
			{
				C[] cs = a.getDeclaredAnnotationsByType(annotationClass);
				if(cs.length > 0)
				{
					try
					{
						Method m = annotationClass.getDeclaredMethod(fieldMethodName);
						if(null != m)
						{
							return (T) m.invoke(cs[0]);
						}
					}
					catch(Exception e)
					{
						Mirror.propagateAnyway(e);
					}
				}
				
				return null;
			}
		};
	}
}
