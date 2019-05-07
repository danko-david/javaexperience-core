package eu.javaexperience.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.javaexperience.io.file.TmpFile;

public class FileBasedSetTest
{
	@Test
	public void testAdd() throws Throwable
	{
		try(TmpFile tmp = new TmpFile("/tmp/", ".jvx-FileBasedSetTest"))
		{
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertTrue(set.add("Hello"));
				assertTrue(set.add("World"));
				
				assertEquals(2, set.size());
				assertTrue(set.contains("Hello"));
				assertTrue(set.contains("World"));
			}
			
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertEquals(2, set.size());
				assertTrue(set.contains("Hello"));
				assertTrue(set.contains("World"));
				
				assertTrue(set.remove("World"));
			}
		}
	}
	
	@Test
	public void testAddRemove() throws Throwable
	{
		try(TmpFile tmp = new TmpFile("/tmp/", ".jvx-FileBasedSetTest"))
		{
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertTrue(set.add("Hello"));
				assertTrue(set.add("World"));
				
				assertEquals(2, set.size());
				assertTrue(set.contains("Hello"));
				assertTrue(set.contains("World"));
			}
			
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertEquals(2, set.size());
				assertTrue(set.contains("Hello"));
				assertTrue(set.contains("World"));
				
				assertTrue(set.remove("World"));
			}
			
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertEquals(1, set.size());
				assertTrue(set.contains("Hello"));
			}
		}
	}
	
	@Test
	public void testReAdd() throws Throwable
	{
		try(TmpFile tmp = new TmpFile("/tmp/", ".jvx-FileBasedSetTest"))
		{
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertTrue(set.add("Hello"));
				assertTrue(set.add("World"));
				
				assertEquals(2, set.size());
				assertTrue(set.contains("Hello"));
				assertTrue(set.contains("World"));
			}
			
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertEquals(2, set.size());
				assertTrue(set.contains("Hello"));
				assertTrue(set.contains("World"));
				
				assertTrue(set.remove("World"));
			}
			
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertEquals(1, set.size());
				assertTrue(set.contains("Hello"));
				
				assertTrue(set.add("World"));
			}
			
			try(FileBasedSet set = new FileBasedSet(tmp.getFile()))
			{
				assertEquals(2, set.size());
				assertTrue(set.contains("Hello"));
				assertTrue(set.contains("World"));
			}
		}
	}
}
