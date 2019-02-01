package eu.javaexperience.collection.enumerations;

import java.util.Enumeration;

public class EmptyEnumeration<T> implements Enumeration<T>
{
	public static final EmptyEnumeration instance = new EmptyEnumeration<>();
	
	@Override
	public boolean hasMoreElements()
	{
		return false;
	}

	@Override
	public T nextElement()
	{
		return null;
	}
};