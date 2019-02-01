package eu.javaexperience.collection.set;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import eu.javaexperience.collection.iterator.ArrayIterator;

public class ArrayAsSetRO<T> implements Set<T>
{
	final T[] arr;
	
	public ArrayAsSetRO(T[] arr)
	{
		this.arr = arr;
	}

	@Override
	public int size()
	{
		return arr.length;
	}

	@Override
	public boolean isEmpty()
	{
		return arr.length == 0;
	}

	@Override
	public boolean contains(Object paramObject)
	{
		for(int i=0;i<arr.length;i++)
			if(paramObject.equals(arr[i]))
				return true;
		
		return false;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new ArrayIterator<>(arr);
	}

	@Override
	public Object[] toArray()
	{
		return Arrays.copyOf(arr, arr.length);
	}

	@Override
	public <B> B[] toArray(B[] paramArrayOfT)
	{
		B[] ret = Arrays.copyOf(paramArrayOfT, arr.length);
		int ep = 0;
		for(int i=0;i<arr.length;i++)
			if(arr[i] != null)
				ret[ep++] = (B) arr[i];
		
		return ret;
	}
	
	@Override
	public boolean add(T paramE)
	{
		return false;
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
	public boolean addAll(Collection<? extends T> paramCollection)
	{
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> paramCollection)
	{
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> paramCollection)
	{
		return false;
	}

	@Override
	public void clear()
	{
	}
}
