package eu.javaexperience.io.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.regex.RegexTools;
import eu.javaexperience.text.Format;
import eu.javaexperience.text.StringTools;

public class FileTools
{

	public static long _1Kb = 1024;
	public static long _1Mb = _1Kb*1024;
	public static long _1Gb = _1Mb*1024;
	public static long _1Tb = _1Gb*1024;
	
	public static final File[] emptyFileArray = new File[0];
	
	public static void find(File root, Collection<File> res)
	{
		find(root, (e)->res.add(e));
	}
	
	public static void find(File root, SimplePublish1<File> res)
	{
		res.publish(root);
		if(root.isDirectory())
		{
			for(File f:root.listFiles())
			{
				find(f,res);
			}
		}
	}
	
	public static void conditionalFindDirs(File root, GetBy1<Boolean, File> cond)
	{
		if(root.isDirectory())
		{
			if(Boolean.TRUE == cond.getBy(root))
			{
				for(File f:root.listFiles())
				{
					conditionalFindDirs(f, cond);
				}
			}
		}
	}
	
	public static void deleteDirectory(File file, boolean follow_symlinks)
	{
		if(!file.exists())
		{
			return;
		}
		
		if(!follow_symlinks && Files.isSymbolicLink(file.toPath()))
		{
			return;
		}
		
		if(file.isDirectory())
		{
			for(File f:file.listFiles())
			{
				deleteDirectory(f, follow_symlinks);
			}
		}
		
		file.delete();
	}
	
	public static String toBytesKbMbGbOrTb(long size)
	{
		if(size >= _1Tb)
			return Format.cutFloatDecimals(String.valueOf(((double)size)/_1Tb),3)+" Tb";
		else if(size >= _1Gb)
			return Format.cutFloatDecimals(String.valueOf(((double)size)/_1Gb),3)+" Gb";
		else if(size >= _1Mb)
			return Format.cutFloatDecimals(String.valueOf(((double)size)/_1Mb),3)+" Mb";
		else if(size >= _1Kb)
			return Format.cutFloatDecimals(String.valueOf(((double)size)/_1Kb),3)+" Kb";
		else
			return size+" b";
	}

	public static File generateTempFilename(String prefix, int i, String postfix) throws IOException
	{
		File f;
		do
		{
			f = new File(prefix+StringTools.randomString(i)+postfix);
		}
		while(f.exists());
		
		return f;
	}

	public static File getExisting(File... files)
	{
		for(File f: files)
		{
			if(f.exists())
			{
				return f;
			}
		}
		
		return null;
	}
	
	/**
	 * standard move:
	 * 	moves the file if:
	 * 	- the source exists and accessible
	 * 	- the designation doesn't exists but the path does.
	 * 
	 * otherwise throws exception
	 * */
	public static void move(File from, File to)
	{
		try
		{
			Files.move(from.toPath(), to.toPath());
		}
		catch (IOException e)
		{
			Mirror.throwSoftOrHardButAnyway(e);
		}
	}

	public static void copy(File from, File to)
	{
		try
		{
			Files.copy(from.toPath(), to.toPath());
		}
		catch (IOException e)
		{
			Mirror.throwSoftOrHardButAnyway(e);
		}
	}
	
	public static void createDirectoryIfNotExtists(String dir)
	{
		File f = new File(dir);
		if(!f.exists())
		{
			f.mkdirs();
		}
	}

	public static String canonicalFilename(String localWD)
	{
		try
		{
			return new File(localWD).getCanonicalPath();
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	public static File tryGetCanonicalFile(String f)
	{
		return tryGetCanonicalFile(new File(f));
	}
	
	public static File tryGetCanonicalFile(File f)
	{
		try
		{
			return f.getCanonicalFile();
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Modified version of: https://docs.oracle.com/javase/tutorial/essential/io/find.html
	 * */
	public static class Finder extends SimpleFileVisitor<Path>
	{
		protected final PathMatcher matcher;
		protected String cutPathPrefix;
		protected Collection<File> dst;
		
		public void setcutPrefix(String pref)
		{
			cutPathPrefix  = pref;
		}
	
		Finder(String pattern) {
			matcher = FileSystems.getDefault()
					.getPathMatcher("glob:" + pattern);
		}
	
		// Compares the glob pattern against
		// the file or directory name.
		void find(Path origin) {
			Path file = origin;
			if(null != cutPathPrefix)
			{
				String str = StringTools.getSubstringAfterFirstString(file.toString(), cutPathPrefix, null);
				if(null != str)
				{
					Path set = new File(str).toPath();
					if(null != set)
					{
						file = set;
					}
				}
			}
			if (file != null && matcher.matches(file))
			{
				if(null != dst)
				{
					dst.add(origin.toFile());
				}
			}
		}
	
		// Invoke the pattern matching
		// method on each file.
		@Override
		public FileVisitResult visitFile(Path file,
				BasicFileAttributes attrs) {
			find(file);
			return FileVisitResult.CONTINUE;
		}
	
		// Invoke the pattern matching
		// method on each directory.
		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) {
			find(dir);
			return FileVisitResult.CONTINUE;
		}
	
		@Override
		public FileVisitResult visitFileFailed(Path file,
				IOException exc) {
			System.err.println(exc);
			return FileVisitResult.CONTINUE;
		}

		public Collection<File> getResultCollection()
		{
			return dst;
		}

		public void setResultCollection(Collection<File> dst)
		{
			this.dst = dst;
		}
	}

	
	public static void globFiles(Collection<File> files, File start, boolean relativeGlob, String glob) throws IOException
	{
		Path startingDir = start.toPath();
		Finder finder = new Finder(glob);
		finder.setResultCollection(files);
		if(relativeGlob)
		{
			String str = start.toString();
			if(start.isDirectory())
			{
				str = StringTools.ensureEndsWith(str, "/");
			}
			finder.setcutPrefix(str);
		}
		Files.walkFileTree(startingDir, finder);
	}

	public static File getDirectory(File f)
	{
		if(f.isDirectory())
		{
			throw new RuntimeException(f+" is already a directory");
		}
		
		return new File(StringTools.getSubstringBeforeLastString(f.toString(), "/", "/"));
	}

	public static void translateFiles
	(
		boolean recursive,
		File fromDir,
		File toDir,
		SimplePublish2<File, File> operation
	)
	{
		if(!fromDir.isDirectory())
		{
			throw new RuntimeException("Source file is not directory: "+fromDir);
		}
		
		if(!toDir.exists())
		{
			toDir.mkdirs();
		}
		
		if(!toDir.isDirectory())
		{
			throw new RuntimeException("Destination file is not directory: "+toDir);
		}
		
		String toDirString = toDir.toString();
		
		for(File f:fromDir.listFiles())
		{
			String last = StringTools.getSubstringAfterLastString(f.toString(), "/");
			File to = new File(toDirString+"/"+last);
			if(f.isDirectory())
			{
				if(recursive)
				{
					translateFiles(recursive, f, to, operation);
				}
			}
			else
			{
				operation.publish(f, to);
			}
		}
	}

	public static void slientDelete(File f)
	{
		try
		{
			f.delete();
		}catch(Exception e){}
	}

	public static String normalizeSlashes(String file)
	{
		return RegexTools.SLASHES_LINUX_WINDOWS.matcher(file).replaceAll("/");
	}

	public static boolean createDirectoryForFile(File f)
	{
		File dir = getDirectory(f);
		return dir.mkdirs();
	}
}
