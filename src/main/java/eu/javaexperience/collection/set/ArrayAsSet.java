package eu.javaexperience.collection.set;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ArrayAsSet<T> implements Set<T>
{
	Object[] arr;
	int ep = 0;
	int vals = 0;
	
	
	public ArrayAsSet()
	{
		arr = new Object[8];
	}
	
	ArrayAsSet(T[] arr,int ep,int vals)
	{
		this.arr = arr;
		this.ep = ep;
		this.vals = vals;
	}
	
/*	
	public ArraySet(T[] arr)
	{
		this(Arrays.copyOf(arr, arr.length),arr.length);
	}
	
	private ArraySet(T[] arr,int ep)
	{
		this.arr = arr;
		this.ep = ep;
		for(int i=0;i<ep;i++)
			if(arr[i] != null)
				vals++;
	}
*/	
	@Override
	public int size()
	{
		return vals;
	}

	@Override
	public boolean isEmpty()
	{
		return vals == 0;
	}

	@Override
	public boolean contains(Object paramObject)
	{
		for(int i=0;i<ep;i++)
			if(paramObject.equals(arr[i]))
				return true;
		
		return false;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			int rets = 0;
			int cep = 0;
			@Override
			public boolean hasNext()
			{
				return rets < vals;
			}

			@Override
			public T next()
			{
				for(int i=cep;i<ep;i++)
					if(arr[i] != null)
					{
						cep = i+1;
						rets++;
						return (T) arr[i];
					}
				return null;
			}

			@Override
			public void remove()
			{
				ArrayAsSet.this.remove(arr[cep-1]);
			}
		};
	}

	@Override
	public Object[] toArray()
	{
		Object[] ret = new Object[vals];
		int ep = 0;
		for(int i=0;i<ep;i++)
			if(arr[i] != null)
				ret[ep++] = arr[i];
		
		return ret;
	}

	@Override
	public <T> T[] toArray(T[] paramArrayOfT)
	{
		T[] ret = Arrays.copyOf(paramArrayOfT, vals);
		int ep = 0;
		for(int i=0;i<ep;i++)
			if(arr[i] != null)
				ret[ep++] = (T) arr[i];
		
		return ret;
	}
	
	protected void inc()
	{
		if(ep == arr.length)
			arr = Arrays.copyOf(arr, arr.length*2);
	}

	@Override
	public boolean add(T paramE)
	{
		for(int i=0;i<ep;i++)
			if(paramE.equals(arr[i]))
				return false;
		
		if(ep == vals)
		{
			arr[ep++] = paramE;
			vals++;
			inc();
		}
		else
			for(int i=0;i<ep;i++)
				if(arr[i] == null)
				{
					arr[i] = paramE;
					vals++;
					break;
				}
		
		return true;
	}

	@Override
	public boolean remove(Object paramObject)
	{
		for(int i=0;i<ep;i++)
			if(paramObject.equals(arr[i]))
			{
				arr[i] = null;
				vals--;
				if(i == ep -1)
					ep--;

				return true;
			}
	
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> paramCollection)
	{
		kint:for(Object o:paramCollection)
		{
			for(int i=0;i<ep;i++)
				if(o.equals(arr[i]))
					continue kint;
			
			return false;
		}
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> paramCollection)
	{
		boolean ret = true;
		for(T o:paramCollection)
			ret &= add(o);

		return ret;
	}

	@Override
	public boolean retainAll(Collection<?> paramCollection)
	{
		boolean ret = true;
		kint:for(int i=0;i<ep;i++)
			{
			if(arr[i] != null)
				for(Object o:paramCollection)
					if(arr[i].equals(o))
						continue kint;
		
				remove(arr[i]);
				ret = false;
			}						
						
		return ret;
	}

	@Override
	public boolean removeAll(Collection<?> paramCollection)
	{
		boolean ret = true;
		for(Object o:paramCollection)
			ret &= remove(o);
		
		return ret;
	}

	@Override
	public void clear()
	{
		for(int i=0;i<ep;i++)
			arr[i] = null;
		
		ep = 0;
		vals = 0;
	}

}
