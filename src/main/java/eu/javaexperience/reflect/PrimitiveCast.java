package eu.javaexperience.reflect;

public class PrimitiveCast
{
	public static int toInt(Integer val, int _default)
	{
		if(null == val)
		{
			return _default;
		}
		
		return val;
	}
}
