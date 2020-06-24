package eu.javaexperience.file.fs.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.zip.ZipEntry;

import eu.javaexperience.collection.tree.TreeNode;
import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;
import eu.javaexperience.file.FileSystemTools;
import eu.javaexperience.reflect.Mirror;

public class ZipFsFile implements AbstractFile
{
	protected ZipFileSystem fs;
	protected String file;
	protected ZipEntry ent;
	
	public ZipFsFile(ZipFileSystem fs, String file)
	{
		this.fs = fs;
		this.file = file;
		//this.ent = fs.zip.getEntry(file);
		TreeNode tn = fs.getNodeByPath(file);
		if(null != tn)
		{
			this.ent = (ZipEntry) tn.getEtc("ent");
		}
	}
	
	@Override
	public String getUrl()
	{
		return file;
	}

	@Override
	public String getFileName()
	{
		String[] p = FileSystemTools.decomposePath(file);
		if(0 == p.length)
		{
			return "";
		}
		return p[p.length-1];
	}

	@Override
	public AbstractFileSystem getFileSystem()
	{
		return fs;
	}

	@Override
	public AbstractFile getCanonicalFile() throws IOException
	{
		if(null == ent)
		{
			return null;
		}
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
		if(null == ent)
		{
			return 0;
		}
		return ent.getLastModifiedTime().toMillis();
	}

	@Override
	public boolean setLastModified(long time) throws IOException
	{
		if(null == ent)
		{
			return false;
		}
		ent.setLastModifiedTime(FileTime.fromMillis(time));
		return true;
	}

	@Override
	public boolean exists()
	{
		return null != ent;
	}

	@Override
	public boolean createNewRegularFile() throws IOException
	{
		return false;
	}

	@Override
	public boolean isRegularFile()
	{
		if(null == ent)
		{
			return false;
		}
		
		return !ent.isDirectory();
	}

	@Override
	public long getSize()
	{
		if(ent == null)
		{
			return 0;
		}
		return ent.getSize();
	}

	@Override
	public boolean isDirectory()
	{
		if(null == ent)
		{
			return false;
		}
		
		TreeNode tn = fs.getNodeByPath(file);
		if(null != tn)
		{
			if(!tn.getChilds().isEmpty())
			{
				//System.out.println(this+" isDirectory: childs true");
				return true;
			}
		}
		//System.out.println(this+" isDirectory: "+ent.isDirectory());
		return ent.isDirectory();
	}

	@Override
	public AbstractFile[] listFiles()
	{
		if(null == ent)
		{
			return null;
		}
		
		TreeNode tn = fs.getNodeByPath(file);
		
		if(null == tn)
		{
			return null;
		}
		
		List<TreeNode> chs = tn.getChilds();
		
		AbstractFile[] ret = new AbstractFile[chs.size()];
		
		for(int i=0;i<chs.size();++i)
		{
			TreeNode ch = chs.get(i);
			ret[i] = new ZipFsFile(fs, fs.toPath(ch));
		}
		
		return ret;
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
		return null != ent;
	}

	@Override
	public boolean canWrite()
	{
		return false;
	}

	public void assertExists() throws IOException
	{
		if(null == ent)
		{
			throw new IOException("File `"+file+"` in zip `"+fs.file+"` doesn't exists.");
		}
	}
	
	@Override
	public InputStream openRead() throws IOException
	{
		assertExists();
		return fs.zip.getInputStream(ent);
	}

	@Override
	public OutputStream openWrite(boolean append) throws IOException
	{
		throw new IOException("Zip archive is not writeable `"+file+"` in zip `"+fs.file+"`");
	}

	@Override
	public AbstractFile getParentFile()
	{
		TreeNode tn = fs.getNodeByPath(file);
		if(null == tn)
		{
			return null;
		}
		
		tn = tn.getParent();
		if(null == tn)
		{
			return null;
		}
		
		return new ZipFsFile(fs, fs.toPath(tn));
	}
	
	@Override
	public String toString()
	{
		return "FipFsFile: `"+file+"` in `"+fs.file+"`";
	}

	@Override
	public long createTime()
	{
		return ent.getCreationTime().toMillis();
	}
}
