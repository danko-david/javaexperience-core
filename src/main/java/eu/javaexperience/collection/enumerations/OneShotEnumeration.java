package eu.javaexperience.collection.enumerations;

import java.util.Enumeration;

public class OneShotEnumeration<E> implements Enumeration<E>
{
	protected E element;
	public OneShotEnumeration(E elem)
	{
		this.element = elem;
	}
	
	protected boolean next = true;
	@Override
	public boolean hasMoreElements()
	{
		return next;
	}

	@Override
	public E nextElement()
	{
		next = false;
		return element;
	}
}
