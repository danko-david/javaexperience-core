package eu.javaexperience.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

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
}
