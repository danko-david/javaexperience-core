package eu.javaexperience.file.fs.os;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;
import eu.javaexperience.file.FileSystemTools;
import eu.javaexperience.semantic.references.MayNull;

public class OsFile implements AbstractFile
{
	protected final File file;
	
	public OsFile(File f)
	{
		this.file = f;
	}
	
	public OsFile(String f)
	{
		this.file = new File(f);
	}
	
	@Override
	public String getUrl()
	{
		return file.toString();
	}

	@Override
	public AbstractFileSystem getFileSystem()
	{
		return FileSystemTools.DEFAULT_FILESYSTEM;
	}

	@Override
	public AbstractFile getCanonicalFile() throws IOException
	{
		File ret = file.getCanonicalFile();
		if(null == ret)
		{
			return null;
		}
		
		return new OsFile(ret);
	}

	@Override
	public boolean isDirectory()
	{
		return file.isDirectory();
	}

	@Override
	public @MayNull AbstractFile[] listFiles()
	{
		File[] r = file.listFiles();
		if(null == r)
		{
			return null;
		}
		
		AbstractFile[] ret = new AbstractFile[r.length]; 
		for(int i=0;i<ret.length;++i)
		{
			ret[i] = new OsFile(r[i]); 
		}
		
		return ret;
	}

	@Override
	public boolean mkdir()
	{
		return file.mkdir();
	}

	@Override
	public boolean mkdirs()
	{
		return file.mkdirs();
	}

	@Override
	public long lastModified()
	{
		return file.lastModified();
	}
	
	@Override
	public boolean canRead()
	{
		return file.canRead();
	}

	@Override
	public boolean canWrite()
	{
		return file.canWrite();
	}

	@Override
	public boolean delete()
	{
		return file.delete();
	}

	@Override
	public boolean setLastModified(long time)
	{
		return file.setLastModified(time);
	}

	@Override
	public boolean createNewRegularFile() throws IOException
	{
		return file.createNewFile();
	}

	@Override
	public InputStream openRead() throws FileNotFoundException
	{
		return new FileInputStream(file);
	}

	@Override
	public OutputStream openWrite(boolean append) throws FileNotFoundException
	{
		return new FileOutputStream(file, append);
	}

	@Override
	public boolean exists()
	{
		return file.exists();
	}

	@Override
	public boolean isRegularFile()
	{
		return file.isFile();
	}

	@Override
	public long getSize()
	{
		return file.length();
	}

	@Override
	public String getFileName()
	{
		return file.getName();
	}

	@Override
	public AbstractFile getParentFile()
	{
		File p = file.getParentFile();
		if(null == p)
		{
			return null;
		}
		
		return new OsFile(p);
	}
	
	@Override
	public String toString()
	{
		return file.toString();/*"OsFile: "+*/
	}
	
	@Override
	public int hashCode()
	{
		return file.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof OsFile))
		{
			return false;
		}
		return file.equals(((OsFile)obj).file);
	}

	public File getFile()
	{
		return file;
	}
}