package eu.javaexperience.generic;

public interface TimeAttrEntry<T>
{
	/**
	 * CTMS 
	 */
	public long getLastModifiedTime();
	public T getSubject();
	public Object getOrigin();
}
