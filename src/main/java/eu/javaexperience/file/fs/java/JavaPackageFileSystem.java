package eu.javaexperience.file.fs.java;

import java.io.File;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;

public class JavaPackageFileSystem implements AbstractFileSystem
{
	@Override
	public AbstractFile fromUri(String uri)
	{
		return null;
	}

	@Override
	public String getFileSeparator()
	{
		return File.separator;
	}
}
