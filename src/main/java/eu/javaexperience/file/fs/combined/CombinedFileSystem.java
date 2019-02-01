package eu.javaexperience.file.fs.combined;

import java.io.File;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;

public class CombinedFileSystem implements AbstractFileSystem
{
	protected AbstractFileSystem[] fss;
	
	public CombinedFileSystem(AbstractFileSystem... fss)
	{
		this.fss = fss;
	}
	
	@Override
	public AbstractFile fromUri(String uri)
	{
		for(AbstractFileSystem fs:fss)
		{
			AbstractFile ret = fs.fromUri(uri);
			if(null != ret && ret.exists())
			{
				return ret;
			}
		}
		
		return null;
	}

	@Override
	public String getFileSeparator()
	{
		return File.separator;
	}
}
