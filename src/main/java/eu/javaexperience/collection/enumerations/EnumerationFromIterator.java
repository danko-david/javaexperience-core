package eu.javaexperience.collection.enumerations;

import java.util.Enumeration;
import java.util.Iterator;


public class EnumerationFromIterator<E> implements Enumeration<E>
{
	private final Iterator<E> iterator;

	public EnumerationFromIterator(Iterator<E> iterator)
	{
		this.iterator = iterator;
	}

	public EnumerationFromIterator(Iterable<E> iterable)
	{
		this.iterator = iterable.iterator();
	}

	public E nextElement()
	{
		return iterator.next();
	}

	public boolean hasMoreElements()
	{
		return iterator.hasNext();
	}
}