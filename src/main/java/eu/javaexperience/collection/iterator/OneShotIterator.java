package eu.javaexperience.collection.iterator;

import java.util.Iterator;

public class OneShotIterator<E> implements Iterator<E>
{
	protected final E elem;
	protected boolean inquired = false;
	
	public OneShotIterator(E element)
	{
		this.elem = element;
	}

	@Override
	public boolean hasNext()
	{
		if(inquired)
			return false;
		else
		{
			inquired = true;
			return true;
		}
	}

	@Override
	public E next()
	{
		return elem;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();		
	}
}
