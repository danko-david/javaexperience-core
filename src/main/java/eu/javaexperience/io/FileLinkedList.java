package eu.javaexperience.io;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import eu.javaexperience.io.FileLinkedList.Link;
import eu.javaexperience.io.file.FileTools;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.semantic.references.MayNull;

/**
 * A linked list stores every entry on disc in a specified directory
 * 
 * Not protected from cyclic referencing. 
 * 
 * add new link:
 * 		- create a new link, by default the id is a newly assigned id
 * 			and parent id is zero, file creatated so
 * 
 * 
 * 
 * */
public class FileLinkedList<T extends Serializable> implements Iterable<Link<T>>
{
	/**
	 * Files in dir:
	 * */
	
	protected final File dir;
	
	public FileLinkedList(File dir)
	{
		if(!dir.exists() || !dir.isDirectory())
		{
			throw new RuntimeException("Given file is not a directory");
		}
		this.dir = dir;
		
		load();
	}
	
	protected void load()
	{
		String[] files = dir.list();

		//load all and create all Link<T> wrapper.
		//determine startpoints, endpoints, max_id
		
		HashMap<Integer, Link<T>> link_registry = new HashMap<>();
		
		//loading elements, find max index
		for(String f: files)
		{
			int index = f.indexOf('.');
			
			if(index < 1)
			{
				continue;
			}
			
			String s1 = f.substring(0, index);
			String s2 = f.substring(index+1, f.length());
			
			int id = Integer.parseInt(s1);
			int pid = Integer.parseInt(s2);
			Link<T> l = new Link<>(this);
			l.id = id;
			if(max_id < id)
			{
				max_id = id;
			}
			
			l.tmp_parent_id = pid;
			link_registry.put(id, l);
			entrys.add(l);
		}
		
		//linking with each others
		for(Link<T> l:entrys)
		{
			if(0 == l.tmp_parent_id)
			{
				continue;
			}

			Link<T> parent = link_registry.get(l.tmp_parent_id);
			//looks like parent destroyed
			
			if(parent == l)//break self referencing 
			{
				parent = null;
			}
			
			if(null == parent)
			{
				l.on_parent_not_found(l.tmp_parent_id);
			}
			else
			{
				l.parent = parent;
				++parent.refcount;
			}
		}
		
		//who has null parent is root, who not referenced is endpoint
		//identify root and endpoints
		for(Link<T> l:entrys)
		{
			l.file = l.generateFileName();
			if(null == l.parent)
			{
				headLinks.add(l);
			}

			if(l.refcount == 0)
			{
				tailLinks.add(l);
			}
		}
	}
	
	//info object: protected Link
	protected Set<Link<T>> entrys = new HashSet<>();
	
	protected final Set<Link<T>> headLinks = new HashSet<>();
	protected final Set<Link<T>> tailLinks = new HashSet<>();
	
	public void fillHeadLinks(Collection<Link<T>> heads)
	{
		heads.addAll(headLinks);
	}
	
	public void fillTailLinks(Collection<Link<T>> tails)
	{
		tails.addAll(tailLinks);
	}
	
	protected int max_id = 0;
	
	public Link<T> newLink(@MayNull Link<T> parent)
	{
		Link<T> l = new Link<>(this, ++max_id, parent);
		return l;
	}
	
	
	public static class Link<T extends Serializable>
	{
		protected transient int tmp_parent_id;
		protected transient int refcount;
		
		protected final FileLinkedList<T> owner;
		
		protected Link(FileLinkedList<T> owner)
		{
			this.owner = owner;
		}
		
		protected Link(FileLinkedList<T> owner, int id, Link<T> parent)
		{
			this(owner);
			this.id = id;
			setParent(parent, true);
			touch();
			owner.entrys.add(this);
			owner.tailLinks.add(this);
		}
		
		protected int id;
		protected File file;
		protected Link<T> parent;
		
		protected int getParentId()
		{
			if(null == parent)
			{
				return 0;
			}
			else
			{
				return parent.id;
			}
		}
		
		protected boolean touch()
		{
			try
			{
				return file.createNewFile();
			}
			catch (IOException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
				return false;//have a nice day
			}
		}
		
		public FileLinkedList<T> getOwner()
		{
			return owner;
		}

		public File _generateFileName(int parent)
		{
			return new File(owner.dir+"/"+id+"."+parent);
		}
		
		public File generateFileName()
		{
			return _generateFileName(getParentId());
		}
		
		@SuppressWarnings("unchecked")
		public T getContent()
		{
			return (T) SerializationTools.deserializeFromFile(file);
		}
		
		public void setContent(T elem)
		{
			SerializationTools.serializeIntoFile(file, elem);
		}
		
		public Link<T> getParent()
		{
			return parent;
		}
		
		public boolean isRootNode()
		{
			return null == parent;
		}
		
		public boolean isEndpoint()
		{
			return owner.tailLinks.contains(this);
		}
		
		public void setParent(Link<T> l)
		{
			setParent(l, false);
		}
		
		/**
		 * If this is a standalone element you can set the parent.
		 * */
		protected void setParent(Link<T> l, boolean first)
		{
			if(!first && parent == l)
			{
				//nothing changed
				return;
			}
			
			//fool test
			if(null != l && owner != l.owner)
			{
				throw new RuntimeException("Mixing Link<T> between different FileLinkedList is an illegal operation. (yet)");
			}
			
			//this node previously was a rootNode now remove from roots
			if(null == parent)//previously checked that new parent is not null.
			{
				owner.headLinks.remove(this);
			}
			else
			{
				--parent.refcount;
				//check i'm the only referencing element,
				//if yes: the parent will be a tail element
				if(0 == parent.refcount)
				{
					owner.tailLinks.add(parent);
				}
			}
			
			if(null == l)
			{
				//new this will be a head node
				owner.headLinks.add(this);
			}
			else
			{
				++l.refcount;
				//if the parent is an endpoint, we inheit it's position so now
				//this now this an endpoint. 
				if(l.isEndpoint())
				{
					owner.tailLinks.remove(l);
					owner.tailLinks.add(this);
				}
			}
			
			parent = l;
			
			if(!first)
			{
				File oldFile = file;
				file = generateFileName();//new name generated to the new parent
				FileTools.move(oldFile, file);//move to the right new name.
			}
			else
			{
				//if first set parent, just generate path name
				file = generateFileName();
			}
		}
		
		/**
		 * If Link<T> between not found at load time.
		 * This moves to root and set as root element
		 * */
		protected void on_parent_not_found(int parent)
		{
			File old = _generateFileName(parent);
			this.parent = null;
			this.file = generateFileName();
			FileTools.move(old, this.file);
		}
	}

	@Override
	public Iterator<Link<T>> iterator()
	{
		return (Iterator<Link<T>>) entrys.iterator();
	}
}
