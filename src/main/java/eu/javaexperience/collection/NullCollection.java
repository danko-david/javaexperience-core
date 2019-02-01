package eu.javaexperience.collection;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import eu.javaexperience.collection.iterator.EmptyIterator;
import eu.javaexperience.reflect.Mirror;

public class NullCollection<T> implements ImprovedCollection<T>
{
	public static final NullCollection INSTANCE = new NullCollection<>();
	
	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public boolean isEmpty()
	{
		return true;
	}

	@Override
	public boolean contains(Object paramObject)
	{
		return false;
	}

	@Override
	public Iterator<T> iterator()
	{
		return EmptyIterator.INSTANCE;
	}

	@Override
	public Object[] toArray()
	{
		return Mirror.emptyObjectArray;
	}

	@Override
	public <T> T[] toArray(T[] paramArrayOfT)
	{
		return Arrays.copyOf(paramArrayOfT, 0);
	}

	@Override
	public boolean remove(Object paramObject)
	{
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> paramCollection)
	{
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> paramCollection)
	{
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> paramCollection)
	{
		return false;
	}

	@Override
	public void clear()
	{
		//no-op
	}

	@Override
	public boolean add(T paramE)
	{
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> paramCollection)
	{
		return false;
	}

	@Override
	public int copyAll(Collection<? super T> dst)
	{
		return 0;
	}
}