package eu.javaexperience.file.fs.os;

import java.io.File;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;

public class OsFileSystem implements AbstractFileSystem
{
	@Override
	public AbstractFile fromUri(String uri)
	{
		return new OsFile(new File(uri));
	}

	@Override
	public String getFileSeparator()
	{
		return File.separator;
	}
};