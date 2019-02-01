package eu.javaexperience.collection.iterator;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T>
{
	protected final T[] arr;
	protected int i = 0;
	
	public ArrayIterator(T[] arr)
	{
		if(arr == null)
			throw new NullPointerException("Array is null!");
		this.arr = arr;
	}
	
	@Override
	public boolean hasNext()
	{
		return i < arr.length;
	}

	@Override
	public T next()
	{
		return arr[i++];
	}

	@Override
	public void remove()
	{
		throw new RuntimeException("Removing element is not supported.");
	}
}