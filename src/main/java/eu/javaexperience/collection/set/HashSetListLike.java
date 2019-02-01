package eu.javaexperience.collection.set;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import eu.javaexperience.exceptions.UnimplementedMethodException;

public class HashSetListLike<E> extends HashSet<E> implements List<E>
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public E get(int index)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public E set(int index, E element)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public void add(int index, E element)
	{
		throw new UnimplementedMethodException();		
	}

	@Override
	public E remove(int index)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public int indexOf(Object o)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public ListIterator<E> listIterator()
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public ListIterator<E> listIterator(int index)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		throw new UnimplementedMethodException();
	}
}
