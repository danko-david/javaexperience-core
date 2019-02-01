package eu.javaexperience.collection.list;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import eu.javaexperience.collection.iterator.EmptyIterator;
import eu.javaexperience.reflect.Mirror;

public class NullList<T> implements List<T>
{
	public static final NullList instance = new NullList();
	
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
	public boolean contains(Object o)
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
	public <T> T[] toArray(T[] a)
	{
		return (T[]) Array.newInstance(a.getClass().getComponentType(), 0);
	}

	@Override
	public boolean add(T e)
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
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
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
	}

	@Override
	public T get(int index)
	{
		return null;
	}

	@Override
	public T set(int index, T element)
	{
		return null;
	}

	@Override
	public void add(int index, T element)
	{
	}

	@Override
	public T remove(int index)
	{
		return null;
	}

	@Override
	public int indexOf(Object o)
	{
		return -1;
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return -1;
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return null;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return null;
	}
}
