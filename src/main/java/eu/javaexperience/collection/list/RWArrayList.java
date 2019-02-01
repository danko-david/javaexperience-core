package eu.javaexperience.collection.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import eu.javaexperience.collection.ImprovedCollection;
import eu.javaexperience.collection.iterator.IteratorTools;

public class RWArrayList<E> implements List<E>, ImprovedCollection<E>
{
	protected ArrayList<E> list = new ArrayList<>();
	
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	protected ReadLock rl = lock.readLock();
	protected WriteLock wl = lock.writeLock();
	
	@Override
	public int size()
	{
		rl.lock();
		try
		{
			return list.size();
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public boolean isEmpty()
	{
		rl.lock();
		try
		{
			return list.isEmpty();
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public boolean contains(Object o)
	{
		rl.lock();
		try
		{
			return list.contains(o);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public Iterator<E> iterator()
	{
		rl.lock();
		try
		{
			return (Iterator<E>) IteratorTools.wrapArray(list.toArray());
		}
		finally
		{
			rl.unlock();
		}
	
	}

	@Override
	public Object[] toArray()
	{
		rl.lock();
		try
		{
			return list.toArray();
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		rl.lock();
		try
		{
			return list.toArray(a);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public boolean add(E e)
	{
		wl.lock();
		try
		{
			return list.add(e);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public boolean remove(Object o)
	{
		wl.lock();
		try
		{
			return list.remove(o);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		rl.lock();
		try
		{
			return list.containsAll(c);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		wl.lock();
		try
		{
			return list.addAll(c);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		wl.lock();
		try
		{
			return list.addAll(index, c);
		}
		finally
		{
			wl.unlock();
		}
		
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		wl.lock();
		try
		{
			return list.removeAll(c);
		}
		finally
		{
			wl.unlock();
		}
		
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		wl.lock();
		try
		{
			return list.retainAll(c);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public void clear()
	{
		wl.lock();
		try
		{
			list.clear();
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public E get(int index)
	{
		rl.lock();
		try
		{
			return list.get(index);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public E set(int index, E element)
	{
		wl.lock();
		try
		{
			return list.set(index, element);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public void add(int index, E element)
	{
		wl.lock();
		try
		{
			list.add(index, element);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public E remove(int index)
	{
		wl.lock();
		try
		{
			return list.remove(index);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public int indexOf(Object o)
	{
		rl.lock();
		try
		{
			return list.indexOf(o);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public int lastIndexOf(Object o)
	{
		rl.lock();
		try
		{
			return list.lastIndexOf(o);
		}
		finally
		{
			rl.unlock();
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public ListIterator<E> listIterator()
	{
		wl.lock();
		try
		{
			return ((ArrayList<E>)list.clone()).listIterator();
		}
		finally
		{
			wl.unlock();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListIterator<E> listIterator(int index)
	{
		wl.lock();
		try
		{
			return ((ArrayList<E>)list.clone()).listIterator(index);
		}
		finally
		{
			wl.unlock();
		}
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		rl.lock();
		try
		{
			return list.subList(fromIndex, toIndex);
		}
		finally
		{
			rl.unlock();
		}
	}

	@Override
	public int copyAll(Collection<? super E> dst)
	{
		rl.lock();
		try
		{
			int ret = 0;
			for(E e:list)
			{
				if(dst.add(e))
				{
					++ret;
				}
			}
			return ret;
		}
		finally
		{
			rl.unlock();
		}
	}
	
	/**
	 * Only for advanced usage
	 * */
	@Deprecated
	public ReadLock getReadLock()
	{
		return rl;
	}

	/**
	 * Only for advanced usage
	 * */
	@Deprecated
	public WriteLock getWriteLock()
	{
		return wl;
	}

	/**
	 * Only for advanced usage
	 * */
	@Deprecated
	public List<E> getBackendList()
	{
		return list;
	}
}
