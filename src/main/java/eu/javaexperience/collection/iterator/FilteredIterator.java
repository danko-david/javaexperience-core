package eu.javaexperience.collection.iterator;

import java.util.ArrayList;
import java.util.Iterator;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.text.StringFunctions;

public class FilteredIterator<T> implements Iterator<T>
{
	protected final Iterator<T> origin;
	protected final GetBy1<Boolean, T> filter;
	
	public FilteredIterator(Iterator<T> origin, GetBy1<Boolean, T> filter)
	{
		this.origin = origin;
		this.filter = filter;
	}
	
	protected boolean getted = true;
	protected boolean hasNext = true;
	protected T next;

	
	protected void updateNext()
	{
		if(getted && hasNext)
		{
			//go next
			while(origin.hasNext())
			{
				T n = origin.next();
				if(Boolean.TRUE == filter.getBy(n))
				{
					next = n;
					getted = false;
					//matching element found, publishing
					return;
				}
			}
			
			//gone trought all element without finding a matching one.
			
			//end of iteration
			this.hasNext = false;
			this.next = null;
		}
	}
	
	@Override
	public boolean hasNext()
	{
		updateNext();
		return hasNext;
	}

	@Override
	public T next()
	{
		updateNext();
		getted = true;
		return next;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("FilteredIterator.remove()");
	}
	
	/*public static void main(String[] args)
	{
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("alma");
		arr.add("Bela");
		arr.add("alma");
		arr.add("kek");
		
		for(String s:IteratorTools.wrapIterator(new FilteredIterator<String>(arr.iterator(), StringFunctions.isStartsWith("a"))))
		{
			System.out.println(s);
		}
	}*/
}
