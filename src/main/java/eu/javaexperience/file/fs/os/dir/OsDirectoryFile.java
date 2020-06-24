package eu.javaexperience.file.fs.os.dir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;
import eu.javaexperience.io.file.FileTools;
import eu.javaexperience.text.StringTools;

public class OsDirectoryFile implements AbstractFile
{
	protected OsDirectoryFilesystem dfs;
	protected String file;
	
	public OsDirectoryFile(OsDirectoryFilesystem dfs, String file)
	{
		this.dfs = dfs;
		this.file = FileTools.normalizeSlashes(file);
	}

	@Override
	public String getUrl()
	{
		return file;
	}

	@Override
	public String getFileName()
	{
		String ret = StringTools.getSubstringAfterLastString(file, "/");
		if(ret.length() == 0)
		{
			ret = StringTools.getSubstringBeforeLastString(file, "/");
			ret = StringTools.getSubstringAfterFirstString(ret, "/");
		}
		return ret;
	}

	@Override
	public AbstractFileSystem getFileSystem()
	{
		return dfs;
	}
	
	protected String unwrap(File c)
	{
		if(null == c)
		{
			return null;
		}
		
		String cs = c.toString();
		
		String root = dfs.root.toString();
		
		if(!cs.startsWith(root))
		{
			return null;
		}
		
		return StringTools.getSubstringAfterFirstString(c.toString(), root, null);
	}

	@Override
	public AbstractFile getCanonicalFile() throws IOException
	{
		String ret = unwrap(new File(dfs.root+"/"+file).getCanonicalFile());
		if(null == ret)
		{
			return null;
		}
		
		return new OsDirectoryFile(dfs, ret);
	}

	protected File asFile()
	{
		return new File(dfs.root.toString()+"/"+file);
	}
	
	@Override
	public boolean delete() throws IOException
	{
		return asFile().delete();
	}

	@Override
	public long lastModified()
	{
		return asFile().lastModified();
	}

	@Override
	public boolean setLastModified(long time) throws IOException
	{
		return asFile().setLastModified(time);
	}

	@Override
	public boolean exists()
	{
		return asFile().exists();
	}

	@Override
	public boolean createNewRegularFile() throws IOException
	{
		return asFile().createNewFile();
	}

	@Override
	public boolean isRegularFile()
	{
		return asFile().isFile();
	}

	@Override
	public long getSize()
	{
		return asFile().length();
	}

	@Override
	public boolean isDirectory()
	{
		return asFile().isDirectory();
	}

	@Override
	public AbstractFile[] listFiles()
	{
		File[] fs = asFile().listFiles();
		if(null == fs)
		{
			return null;
		}
		
		AbstractFile[] ret = new AbstractFile[fs.length];
		int l = 0;
		
		for(int i=0;i<ret.length;++i)
		{
			String add = unwrap(fs[i]);
			if(null != add)
			{
				ret[l++] = new OsDirectoryFile(dfs, add);
			}
		}
		
		if(ret.length == l)
		{
			return ret;
		}
		return Arrays.copyOf(ret, l);
	}

	@Override
	public boolean mkdir() throws IOException
	{
		return asFile().mkdir();
	}

	@Override
	public boolean mkdirs() throws IOException
	{
		return asFile().mkdirs();
	}

	@Override
	public boolean canRead()
	{
		return asFile().canRead();
	}

	@Override
	public boolean canWrite()
	{
		return asFile().canWrite();
	}

	@Override
	public InputStream openRead() throws IOException
	{
		return new FileInputStream(asFile());
	}

	@Override
	public OutputStream openWrite(boolean append) throws IOException
	{
		return new FileOutputStream(asFile(), append);
	}

	@Override
	public AbstractFile getParentFile()
	{
		String f = null;
		if(file.endsWith("/"))
		{
			f = StringTools.getLastBetween(file, "/", "/", null);
		}
		else
		{
			f = StringTools.getSubstringAfterLastString(file, "/", null);
		}
		
		if(null == f)
		{
			return null;
		}
		
		return new OsDirectoryFile(dfs, f);
	}

	@Override
	public long createTime()
	{
		return 0;
	}
}
