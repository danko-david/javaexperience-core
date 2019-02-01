package eu.javaexperience.collection;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public abstract class PublisherList<T> extends PublisherCollection<T> implements List<T>
{
	@Override
	public void add(int index, T element) {}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		return false;
	}

	@Override
	public T get(int index)
	{
		return null;
	}

	@Override
	public int indexOf(Object o)
	{
		return 0;
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return 0;
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
	public T remove(int index)
	{
		return null;
	}

	@Override
	public T set(int index, T element)
	{
		return null;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return null;
	}
}
