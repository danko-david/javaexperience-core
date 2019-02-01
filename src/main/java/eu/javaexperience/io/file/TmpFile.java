package eu.javaexperience.io.file;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import eu.javaexperience.io.IOTools;

public class TmpFile implements Closeable
{
	protected File file;
	
	public TmpFile(String prefix, String postfix) throws IOException
	{
		file = FileTools.generateTempFilename(prefix, 50, postfix);
	}
	
	public File getFile()
	{
		return file;
	}
	
	public void create() throws IOException
	{
		file.createNewFile();
	}
	
	public FileInputStream openRead() throws FileNotFoundException
	{
		return new FileInputStream(file);
	}
	
	public PrintWriter openWriter() throws FileNotFoundException
	{
		return new PrintWriter(file);
	}
	
	public FileOutputStream openWrite() throws FileNotFoundException
	{
		return new FileOutputStream(file);
	}
	
	@Override
	public void close() throws IOException
	{
		file.delete();
	}

	public byte[] getFileBytes() throws IOException
	{
		return IOTools.loadFileContent(file.toString());
	}
}
