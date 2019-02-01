package eu.javaexperience.patterns.structure.named;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.asserts.AssertArgument;

public abstract class NameRegistry<E extends NamedItem>
{
	protected ConcurrentMap<String, E> namedElements = new ConcurrentHashMap<>();
	
	public boolean addNamedElement(E elem)
	{
		String name = elem.getName();
		AssertArgument.assertNotNull(name, "element name");
		return null == namedElements.putIfAbsent(name, elem);
	}
	
	public void assertAddNamedElement(E elem)
	{
		String name = elem.getName();
		AssertArgument.assertNotNull(name, "element name");
		if(null != namedElements.putIfAbsent(name, elem))
		{
			throw new IllegalArgumentException("Element with name: \""+name+"\" already in registry");
		}
	}
	
	public boolean isNameReserved(String name)
	{
		return namedElements.containsKey(name);
	}
	
	public E getByName(String name)
	{
		return namedElements.get(name);
	}
	
	public E assertGetByName(String name)
	{
		E e = getByName(name);
		if(null == e)
		{
			throw new RuntimeException("Entity with name \""+name+"\" doesn't exists.");
		}
		return e;
	}
	
	public Set<String> getNames()
	{
		return namedElements.keySet();
	}
	
	public Collection<E> getElements()
	{
		return namedElements.values();
	}
	
	
	
	
	//TODO mediators add/remove
	
	
	
	
	
	
}
