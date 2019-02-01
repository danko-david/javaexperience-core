package eu.javaexperience.collection.iterator;


import java.util.Iterator;

public class NotNullIterable
{
	public static <T> Iterable<T> whitoutNulls(final Iterable<T> iterable)
	{
		if(iterable == null)
			return iterable;

		return new Iterable<T>()
		{
			@Override
			public Iterator<T> iterator()
			{
				return new Iterator<T>()
				{
					Iterator<T> it = iterable.iterator();
					T last = null;
					boolean getted = false;

					{
						getNext();
					}
					
					@Override
					public boolean hasNext()
					{
						return last != null;
					}

					private T getNext()
					{
						if(!it.hasNext())
						{
							last = null;
							return null;
						}
						while(it.hasNext() && (last = it.next()) == null);
						getted = true;
						return last;
					}
					
					@Override
					public T next()
					{
						getted = false;
						T old = last;
						getNext();
						return old;
					}

					@Override
					public void remove()
					{
						it.remove();
					}
				};
			}
		};
	}
	
	public static <T> Iterable<T> whitoutNulls(final T[] array)
	{
		return whitoutNulls(new Iterable<T>()
		{
			@Override
			public Iterator<T> iterator()
			{
				return new ArrayIterator(array);
			}
		});
	}
}