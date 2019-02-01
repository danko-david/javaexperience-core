package eu.javaexperience.collection.enumerations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import eu.javaexperience.asserts.AssertArgument;

public class SimpleFixedEnumManager<E extends EnumLike<E>> implements EnumManager<E>
{
	protected List<E> cnst;
	final HashMap<String, E> byName = new HashMap<>();
	
	public SimpleFixedEnumManager(List<E> from)
	{
		cnst = new ArrayList<>(from);
		int i = 0;
		for(E c:cnst)
		{
			byName.put(c.getName(), c);
			//SimpleEnumManager doest this
			//c.setEnumManager(this);
			//c.setOrdinal(i);
			AssertArgument.assertEquals(i++, c.getOrdinal(), "enum_oridnal");
		}
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return cnst.iterator();
	}

	@Override
	public void registerElement(E elem)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public E getByOrdinal(int ord)
	{
		return cnst.get(ord);
	}

	@Override
	public E getByName(String name)
	{
		return byName.get(name);
	}

	@Override
	public Object[] getValues()
	{
		return cnst.toArray();
	}

	@Override
	public E[] getValues(E[] arr)
	{
		return cnst.toArray(arr);
	}

	@Override
	public List<E> getValueList()
	{
		return cnst;
	}
	
	@Override
	public String toString()
	{
		return "SimpleFixedEnumManager";
	}
	
}
