package eu.javaexperience.collection.set;


import java.util.Set;

import eu.javaexperience.collection.OneShotCollection;

public class OneShotSet<E> extends OneShotCollection<E> implements Set<E>
{
	public OneShotSet(E element)
	{
		super(element);
	}
}