package eu.javaexperience.cache;

import java.io.File;
import java.io.Serializable;

import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.io.SerializationTools;

public class FileCacheTools
{
	public static <T extends Serializable> T getOrCalculate(File f, SimpleGet<T> calculate)
	{
		T ret = null;
		try
		{
			ret = (T) SerializationTools.deserializeFromFile(f);
			return ret;
		}
		catch(Exception e){}
		
		ret = calculate.get();
		SerializationTools.serializeIntoFile(f, ret);
		return ret;
	}
}
