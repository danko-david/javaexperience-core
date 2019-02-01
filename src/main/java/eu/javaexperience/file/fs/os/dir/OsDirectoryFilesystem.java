package eu.javaexperience.file.fs.os.dir;

import java.io.File;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;
import eu.javaexperience.io.file.FileTools;

public class OsDirectoryFilesystem implements AbstractFileSystem
{
	protected File root;
	
	public OsDirectoryFilesystem(File root)
	{
		this.root = new File(FileTools.normalizeSlashes(root.toString()));
	}

	@Override
	public AbstractFile fromUri(String uri)
	{
		return new OsDirectoryFile(this, uri);
	}

	@Override
	public String getFileSeparator()
	{
		return File.separator;
	}

}
