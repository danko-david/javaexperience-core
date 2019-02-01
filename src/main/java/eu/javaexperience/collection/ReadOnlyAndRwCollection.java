package eu.javaexperience.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class ReadOnlyAndRwCollection<C extends Collection<?>>
{
	public ReadOnlyAndRwCollection(C  coll, GetBy1<C, C> makeReadOnly)
	{
		writeable = coll;
		readOnly = makeReadOnly.getBy(writeable);
	}
	
	protected C writeable;
	protected C readOnly;
	
	public C getWriteable()
	{
		return writeable;
	}
	
	public C getReadOnly()
	{
		return readOnly;
	}
	
	public static <T> ReadOnlyAndRwCollection<List<T>> createList()
	{
		return new ReadOnlyAndRwCollection(new ArrayList<T>(), CollectionReadOnlyFunctions.MAKE_LIST_READ_ONLY);
	
	}

	public static <T> ReadOnlyAndRwCollection<Set<T>> createSet()
	{
		return new ReadOnlyAndRwCollection(new HashSet<T>(), CollectionReadOnlyFunctions.MAKE_SET_READ_ONLY);
	}
}
