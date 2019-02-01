package eu.javaexperience.reflect;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;

public class FdMirror
{	
	private static final Field fdVal;
	
	static
	{
		Field f = null;
		try {
			f = FileDescriptor.class.getDeclaredField("fd");
			f.setAccessible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		fdVal = f;
	}
	
	public static int getFD(FileDescriptor fd)
	{
		try
		{
			return fdVal.getInt(fd);
		}
		catch(Exception e)
		{
			throw new RuntimeException("Az állományleírók kiolvasása nem lehetséges",e);
		}
	}
	
	private static Class<?> sockFileDes = null;
	
	
	static
	{
		Socket.class.toString();
		try
		{
			Class.forName("java.net.AbstractPlainSocketImpl");
		}
		catch(Exception e){}
	}
	
	public static boolean isGettingFileDescriptorFromSocketIsAvailable()
	{
		if(sockFileDes != null)
			return true;
		else
			try
			{
				sockFileDes = Class.forName("java.net.SocketInputStream");
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
	}

	public static FileDescriptor getFileDescriptorFromSocket(Socket s) throws IOException
	{
		if(sockFileDes == null)
			try
			{
				sockFileDes = Class.forName("java.net.SocketInputStream");
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		return ((FileInputStream)sockFileDes.cast(s.getInputStream())).getFD();
	}
	
	public static void setFd(FileDescriptor ofd,int fd)
	{
		try
		{
			fdVal.set(ofd, fd);
		}
		catch(Exception e)
		{
			throw new RuntimeException("Az állományleírók kiolvasása nem lehetséges",e);
		}
	}
}
