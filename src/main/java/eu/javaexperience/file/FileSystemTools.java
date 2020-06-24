package eu.javaexperience.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.file.fs.classloader.ClassLoaderFileSystem;
import eu.javaexperience.file.fs.os.OsFile;
import eu.javaexperience.file.fs.os.OsFileSystem;
import eu.javaexperience.file.fs.os.dir.OsDirectoryFilesystem;
import eu.javaexperience.file.fs.zip.ZipFileSystem;
import eu.javaexperience.regex.RegexTools;

public class FileSystemTools
{
	public static AbstractFileSystem DEFAULT_FILESYSTEM = new OsFileSystem();
	
	public static AbstractFileSystem SYSTEM_CLASSLOADER_FILESYSTEM = new ClassLoaderFileSystem(ClassLoader.getSystemClassLoader());

	public static AbstractFileSystem [] emptyAbstractFileSystemArray = new AbstractFileSystem[0];

	public static AbstractFile[] emptyAbstractFileArray = new AbstractFile[0];
	
	public static AbstractFile[] warpFiles(File... fs)
	{
		AbstractFile[] ret = new AbstractFile[fs.length];
		for(int i=0;i<ret.length;++i)
		{
			ret[i] = new OsFile(fs[i]);
		}
		return ret;
	}
	
	public static String[] decomposePath(String p)
	{
		String[] ret = RegexTools.SLASHES_LINUX_WINDOWS.split(p);
		
		for(int i=0;i<ret.length;++i)
		{
			if("".equals(ret[i]))
			{
				ret[i] = null;
			}
		}
		
		return ArrayTools.withoutNulls(ret);
	}
	
	public static void find(AbstractFile root, Collection<AbstractFile> res)
	{
		res.add(root);
		if(root.isDirectory())
		{
			for(AbstractFile f:root.listFiles())
			{
				find(f,res);
			}
		}
	}
	
	public static List<AbstractFileSystem> collectJavaClassPathFileSystems()
	{
		ArrayList<AbstractFileSystem> ret = new ArrayList<>();
		
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		if(cl instanceof URLClassLoader)
		{
			URL[] urls = ((URLClassLoader)cl).getURLs();
	
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
		}
		
		//ret.add(FileSystemTools.SYSTEM_CLASSLOADER_FILESYSTEM);
		//ret.add(FileSystemTools.DEFAULT_FILESYSTEM);
		return ret;
	}
}
