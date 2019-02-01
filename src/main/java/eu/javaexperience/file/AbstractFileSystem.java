package eu.javaexperience.file;

public interface AbstractFileSystem
{
	public AbstractFile fromUri(String uri);

	public String getFileSeparator();
	
	//TODO roots, mount points, df
}
