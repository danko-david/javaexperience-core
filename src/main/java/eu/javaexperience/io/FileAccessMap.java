package eu.javaexperience.io;

import java.io.File;
import java.io.IOException;

import eu.javaexperience.collection.map.PublisherMap;
import eu.javaexperience.interfaces.simple.WrapUnwarpTools;
import eu.javaexperience.interfaces.simple.WrapUnwrap;

public class FileAccessMap<K> extends PublisherMap<K, byte[]>
{
	protected WrapUnwrap<String, K> fileWrap;
	
	public FileAccessMap(WrapUnwrap<String, K> filePathWrap)
	{
		this.fileWrap = filePathWrap;
	}
	
	public static FileAccessMap<String> fromDirectory(String dir)
	{
		return new FileAccessMap<String>(WrapUnwarpTools.withPrefix(dir));
	}
	
	public static FileAccessMap<String> fromDirectoryModifiedBy(WrapUnwrap<String, String> wrapBefore, String dir)
	{
		return new FileAccessMap<String>
		(
			WrapUnwarpTools.chain
			(
				wrapBefore,
				WrapUnwarpTools.withPrefix(dir)
			)
		);
	}
	
	@Override
	public byte[] put(K key, byte[] value)
	{
		String path = fileWrap.wrap((K) key);
		try
		{
			IOTools.createPathBeforeFile(path);
			IOTools.putFileContent(path, value);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public byte[] get(Object key)
	{
		String path = fileWrap.wrap((K) key);
		if(new File(path).exists())
		{
			try
			{
				return IOTools.loadFileContent(path);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean containsKey(Object key)
	{
		String path = fileWrap.wrap((K) key);
		return new File(path).exists();
	}
}
