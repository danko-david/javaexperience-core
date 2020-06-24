package eu.javaexperience.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.javaexperience.semantic.references.MayNull;

public interface AbstractFile
{
	//java bindings functions
	public String getUrl();
	
	public String getFileName();
	
	public AbstractFileSystem getFileSystem();
	
	//general file functions
	public AbstractFile getCanonicalFile() throws IOException;
	public boolean delete() throws IOException;
	public long lastModified();
	public boolean setLastModified(long time) throws IOException;
	
	public long createTime();
	
	public boolean exists();
	
	public boolean createNewRegularFile() throws IOException;
	
	public boolean isRegularFile();
	
	public long getSize();
	
	//Directory type related functions
	public boolean isDirectory();
	public @MayNull AbstractFile[] listFiles();
	public boolean mkdir() throws IOException;
	public boolean mkdirs() throws IOException;
	
	
	
	
	//IO
	public boolean canRead();
	public boolean canWrite();
	
	public InputStream openRead() throws IOException;
	public OutputStream openWrite(boolean append) throws IOException;
	public AbstractFile getParentFile();
	
	
	
}
