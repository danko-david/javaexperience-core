package eu.javaexperience.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public class IOFunctions
{
	public static final GetBy1<String, File> GET_FILE_CONTENT = new GetBy1<String, File>()
	{
		@Override
		public String getBy(File a)
		{
			try
			{
				return IOTools.getFileContents(a);
			}
			catch (IOException e)
			{
				Mirror.propagateAnyway(e);
				return null;
			}
		}
	};
	
	public static GetBy1<String, File> getFileContent()
	{
		return GET_FILE_CONTENT ;
	}

	public static final GetBy1<byte[], File> LOAD_FILE_CONTENT = new GetBy1<byte[], File>()
	{
		@Override
		public byte[] getBy(File a)
		{
			try
			{
				return IOTools.loadFileContent(a.toString());
			}
			catch (IOException e)
			{
				Mirror.propagateAnyway(e);
				return null;
			}
		}
	};
	
	public static final GetBy1<byte[], AbstractFile> LOAD_AFILE_CONTENT = new GetBy1<byte[], AbstractFile>()
	{
		@Override
		public byte[] getBy(AbstractFile a)
		{
			try(InputStream is = a.openRead())
			{
				return IOTools.loadAllFromInputStream(is);
			}
			catch (IOException e)
			{
				Mirror.propagateAnyway(e);
				return null;
			}
		}
	};
	
	public static GetBy1<byte[], File> loadFileContent()
	{
		return LOAD_FILE_CONTENT;
	}
}
