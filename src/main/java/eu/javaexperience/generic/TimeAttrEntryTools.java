package eu.javaexperience.generic;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.io.IOTools;
import eu.javaexperience.reflect.Mirror;

public class TimeAttrEntryTools
{
	public static TimeAttrEntry[] emptyTimeAttrEntryArray = new TimeAttrEntry[0];

	public static <R> TimeAttrEntry<R> fromFileWithExaminer(final AbstractFile f, final GetBy1<R, AbstractFile> examiner)
	{
		return new TimeAttrEntry<R>()
		{
			@Override
			public R getSubject()
			{
				return examiner.getBy(f);
			}
			
			@Override
			public long getLastModifiedTime()
			{
				return f.lastModified();
			}

			@Override
			public Object getOrigin()
			{
				return f;
			}
		};
	}

	
	public static <R> TimeAttrEntry<R> fromFileWithExaminer(final File f, final GetBy1<R, File> examiner)
	{
		return new TimeAttrEntry<R>()
		{
			@Override
			public R getSubject()
			{
				return examiner.getBy(f);
			}
			
			@Override
			public long getLastModifiedTime()
			{
				return f.lastModified();
			}

			@Override
			public Object getOrigin()
			{
				return f;
			}
		};
	}
	
	public static TimeAttrEntry<File> fromFile(final File file)
	{
		return new TimeAttrEntry<File>()
		{
			@Override
			public File getSubject()
			{
				return file;
			}
			
			@Override
			public long getLastModifiedTime()
			{
				return file.lastModified();
			}
			
			@Override
			public Object getOrigin()
			{
				return file;
			}
		};
	}

	public static TimeAttrEntry<byte[]> constant(final byte[] data, final int time)
	{
		return new TimeAttrEntry<byte[]>()
		{
			@Override
			public long getLastModifiedTime()
			{
				return time;
			}

			@Override
			public byte[] getSubject()
			{
				return data;
			}

			@Override
			public Object getOrigin()
			{
				return data;
			}
		};
	}

	public static long getLastModification(TimeAttrEntry<?>... entries)
	{
		long ret = 0;
		
		for(TimeAttrEntry<?> ent:entries)
		{
			long t = ent.getLastModifiedTime();
			if(t > ret)
			{
				ret = t;
			}
		}
		
		return ret;
	}
	
	public static long getLastModification(Collection<? extends TimeAttrEntry<?>> entries)
	{
		long ret = 0;
		
		for(TimeAttrEntry<?> ent:entries)
		{
			long t = ent.getLastModifiedTime();
			if(t > ret)
			{
				ret = t;
			}
		}
		
		return ret;
	}

	public static <P> TimeAttrEntry<byte[]> multiEntryBinaryGenerator
	(
		final File dst,
		final GetBy2<byte[], File, TimeAttrEntry<P>[]> processor,
		final TimeAttrEntry<P>... entries
	)
	{
		return new TimeAttrEntry<byte[]>()
		{
			protected long calcLastMod()
			{
				return TimeAttrEntryTools.getLastModification(entries);
			}
			
			@Override
			public byte[] getSubject()
			{
				long t = calcLastMod();
				if(dst.lastModified() < t)
				{
					try
					{
						byte[] data = processor.getBy(dst, entries);
						IOTools.putFileContent(dst.toString(), data);
						dst.setLastModified(t);
						return data;
					}
					catch(Exception e)
					{
						Mirror.propagateAnyway(e);
					}
				}
				
				try
				{
					return IOTools.loadFileContent(dst.toString());
				}
				catch (IOException e)
				{
					Mirror.propagateAnyway(e);
					return null;
				}
			}
			
			@Override
			public Object getOrigin()
			{
				return entries;
			}
			
			@Override
			public long getLastModifiedTime()
			{
				if(calcLastMod() > dst.lastModified())
				{
					getSubject();
				}
				
				return calcLastMod();
			}
		};
	}
}
