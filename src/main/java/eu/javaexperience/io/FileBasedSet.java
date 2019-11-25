package eu.javaexperience.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.reflect.Mirror;

/**
 * File that stores strings in a backend file.
 * TODO that's a "fast to write" implementation that created to resolv an
 * 	unexcepted situation and need to be optimised lated
 * */
public class FileBasedSet implements Set<String>, Closeable
{
	protected final File file;
	protected final RandomAccessFile rw;
	protected final Set<String> content = new HashSet();
	
	protected final TreeMap<Integer, List<FileRegion>> values = new TreeMap<>();
	protected final TreeMap<Integer, List<FileRegion>> free = new TreeMap<>();
	
	public FileBasedSet(File f) throws IOException
	{
		this.file = f;
		if(f.isDirectory())
		{
			throw new RuntimeException("Given file is a directory: "+f);
		}
		
		rw = new RandomAccessFile(file, "rw");
		readFile();
		for(List<FileRegion> vs:values.values())
		{
			for(FileRegion v:vs)
			{
				content.add(URLDecoder.decode(v.value));
			}
		}
	}
	
	public static List<FileRegion> getOfSize(Map<Integer, List<FileRegion>> map, int size)
	{
		return MapTools.getOrCreate(map, size, (e)-> new ArrayList<>());
	}
	
	protected void readFile() throws IOException
	{
		StringBuilder sb = new StringBuilder();
		final long length = rw.length();
		long start = 0;
		Boolean content = null;
		for(long i=0;i<length;++i)
		{
			int val = rw.read();
			
			if(10 == val || val < 0)
			{
				if(null == content)
				{
					//eof
					if(val < 0)
					{
						return;
					}
					//else skip
				}
				//free space 
				else if(Boolean.FALSE == content)
				{
					getOfSize(free, (int) (i-start)).add(new FileRegion(start, i-start, null));
				}
				//content
				else
				{
					getOfSize(values, (int) (i-start)).add(new FileRegion(start, i-start, sb.toString()));
					sb.delete(0, sb.length());
				}
				
				content = null;
			}
			else
			{
				if(null == content)
				{
					//empty block
					if(13 == val)
					{
						start = i;
						content = false;
					}
					else
					{
						//start of new String
						start = i;
						content = true;
						sb.append((char) val);
					}
				}
				else if(Boolean.TRUE == content)
				{
					sb.append((char) val);
				}
				else
				{
					//must be /r
					if(13 != val)
					{
						throw new RuntimeException("invalid empty block at "+i);
					}
				}
			}
		}
	}
	
	protected static class FileRegion
	{
		long off;
		long size;
		
		//null if region is free, no null when the region contains String
		String value;
		
		public FileRegion(long off, long size, String value)
		{
			this.off = off;
			this.size = size;
			this.value = value;
		}
		
		public void erase(RandomAccessFile rw) throws IOException
		{
			byte[] data = new byte[(int) size];
			for(int i=0;i<size;++i)
			{
				data[i] = '\r';
			}
			override(rw, data);
		}
		
		public void override(RandomAccessFile rw, byte[] data) throws IOException
		{
			if(data.length != size)
			{
				throw new RuntimeException("Content size mismatch: ("+data.length+") space for content: "+size);
			}
			
			rw.seek(off);
			rw.write(data);
		}
	}
	
	@Override
	public synchronized int size()
	{
		return content.size();
	}

	@Override
	public synchronized boolean isEmpty()
	{
		return 0 == size();
	}

	@Override
	public synchronized boolean contains(Object o)
	{
		return content.contains(o);
	}

	@Override
	public synchronized Iterator<String> iterator()
	{
		return content.iterator();
	}

	@Override
	public synchronized Object[] toArray()
	{
		return content.toArray();
	}

	@Override
	public synchronized <T> T[] toArray(T[] a)
	{
		return content.toArray(a);
	}

	@Override
	public synchronized boolean add(String e)
	{
		AssertArgument.assertNotNull(e, "value");
		if(content.add(e))
		{
			String fc = URLEncoder.encode(e);
			int len = fc.length();
/*			Entry<Integer, List<FileRegion>> ents = free.ceilingEntry(len);
			if(null != ents)
			{
				//TODO exat size match ot split regions
			}
			else*/
			{
				//add to the end of the file
				try
				{
					FileRegion reg = new FileRegion(Math.max(0, rw.length()), len, fc);
					getOfSize(values, len).add(reg);
					reg.override(rw, fc.getBytes());
					rw.write(10);
				}
				catch (IOException e1)
				{
					Mirror.propagateAnyway(e1);
				}
			}
			
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean remove(Object o)
	{
		AssertArgument.assertNotNull(o, "value");
		if(content.remove(o))
		{
			String fc = URLEncoder.encode(o.toString());
			int size = fc.getBytes().length;
			List<FileRegion> frs = values.get(size);
			
			//TODO join regions
			for(FileRegion fr:frs)
			{
				if(fc.equals(fr.value))
				{
					try
					{
						fr.erase(rw);
						frs.remove(fr);
						fr.value = null;
						getOfSize(free, size).add(fr);
					}
					catch(Exception e)
					{
						Mirror.propagateAnyway(e);
					}
					
					return true;
				}
			}
			
			throw new RuntimeException("Inconsistent removal: removed from the cache but not in the file: "+o);
		}
		return false;
	}

	@Override
	public synchronized boolean containsAll(Collection<?> c)
	{
		return content.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends String> c)
	{
		boolean all = true;
		for(String v:c)
		{
			all &= add(v);
		}
		
		return all;
	}

	@Override
	public synchronized boolean retainAll(Collection<?> c)
	{
		boolean all = true;
		for(String v:content)
		{
			if(!c.contains(v))
			{
				all &= remove(v);
			}
		}
		return all;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean all = true;
		for(Object v:c)
		{
			all &= remove(v);
		}
		
		return all;
	}

	@Override
	public synchronized void clear()
	{
		try
		{
			content.clear();
			rw.setLength(0);
			values.clear();
			free.clear();
		}
		catch(IOException e)
		{
			Mirror.propagateAnyway(e);
		}
	}
	
	@Override
	public synchronized String toString()
	{
		return CollectionTools.toStringMultiline(content);
	}

	@Override
	public void close() throws IOException
	{
		rw.close();
	}
}
