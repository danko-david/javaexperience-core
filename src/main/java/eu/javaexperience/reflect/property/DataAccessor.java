package eu.javaexperience.reflect.property;

public interface DataAccessor
{
	public boolean canHandle(Object subject);
	public Object get(Object subject, String key);
	public String[] keys(Object subject);
}