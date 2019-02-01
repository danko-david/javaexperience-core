package eu.javaexperience.collection.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.text.StringTools;

public class TreeNode<N extends TreeNode<N>> implements Iterable<N>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1353360183790746251l;

	public static TreeNode[] emptyTreeNodeArray = new TreeNode[0];
	
	//externalize this
	protected Map<Object, Object> etc;
	
	public Object getEtc(Object key)
	{
		if(null == etc)
		{
			return null;
		}
		
		return etc.get(key);
	}
	
	public Object putEtc(Object key, Object val)
	{
		if(null == etc)
		{
			etc = new SmallMap<>();
		}
		
		return etc.put(key, val);
	}
	
	protected void clearEtc()
	{
		if(null != etc)
		{
			etc.clear();
		}
	}
	//
	
	protected N parent;
	protected ArrayList<N> childs = new ArrayList<>();
	
	public void reorderChilds(Comparator<N> cmp)
	{
		Collections.sort(childs, cmp);
	}
	
	public void addChild(N child)
	{
		if(null != child.parent)
		{
			throw new RuntimeException("TreeNode already have parent.");
		}
		child.parent = (N) this;
		childs.add(child);
	}
	
	public N getParent()
	{
		return parent;
	}
	
	public N setParent(N parent)
	{
		N ret = this.parent;
		this.parent = parent;
		return ret;
	}
	
	public final void unlink()
	{
		if(null != parent)
		{
			parent.childs.remove(this);
		}
		
		this.parent = null;
	}
	
	public String getPath(TreeMerger<N> merger, String separator)
	{
		return appendYourselfBefore(merger, separator, null);
	}
	
	protected String appendYourselfBefore
	(
		TreeMerger<N> merger,
		String separator,
		String currentString
	)
	{
		String myName = merger.getNameOf((N) this);
		if(StringTools.isNullOrTrimEmpty(currentString))
		{
			currentString = myName;
		}
		else
		{
			currentString = myName+separator+currentString;
			
		}
		
		if(null != parent)
		{
			return parent.appendYourselfBefore(merger, separator, currentString);
		}
		
		return currentString;
	}
	
	@Override
	public Iterator<N> iterator()
	{
		return childs.iterator();
	}
	
	public void iterateThisAndChilds(SimplePublish1<N> pub)
	{
		pub.publish((N) this);
		for(N ch:this)
		{
			ch.iterateThisAndChilds(pub);
		}
	}
	
	public int getLevel()
	{
		if(null == parent)
		{
			return 0;
		}
		
		return parent.getLevel()+1;
	}
	
	public void iterateAllChild
	(
		SimplePublish1<N> pub
	)
	{
		for(N n:this)
		{
			n.iterateThisAndChilds(pub);
		}
	}

	public List<N> getChilds()
	{
		return childs;
	}

	public void walkTree(SimplePublish1<? extends TreeNode<N>> publish)
	{
		((SimplePublish1)publish).publish(this);
		for(N c:childs)
		{
			c.walkTree(publish);
		}
	}
}
