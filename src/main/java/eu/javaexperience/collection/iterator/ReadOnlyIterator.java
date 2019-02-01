package eu.javaexperience.collection.iterator;

import java.util.Iterator;

import eu.javaexperience.asserts.AssertArgument;

public class ReadOnlyIterator<T> implements Iterator<T>, Iterable<T>
{
	protected final Iterator<T> it;
	
	public ReadOnlyIterator(Iterator<T> it)
	{
		AssertArgument.assertNotNull(this.it = it, "iterator");
	}
	
	
	@Override
	public boolean hasNext()
	{
		return it.hasNext();
	}

	@Override
	public T next()
	{
		return it.next();
	}

	@Override
	public void remove()
	{
		throw new RuntimeException("Iterator is read only") ;
	}

	@Override
	public Iterator<T> iterator()
	{
		return this;
	}
}
