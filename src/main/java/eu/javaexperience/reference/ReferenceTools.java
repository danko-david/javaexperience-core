package eu.javaexperience.reference;

import java.util.concurrent.atomic.AtomicReference;

public class ReferenceTools
{
	public static <T> void assingOnce(AtomicReference<T> ref, T value)
	{
		if(!ref.compareAndSet(null, value))
		{
			throw new RuntimeException(value+" already set.");
		}
	}
}
