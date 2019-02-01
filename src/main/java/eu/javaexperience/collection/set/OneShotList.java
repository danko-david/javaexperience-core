package eu.javaexperience.collection.set;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import eu.javaexperience.collection.OneShotCollection;
import eu.javaexperience.reflect.Mirror;

public class OneShotList<T> extends OneShotCollection<T> implements List<T>
{
	public OneShotList(T element)
	{
		super(element);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		return false;
	}

	@Override
	public T get(int index)
	{
		if(0 == index)
		{
			return element;
		}
		return null;
	}

	@Override
	public T set(int index, T element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(int index, T element){}

	@Override
	public T remove(int index)
	{
		return null;
	}

	@Override
	public int indexOf(Object o)
	{
		if(Mirror.equals(element, o))
		{
			return 0;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return indexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
