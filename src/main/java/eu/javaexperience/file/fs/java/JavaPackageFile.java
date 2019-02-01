package eu.javaexperience.file.fs.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;

public class JavaPackageFile implements AbstractFile
{
	@Override
	public String getUrl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractFileSystem getFileSystem()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractFile getCanonicalFile() throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete() throws IOException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long lastModified() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setLastModified(long time) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createNewRegularFile() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRegularFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDirectory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AbstractFile[] listFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mkdir() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mkdirs() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canWrite() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InputStream openRead() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream openWrite(boolean append) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractFile getParentFile() {
		// TODO Auto-generated method stub
		return null;
	}

}
