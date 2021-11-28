package eu.javaexperience.file;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.file.AbstractFileSystem;
import eu.javaexperience.file.FileSystemTools;
import eu.javaexperience.file.fs.combined.CombinedFileSystem;
import eu.javaexperience.file.fs.os.dir.OsDirectoryFilesystem;
import eu.javaexperience.file.fs.zip.ZipFileSystem;

public class RuntimeFilesystem
{
	protected static List<URL> getClassDirLists()
	{
		List<URL> ret = new ArrayList<>();
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		
		if(cl instanceof URLClassLoader)
		{
			CollectionTools.inlineAdd(ret, ((URLClassLoader)cl).getURLs());
		}
		else
		{
			for(String path: System.getProperty("java.class.path").split(":"))
			{
				try
				{
					if(!path.startsWith("/"))
					{
						path = new File(path).getAbsolutePath();
					}
					ret.add(new URL("file://"+path));
				}
				catch (MalformedURLException e)
				{
				}
			}
		}

		
		return ret;
	}
	
	protected static List<AbstractFileSystem> collectFs()
	{
		ArrayList<AbstractFileSystem> ret = new ArrayList<>();
		
		List<URL> urls = getClassDirLists();

		for(URL url: urls)
		{
			File f = new File(url.getFile());
			if(f.exists())
			{
				try
				{
					if(f.toString().endsWith(".jar"))
					{
						ret.add(new ZipFileSystem(f.toString()));
					}
					else
					{
						ret.add(new OsDirectoryFilesystem(f));
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		ret.add(FileSystemTools.SYSTEM_CLASSLOADER_FILESYSTEM);
		ret.add(FileSystemTools.DEFAULT_FILESYSTEM);
		return ret;
	}
	
	public static final AbstractFileSystem FILESYSTEM = new CombinedFileSystem(collectFs().toArray(FileSystemTools.emptyAbstractFileSystemArray));
}
