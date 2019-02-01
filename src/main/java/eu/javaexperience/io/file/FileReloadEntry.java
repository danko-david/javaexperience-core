package eu.javaexperience.io.file;

import java.io.File;
import java.io.IOException;

import eu.javaexperience.asserts.AssertArgument;

/**
 * Simple but useful
 * */
public abstract class FileReloadEntry<T>
{
	//protected static final RWLockMap<File, Long> LAST_LOAD_TIME = new RWLockMap<>(new HashMap<File, V>());
	
	/**
	 * I've faced an interesting bug in the previous version:
	 * When i cached the passwd's file content, sometimes i get the older
	 * version of the from the cache.
	 * The reason is that the java returns the file last modification time in
	 * secounds resolution, so if the passwd file modified right since some
	 * milisecs the cache determines (badly) that the file not modified, so this
	 * case is important if we apply file cache based on last modification.
	 * */
	public static boolean needReload(long entryTime, long fileTime)
	{
		entryTime /= 1000;
		fileTime /= 1000;
		long currentTime = System.currentTimeMillis()/1000;
		return fileTime == currentTime || fileTime > entryTime;
	}
	
	protected final File file;
	protected long lastAccess = -1;
	
	public FileReloadEntry(File f)
	{
		AssertArgument.assertNotNull(this.file = f, "file");
	}
	
	protected abstract T processFile(File f) throws IOException;
	
	protected T thing;
	
	public synchronized T get() throws IOException
	{
		long lastmod = file.lastModified();
		if(needReload(lastAccess, lastmod))
		{
			this.thing = processFile(file);
			this.lastAccess = lastmod;
		}
		
		return thing;
	}
}