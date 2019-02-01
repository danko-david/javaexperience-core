package eu.javaexperience.collection.enumerations;

public interface IntFlag
{
	public int mask();
	
	public boolean isSet(int value);
	
	public int set(int value);
	
	public int unset(int value);
}
