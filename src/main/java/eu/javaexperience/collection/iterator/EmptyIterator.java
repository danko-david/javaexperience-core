package eu.javaexperience.collection.iterator;

import java.util.Iterator;

public class EmptyIterator<T> implements Iterator<T>
{
	public static final EmptyIterator INSTANCE = new EmptyIterator<>();
	
	@Override
	public boolean hasNext()
	{
		return false;
	}

	@Override
	public T next()
	{
		return null;
	}

	@Override
	public void remove()
	{}
}