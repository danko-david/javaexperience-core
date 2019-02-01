package eu.javaexperience.io.test;

import java.io.File;
import java.util.ArrayList;

import eu.javaexperience.io.FileLinkedList;
import eu.javaexperience.io.FileLinkedList.Link;
import eu.javaexperience.io.file.FileTools;

public class FileLinkedListTest
{
	public static void main(String[] args)
	{
		//create new dir in the CWD.
		File dir = new File("./FileLinkedListTest");
		
		if(dir.exists() || dir.isDirectory())
		{
			throw new RuntimeException("Assert Failed directry FileLinkedListTest exists before starting the test. Aborting test.");
		}
		
		dir.mkdirs();
		
		if(!dir.exists() || !dir.isDirectory())
		{
			throw new RuntimeException("Assert Failed directry FileLinkedListTest doesn't exists.");
		}
		
		try
		{
			FileLinkedList<String> ll = new FileLinkedList<>(dir);
			Link<String> root = ll.newLink(null);
			
			root.setContent("Helló");
			
			Link<String> l2 = ll.newLink(root);
			l2.setContent("World");
			
			Link<String> l3 = ll.newLink(root);
			l3.setContent("Tail");
			
			dbg(ll);
			
			dbg(new FileLinkedList<String>(dir));
		}
		finally
		{
			//delete directory with every entry
			FileTools.deleteDirectory(dir, false);
		}
	}
	
	public static void dbg(FileLinkedList<String> fll)
	{
		ArrayList<Link<String>> ends = new ArrayList<>(); 
		
		fll.fillTailLinks(ends);
		for(Link<String> t:ends)
		{
			printStringBackwardToRoot(t);
			System.out.println("");
		}
		System.out.println("------------");
	}
	
	public static void printStringBackwardToRoot(Link<String> l)
	{
		while(null != l)
		{
			System.out.println(l.getContent());
			l = l.getParent();
		}
	}
}