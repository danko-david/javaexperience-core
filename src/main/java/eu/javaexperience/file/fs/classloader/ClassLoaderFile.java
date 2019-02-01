package eu.javaexperience.file.fs.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;
import eu.javaexperience.file.FileSystemTools;
import eu.javaexperience.file.fs.os.OsFile;
import eu.javaexperience.io.IOTools;

public class ClassLoaderFile implements AbstractFile
{
	protected ClassLoaderFileSystem clfs;
	protected String file;
	
	
	public ClassLoaderFile(ClassLoaderFileSystem clfs, String file)
	{
		this.clfs = clfs;
		this.file = file;
	}
	
	@Override
	public String getUrl()
	{
		return file;
	}

	@Override
	public String getFileName()
	{
		return null;
	}

	@Override
	public AbstractFileSystem getFileSystem()
	{
		return clfs;
	}

	@Override
	public AbstractFile getCanonicalFile() throws IOException
	{
		return this;
	}

	@Override
	public boolean delete() throws IOException
	{
		return false;
	}

	@Override
	public long lastModified()
	{
		return 0;
	}

	@Override
	public boolean setLastModified(long time) throws IOException
	{
		return false;
	}

	@Override
	public boolean exists()
	{
		return null != clfs.cl.getResource(file);
	}

	@Override
	public boolean createNewRegularFile() throws IOException
	{
		return false;
	}

	@Override
	public boolean isRegularFile()
	{
		return exists();
	}

	@Override
	public long getSize()
	{
		try(InputStream r = openRead())
		{
			return IOTools.copyStream(r, IOTools.nullOutputStream);
		}
		catch (IOException e)
		{
			return -1;
		}
	}

	public File tryGetFile()
	{
		URL u = clfs.cl.getResource(file);
		if(null != u)
		{
			return new File(u.getFile());
		}
		return null;
	}
	
	@Override
	public boolean isDirectory()
	{
		File f = tryGetFile();
		if(null == f)
		{
			return false;
		}
		
		return f.isDirectory();
	}

	@Override
	public AbstractFile[] listFiles()
	{
		File f = tryGetFile();
		if(null == f)
		{
			return null;
		}
		
		return FileSystemTools.warpFiles(f.listFiles());
	}

	@Override
	public boolean mkdir() throws IOException
	{
		return false;
	}

	@Override
	public boolean mkdirs() throws IOException
	{
		return false;
	}

	@Override
	public boolean canRead()
	{
		return true;
	}

	@Override
	public boolean canWrite()
	{
		return false;
	}

	@Override
	public InputStream openRead() throws IOException
	{
		return clfs.cl.getResourceAsStream(file);
	}

	@Override
	public OutputStream openWrite(boolean append) throws IOException
	{
		throw new UnsupportedOperationException("Can't open ClassLoaderFile for write: "+file);
	}
	
	@Override
	public String toString()
	{
		return "ClassLoaderFile: "+file;
	}
	
	@Override
	public int hashCode()
	{
		return clfs.hashCode() ^ file.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ClassLoaderFile))
		{
			return false;
		}
		
		ClassLoaderFile other = (ClassLoaderFile) obj;
		
		return other.clfs.equals(clfs) && other.file.equals(file);
	}
	
	@Override
	public AbstractFile getParentFile()
	{
		File f = tryGetFile();
		if(null == f)
		{
			return null;
		}
		return new OsFile(f.getParentFile());
	}
}
