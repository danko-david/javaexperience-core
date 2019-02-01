package eu.javaexperience.collection.enumerations;

import java.util.List;

public interface EnumManager<E extends EnumLike<E>> extends Iterable<E>
{
	public void registerElement(E elem);
	public E getByOrdinal(int ord);
	public E getByName(String name);
	public Object[] getValues();
	public E[] getValues(E[] arr);
	public List<E> getValueList();
}
