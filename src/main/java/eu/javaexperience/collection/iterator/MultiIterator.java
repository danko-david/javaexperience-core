package eu.javaexperience.collection.iterator;

import java.util.ArrayList;
import java.util.Iterator;

public class MultiIterator<T> implements Iterator<T>
{
	ArrayList<Iterator<T>> its = new ArrayList<>();
	
	public MultiIterator()
	{}
	
	public void addIterator(Iterator<T> it)
	{
		its.add(it);
	}
	
	protected int crnt = -1;
	
	@Override
	public boolean hasNext()
	{
		//on the first call
		if(crnt == -1)
		{
			//if no iterator in the array
			if(its.size() == 0)
			{
				//sure there is no next value
				return false;
			}
			else
			{
				//otherwise we get the first iterator 
				crnt = 0;
			}
		}
			
		//is the current iterator has next element?
		boolean ret = its.get(crnt).hasNext();
		
		
		if(ret)
		{
			//client may get the next
			return true;
		}
		else
		{
			//if there is no more element in the current iterator
			//we change to the next iterator and with recursion
			//determine: is there next element?
			if(its.size()-1 > crnt)
			{
				++crnt;
				return hasNext();
			}
			else
			{
				//end of iterators and there is no more element
				//in this last iterator 
				return false;
			}
		}
	}

	@Override
	public T next()
	{
		//get the current iterator's next value
		//we thrust the client that use the iterator properly
		return its.get(crnt).next();
	}

	@Override
	public void remove()
	{
		its.get(crnt).remove();
	}
}