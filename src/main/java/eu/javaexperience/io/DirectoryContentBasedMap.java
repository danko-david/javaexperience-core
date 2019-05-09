package eu.javaexperience.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import eu.javaexperience.reflect.Mirror;

public abstract class DirectoryContentBasedMap<T> implements Map<String, T>, Closeable
{
	protected File dir;
	
	public DirectoryContentBasedMap(File directory) throws FileNotFoundException
	{
		if(!directory.isDirectory())
		{
			throw new RuntimeException("file: \""+directory+"\" must be directory");
		}
		
		this.dir = directory;
		
		populateKeys();
		
		File kf = getKeyFile();
		if(kf.exists())
		{
			keyFd = new FileOutputStream(getKeyFile(), true);
		}
		else
		{
			keyFd = new FileOutputStream(getKeyFile());
		}
	}
	

	protected abstract T readValue(File f, int i);
	
	protected OutputStream keyFd;
	
	protected Map<String, Integer> keyIndex = new HashMap<>();
	
	protected File getKeyFile()
	{
		return new File(dir+"/keys");
	}
	
	protected void populateKeys()
	{
		File keys = getKeyFile();
		if(keys.exists())
		{
			try
			(
				FileInputStream fis = new FileInputStream(keys);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
			)
			{
				String line = null;
				int i=0;
				while(null != (line = br.readLine()))
				{
					line = URLDecoder.decode(line, "UTF-8");
					keyStore.add(line);
					keyIndex.put(line, i++);
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	protected int assertKey(String key)
	{
		if(null == key)
		{
			return -1;
		}
		
		int index = getKeyIndex(key);
		if(index > -1)
		{
			return index;
		}
		
		try
		{
			keyFd.write((URLEncoder.encode(key, "UTF-8")+"\n").getBytes());
			keyFd.flush();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		keyStore.add(key);
		index = keyStore.size()-1;
		keyIndex.put(key, index);
		return index;
	}
	
	protected void clearKeys()
	{
		keyStore.clear();
		keyIndex.clear();
		try
		{
			keyFd.close();
			File keyFile = getKeyFile(); 
			keyFile.delete();
			keyFd = new FileOutputStream(keyFile);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	protected ArrayList<String> keyStore = new ArrayList<>();
	
	protected File getDestinationFile(int index)
	{
		return new File(dir+"/"+index);		
	}
	
	protected void dropValue(int index)
	{
		getDestinationFile(index).delete();
	}
	
	protected boolean isValueSet(int index)
	{
		return getDestinationFile(index).exists();
	}
	
	protected int getKeyIndex(Object Tey)
	{
		Integer ret = keyIndex.get(Tey);
		if(null == ret)
		{
			return -1;
		}
		return ret;
	}
	
	@Override
	public int size()
	{
		return keyIndex.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		return keyIndex.isEmpty();
	}
	
	@Override
	public boolean containsKey(Object Tey)
	{
		int index = getKeyIndex(Tey);
		if(index < 0)
		{
			return false;
		}
		return isValueSet(index);
	}
	
	@Override
	public boolean containsValue(Object value)
	{
		return false;
	}
	
	@Override
	public synchronized T get(Object Tey)
	{
		int index = getKeyIndex(Tey);
		if(index > -1)
		{
			return readValue(getDestinationFile(index), index);
		}
		return null;
	}
	
	protected abstract void saveValue(File f, int i, T value);
	
	@Override
	public synchronized T put(String Tey, T value)
	{
		int index = assertKey(Tey);
		if(index > -1)
		{
			saveValue(getDestinationFile(index), index, value);
		}
		
		return null;
	}
	
	@Override
	public synchronized T remove(Object Tey)
	{
		int index = getKeyIndex(Tey);
		if(index > -1)
		{
			dropValue(index);
		}
		return null;
	}
	
	@Override
	public synchronized void putAll(Map<? extends String, ? extends T> m)
	{
		for(java.util.Map.Entry<? extends String, ? extends T> Tv:m.entrySet())
		{
			put(Tv.getKey(), Tv.getValue());
		}
	}
	
	@Override
	public synchronized void clear()
	{
		for(int i=0;i<keyStore.size();++i)
		{
			dropValue(i);
		}
		clearKeys();
	}
	
	@Override
	public synchronized Set<String> keySet()
	{
		Set<String> ret = new HashSet<>();
		//TODO that's wrong! we return the keys that we dropped.
		ret.addAll(keyStore);
		return ret;
	}
	
	@Override
	public synchronized Collection<T> values()
	{
		ArrayList<T> ret = new ArrayList<>();
		for(int i=0;i<keyStore.size();++i)
		{
			T add = readValue(getDestinationFile(i), i);
			if(null != add)
			{
				ret.add(add);
			}
		}
		
		return ret;
	}
	
	public class LazyKV implements Entry<String, T>
	{
		int index;
		
		protected LazyKV(int index)
		{
			this.index = index;
		}
		
		@Override
		public String getKey()
		{
			return keyStore.get(index);
		}
	
		@Override
		public T getValue()
		{
			return readValue(getDestinationFile(index), index);
		}
	
		@Override
		public T setValue(T value)
		{
			saveValue(getDestinationFile(index), index, value);
			return null;
		}
	
		public int getEntryIndex()
		{
			return index;
		}
	}
	
	@Override
	public synchronized Set<java.util.Map.Entry<String, T>> entrySet()
	{
		Set<java.util.Map.Entry<String, T>> ret = new HashSet<>();
		for(int i=0;i<keyStore.size();++i)
		{
			if(isValueSet(i))
			{
				ret.add(new LazyKV(i));
			}
		}
		
		return ret;
	}
	
	public static <T extends Serializable> FileContentMapper<T> openWithCreate(String file) throws FileNotFoundException
	{
		File dir = new File(file);
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		return new FileContentMapper<T>(dir);
	}
	
	public static <T extends Serializable> FileContentMapper<T> openWithCreateRuntimeException(String file)
	{
		try
		{
			return openWithCreate(file);
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	@Override
	public synchronized void close() throws IOException
	{
		keyFd.close();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		IOTools.silentClose(this);
		super.finalize();
	}
	
	public synchronized long getValueLastModify(String key)
	{
		int index = getKeyIndex(key);
		if(index < 0)
		{
			return 0;
		}
		
		File dst = getDestinationFile(index);
		if(null == dst || !dst.exists())
		{
			return 0;
		}
		
		return dst.lastModified();
	}
}
