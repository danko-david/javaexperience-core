package eu.javaexperience.reflect;

public class ShallowHashEquals
{
	@Override
	public boolean equals(Object obj)
	{
		return Mirror.shallowEquals(this, obj);
	}
	
	@Override
	public int hashCode()
	{
		return Mirror.shallowHashCode(this);
	}
}
