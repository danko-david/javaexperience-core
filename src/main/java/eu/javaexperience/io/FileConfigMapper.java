package eu.javaexperience.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.reflect.Mirror;

public class FileConfigMapper implements Map<String, String>
{
	protected final File dir;
	protected final String path;
	public FileConfigMapper(File dir)
	{
		AssertArgument.assertNotNull(dir, "dir");
		if(!dir.exists() || !dir.isDirectory())
		{
			throw new RuntimeException("Given file is not directory");
		}
		this.dir = dir;
		path = dir.toString()+"/";
	}
	
	@Override
	public int size()
	{
		return dir.list().length;
	}

	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key)
	{
		if(null == key)
		{
			return false;
		}
		return new File(path+key).exists();
	}

	@Override
	public boolean containsValue(Object value)
	{
		for(String k:dir.list())
		{
			if(value.equals(get(k)))
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String get(Object key)
	{
		if(null == key || !containsKey(key))
		{
			return null;
		}
		
		try
		{
			return IOTools.getFileContents(path+key);
		}
		catch (Exception e)
		{
			Mirror.throwSoftOrHardButAnyway(e);
			return null;//have a nice day
		}
	}

	@Override
	public String put(String key, String value)
	{
		try
		{
			IOTools.putFileContent(path+key, false, value.getBytes());
		}
		catch (IOException e)
		{
			Mirror.throwSoftOrHardButAnyway(e);
		}
		return null;
	}

	@Override
	public String remove(Object key)
	{
		if(null == key)
		{
			return null;
		}
		
		new File(path+key).delete();
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m)
	{
		MapTools.putAll(m, this);
	}

	@Override
	public void clear()
	{
		for(File f:dir.listFiles())
		{
			f.delete();
		}
	}

	@Override
	public Set<String> keySet()
	{
		return CollectionTools.inlineAdd(new HashSet<String>(), dir.list());
	}

	@Override
	public Collection<String> values()
	{
		ArrayList<String> ret = new ArrayList<>();
		for(String k:dir.list())
		{
			String re = get(k);
			if(null != re)
			{
				ret.add(re);
			}
		}
		return ret;
	}

	protected class LazyEntry implements Entry<String, String>
	{
		protected String key;
		
		protected LazyEntry(String key)
		{
			this.key = key;
		}
		
		@Override
		public String getKey()
		{
			return key;
		}

		@Override
		public String getValue()
		{
			return get(key);
		}

		@Override
		public String setValue(String value)
		{
			return put(key, value);
		}
		
	}
	
	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet()
	{
		HashSet<Entry<String, String>> ret = new HashSet<>();
		
		for(String s:dir.list())
		{
			ret.add(new LazyEntry(s));
		}
		
		return ret;
	}

}
