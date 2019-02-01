package eu.javaexperience.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationTools
{

	/**
	 * Deserialize the content of the file or throws {@link RuntimeException}
	 * */
	public static Serializable deserializeFromFile(File dst)
	{
		try
		(
			FileInputStream fis = new FileInputStream(dst);
			ObjectInputStream ois = new ObjectInputStream(fis);
		)
		{
			return (Serializable) ois.readObject();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Serialise object into file or throws {@link RuntimeException} 
	 * */
	public static void serializeIntoFile(File f, Serializable value)
	{
		try
		(
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos)
		)
		{
			oos.writeObject(value);
			oos.flush();
			fos.flush();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Serializable deserializeFromBlob(byte[] src)
	{
		try
		(
			ByteArrayInputStream fis = new ByteArrayInputStream(src);
			ObjectInputStream ois = new ObjectInputStream(fis);
		)
		{
			return (Serializable) ois.readObject();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] serializeIntoBlob(Serializable value)
	{
		try
		(
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos)
		)
		{
			oos.writeObject(value);
			oos.flush();
			return baos.toByteArray();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

}
