package eu.javaexperience.collection.tree;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.exceptions.OperationSuccessfullyEnded;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish3;
import eu.javaexperience.reflect.Mirror;

public abstract class TreeMerger<N extends TreeNode<N>> implements Iterable<N>, Serializable
{
	protected ArrayList<N> roots = new ArrayList<>();
	protected Map<Object, N> idToNode = new HashMap<>();
	
	public void mergeChain(N node)
	{
		mergeInternal(null, node);
	}
	
	protected void mergeInternal(N prevIn, N crnt)
	{
		Object id = getIdOf(crnt);
		TreeNode in = idToNode.get(id);
		
		//if node is absent from the registry, we register it.
		if(null == in)
		{
			crnt.setParent(null);
			
			//link to the previous node that found in the registry tree
			if(null != prevIn)
			{
				prevIn.addChild(crnt);
			}
			else
			{
				//no previous node found (in the registry), this gonna be a root node.
				roots.add(crnt);
			}
			
			//import descendant nodes
			indexFromDescendants(crnt);
			return;
		}
		//if node available in the registry we don't care with.
		else
		{
			for(TreeNode c:crnt.childs.toArray(TreeNode.emptyTreeNodeArray))
			{
				//traversing up
				mergeInternal((N) in, (N) c);
			}
		}
	}
	
	//note: parameter may be modified
	
	protected void indexFromDescendants(N crnt)
	{
		idToNode.put(getIdOf(crnt), crnt);
		for(N tn :crnt)
		{
			indexFromDescendants(tn);
		}
	}
	
	public Iterable<N> iterateAll()
	{
		return idToNode.values();
	}
	
	public abstract Object getIdOf(N crnt);
	
	public abstract String getNameOf(N node);
	
	/*public static final SimplePublish3<Appendable, TreeNode, TreeMerger> HTML_COLLAPSE_TREE_RENDERER =
		new SimplePublish3<Appendable, TreeNode,TreeMerger>()
	{
		@Override
		public void publish(Appendable app, TreeNode node, TreeMerger logic)
		{
			try
			{
				
			}
			catch(Exception e)
			{
				Mirror.propagateAnyway(e);
			}
		}
	};*/
	
	
	
	public static <N extends TreeNode<N>> void renderNodeHtml(Appendable app, TreeMerger<N> logic, N node) throws IOException
	{
		app.append("<ul>\n");
		app.append("\t<li><input type=\"checkbox\" id=\""+node.hashCode()+"\"/><label "+(node.childs.size() == 0?"class=\"leaf\"":"")+" for=\""+node.hashCode()+"\" data-cid=\""+logic.getIdOf(node)+"\">");
		app.append(logic.getNameOf(node));
		app.append("["+node+"]");
		app.append("</label>\n");
		
		for(N c:node)
		{
			renderNodeHtml(app, logic, c);
		}
			
		app.append("</ul>");
	}
	
	public void toHtmlTree(Appendable app) throws IOException
	{
		app.append(getCollapseCSS());
		for(N n:roots)
		{
			renderNodeHtml(app, this, n);
		}
	}
	
	public static <P, N extends TreeNode<N>> void render
	(
		P param,
		SimplePublish3<P, N, TreeMerger<N>> renderer,
		TreeMerger<N> logic,
		N node
	)
	{
		if(null != node)
		{
			renderer.publish(param, node, logic);
		}
		
		Iterable<N> it = null == node? logic.roots:node; 
		
		for(N n:it)
		{
			render(param, renderer, logic, n);
		}
	}
	
	public <P> void render(P param, SimplePublish3<P, N, TreeMerger<N>> renderer)
	{
		for(N n:roots)
		{
			render(param, renderer, this, n);
		}
	}
	
	public String toHtmlTree() throws IOException
	{
		StringBuilder sb = new StringBuilder();
		toHtmlTree(sb);
		return sb.toString();
	}
	
	protected static String COLLAPSE_CSS = "";
	
	public static String getCollapseCSS()
	{
		return COLLAPSE_CSS;
	}
	
	static
	{
		try
		{
			COLLAPSE_CSS = new String(Mirror.getPackageResource(TreeMerger.class, "collapse.css"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public Iterator<N> iterator()
	{
		return roots.iterator();
	}
	
	protected N findById(N node, Object id)
	{
		if(getIdOf(node).equals(id))
		{
			return node;
		}
		
		for(N n: node)
		{
			N ret = findById(n, id);
			if(null != ret)
			{
				return ret;
			}
		}
		
		return null;
	}
	
	
	public N findById(Object s)
	{
		for(N root:roots)
		{
			N ret = findById(root, s);
			if(null != ret)
			{
				return ret;
			}
		}
		
		return null;
	}

	public void ensureKnown(N node)
	{
		indexFromDescendants(node);
		if(null == node.parent)
		{
			roots.add(node);
		}
	}
	
	public String[] getPath(N node)
	{
		String[] path = (String[]) node.getEtc("str_path");
		if(null == path)
		{
			ArrayList<String> re = new ArrayList<>();
			
			N n = node;
			do
			{
				re.add(getNameOf(n));
			}
			while(null != (n = n.parent));
			
			path = ArrayTools.modifyReverse(re.toArray(Mirror.emptyStringArray));
			node.putEtc("str_path", path);
		}
		
		return path;
	}
	
	public boolean tryMatchBackward(N node, String... path)
	{
		//return ArrayTools.isEndsWith(getPath(node), path);
		
		String[] pattern = path;
		String[] search = getPath(node);
		
		if(pattern.length > search.length)
		{
			return false;
		}
		
		for(int i=0;i<pattern.length;++i)
		{
			if(!pattern[pattern.length-1-i].equalsIgnoreCase(search[search.length-1-i]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public int findAllByName(Collection<N> nodes, String... path)
	{
		int ret = 0;
		for(Entry<Object, N> kv:idToNode.entrySet())
		{
			if(tryMatchBackward(kv.getValue(), path))
			{
				++ret;
				nodes.add(kv.getValue());
			}
		}
		
		return ret;
	}
	
	public N findSingleUnique(String... name)
	{ 
		ArrayList<N> re = new ArrayList<>();
		findAllByName(re, name);
		if(0 == re.size())
		{
			return null;
		}
		else if(1 == re.size())
		{
			return re.get(0);
		}
		
		throw new RuntimeException("Node name not unique: "+Arrays.toString(name));
	}

	public void reindex()
	{
		idToNode.clear();
		
		SimplePublish1<N> add = new SimplePublish1<N>()
		{
			@Override
			public void publish(N a)
			{
				idToNode.put(getIdOf(a), a);
			}
		};
		
		for(N r:roots)
		{
			r.iterateThisAndChilds(add);
		}
	}

	public void unlink(N node)
	{
		node.unlink();
		if(null == node.parent)
		{
			roots.remove(node);
		}
		
		reindex();
	}

	public void dropCategoryAndAdoptChilds(N elem)
	{
		N parent = elem.parent;
		elem.unlink();
		if(null != parent)
		{
			for(N e:elem)
			{
				e.parent = elem;
				parent.addChild(e);
			}
		}
		else
		{
			roots.remove(elem);
			for(N e:elem)
			{
				e.parent = null;
				roots.add(e);
			}
		}
		reindex();
	}

	public int getAbsOrder(final N node)
	{
		final int[] ordinal_registry = new int[]{0, -1};
		
		SimplePublish1<N> it = new SimplePublish1<N>()
		{
			@Override
			public void publish(N a)
			{
				if(a == node)
				{
					ordinal_registry[1] = ordinal_registry[0];
					throw OperationSuccessfullyEnded.instance;
				}
				++ordinal_registry[0];
			}
		};
		
		try 
		{
			for(N n:roots)
			{
				n.iterateThisAndChilds(it);
			}
		}
		catch(OperationSuccessfullyEnded end)
		{
			/*fast iteration exit*/
		}
		
		return ordinal_registry[1];
	}

	public void orderTree(Comparator<N> cmp)
	{
		Collections.sort(roots, cmp);
		for(N c:iterateAll())
		{
			c.reorderChilds(cmp);
		}
	}
	
	public boolean tryAddChild(Object parentId, N childNode)
	{
		N p = idToNode.get(parentId);
		if(null == p)
		{
			return false;
		}
		else
		{
			p.addChild(childNode);
			indexFromDescendants(childNode);
			return true;
		}
	}
	
	public void fillNodesInOrder(Collection<? super N> dst)
	{
		for(N n: roots)
		{
			n.iterateThisAndChilds(new SimplePublish1<N>()
			{
				@Override
				public void publish(N a)
				{
					dst.add(a);					
				}
			});
		}
	}

	public void findNodesByName(Collection<N> nodes, GetBy1<Boolean, String> contains)
	{
		for(Entry<Object, N> kv:idToNode.entrySet())
		{
			if(contains.getBy(getNameOf(kv.getValue())))
			{
				nodes.add(kv.getValue());
			}
		}
	}
	
	public void walkTree(SimplePublish1<? extends TreeNode<N>> publish)
	{
		for(N e:roots)
		{
			e.walkTree(publish);
		}
	}
}
