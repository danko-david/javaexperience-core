package eu.javaexperience.collection.enumerations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.collection.CollectionReadOnlyFunctions;
import eu.javaexperience.collection.ReadOnlyAndRwCollection;

public class SimpleEnumManager<T extends EnumLike<T>> implements EnumManager<T>
{
	protected ReadOnlyAndRwCollection<List<T>> values = new ReadOnlyAndRwCollection(new ArrayList<>(), CollectionReadOnlyFunctions.MAKE_LIST_READ_ONLY);
	
	public void registerElement(T elem)
	{
		AssertArgument.assertTrue(!elem.isRegistered(), "Enum element is already registered.");
		int ord = values.getWriteable().size();
		elem.setOrdinal(ord);
		elem.setEnumManager(this);
		values.getWriteable().add(ord, elem);
	}
	
	public T getByOrdinal(int ord)
	{
		return values.getWriteable().get(ord);
	}
	
	public T getByName(String name)
	{
		for(T val:values.getWriteable())
		{
			if(name.equals(val.getName()))
			{
				return val;
			}
		}
		
		return null;
	}
	
	public Object[] getValues()
	{
		return values.getWriteable().toArray();
	}
	
	public T[] getValues(T[] arr)
	{
		return values.getWriteable().toArray(arr);
	}
	
	public List<T> getValueList()
	{
		return values.getReadOnly();
	}

	@Override
	public Iterator<T> iterator()
	{
		return values.getReadOnly().iterator();
	}
}
