package eu.javaexperience.collection.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * It's easier to achieve as first i think. :D
 * */
public class ArrayListSeemsSet<E> extends ArrayList<E> implements Set<E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ArrayListSeemsSet() {}
	
	public ArrayListSeemsSet(E[] s)
	{
		for(E e:s)
		{
			add(e);
		}
	}
	
	public ArrayListSeemsSet(Collection<E> s)
	{
		for(E e:s)
		{
			add(e);
		}
	}
}