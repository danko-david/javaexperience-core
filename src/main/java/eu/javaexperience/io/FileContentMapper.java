package eu.javaexperience.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import eu.javaexperience.io.DirectoryContentBasedMap.LazyKV;
import eu.javaexperience.parse.ParsePrimitive;

public class FileContentMapper<T extends Serializable> extends DirectoryContentBasedMap<T>
{
	public FileContentMapper(File directory) throws FileNotFoundException
	{
		super(directory);
	}

	protected T readValue(File dst, int i)
	{
		if(null == dst || !dst.exists())
		{
			return null;
		}
		
		return (T) SerializationTools.deserializeFromFile(dst);
	}
	
	
	protected void saveValue(File f, int i, T value)
	{
		SerializationTools.serializeIntoFile(f, value);
	}
	
	public static FileContentMapper<?> silentOpenOrRuntimeException(File file)
	{
		try
		{
			return new FileContentMapper<>(file);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public synchronized Set<Entry<String, T>> entrySet()
	{
		Set<java.util.Map.Entry<String, T>> ret = new HashSet<>();
		for(String f: dir.list())
		{
			Integer val = ParsePrimitive.tryParseInt(f);
			if(null != val)
			{
				ret.add(new LazyKV(val));
			}
		}
		
		return ret;
	}
}
