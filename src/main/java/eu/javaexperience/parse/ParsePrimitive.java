package eu.javaexperience.parse;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.log.LogLevel;

public class ParsePrimitive
{
	public static int tryParseInt(String val,int def)
	{
		try
		{
			return Integer.parseInt(val);
		}
		catch(Exception e)
		{
			return def;
		}
	}
	
	public static Integer tryParseInt(String val)
	{
		try
		{
			return Integer.parseInt(val);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static Long tryParseLong(String val)
	{
		try
		{
			return Long.parseLong(val);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static long tryParseLong(String val,long def)
	{
		try
		{
			return Long.parseLong(val);
		}
		catch(Exception e)
		{
			return def;
		}
	}
	
	public static double tryParseDouble(String val,double def)
	{
		try
		{
			return Double.parseDouble(val);
		}
		catch(Exception e)
		{
			return def;
		}
	}
	
	public static Double tryParseDouble(String val)
	{
		try
		{
			return Double.parseDouble(val);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static Boolean tryParseBoolean(String s)
	{
		if("true".equals(s))
		{
			return Boolean.TRUE;
		}
		else if("false".equals(s))
		{
			return Boolean.FALSE;
		}
		else
		{
			return null;
		}
	}

	public static <E extends Enum<E>> E tryParseEnum(Class<? extends E> Enum, String value)
	{
		for(E e:Enum.getEnumConstants())
		{
			if(e.name().equals(value))
			{
				return e;
			}
		}
		
		return null;
	}
	
}