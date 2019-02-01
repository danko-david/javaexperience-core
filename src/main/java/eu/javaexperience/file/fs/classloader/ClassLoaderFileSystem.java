package eu.javaexperience.file.fs.classloader;

import java.io.File;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;

public class ClassLoaderFileSystem implements AbstractFileSystem
{
	protected ClassLoader cl;
	
	public ClassLoaderFileSystem(ClassLoader cl)
	{
		this.cl = cl;
	}
	
	@Override
	public AbstractFile fromUri(String uri)
	{
		return new ClassLoaderFile(this, uri);
	}

	@Override
	public String getFileSeparator()
	{
		return File.separator;
	}
}
