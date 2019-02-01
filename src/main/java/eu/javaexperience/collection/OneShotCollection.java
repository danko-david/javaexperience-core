package eu.javaexperience.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import eu.javaexperience.collection.iterator.OneShotIterator;

public class OneShotCollection<E> implements Collection<E>
{
	protected final E element;
	
	public OneShotCollection(E element)
	{
		this.element = element;
	}
	
	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public boolean contains(Object o)
	{
		return false;
	}

	@Override
	public Iterator<E> iterator()
	{
		return new OneShotIterator<E>(element);
	}

	@Override
	public Object[] toArray()
	{
		return new Object[]{element};
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		T[] ret = Arrays.copyOf(a, 1);
		ret[0] = (T)element;
		return ret;
	}

	@Override
	public boolean add(E e)
	{
		return false;
	}

	@Override
	public boolean remove(Object o)
	{
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		if(c.size() > 1)
			return false;
		
		for(Object o:c)
		{
			if(o == element)
				return true;
			
			if(o != null)
				return o.equals(element);
			
		}
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return false;
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}
}
