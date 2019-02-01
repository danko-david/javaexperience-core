package eu.javaexperience.reference;

import java.lang.ref.SoftReference;

public class StrongReference<T> extends SoftReference<T>
{
	protected final T obj;
	public StrongReference(T referent)
	{
		super(referent);
		obj = referent;
	}

	@Override
	public T get()
	{
		return obj;
	}
}