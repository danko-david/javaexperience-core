package eu.javaexperience.collection.tree;

import java.util.ArrayList;
import java.util.Collection;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class TreeNodeTools
{
	public static <N extends TreeNode<N>, E> N getByPath(N root, E[] elems, GetBy1<E, N> getNodeId)
	{
		return getByPath(root, elems, 0, getNodeId);
	}
	
	public static <N extends TreeNode<N>, E> N getByPath(N root, E[] elems, int lvl, GetBy1<E, N> getNodeId)
	{
		if(elems.length == lvl)
		{
			return root;
		}
		
		E tar = elems[lvl];
		for(N c:root.childs)
		{
			if(tar.equals(getNodeId.getBy(c)))
			{
				return getByPath(c, elems, lvl+1, getNodeId);
			}
		}
		
		return null;
	}
	
	public static <N extends TreeNode<N>, E> N getOrCreatePath(N root, E[] elems, GetBy1<E, N> getNodeId, GetBy1<N, E> createNode)
	{
		return getOrCreatePath(root, elems, 0, getNodeId, createNode);
	}
	
	public static <N extends TreeNode<N>, E> N getOrCreatePath(N root, E[] elems, int lvl, GetBy1<E, N> getNodeId, GetBy1<N, E> createNode)
	{
		if(elems.length == lvl)
		{
			return root;
		}
		
		E tar = elems[lvl];
		for(N c:root.childs)
		{
			if(tar.equals(getNodeId.getBy(c)))
			{
				return getOrCreatePath(c, elems, lvl+1, getNodeId, createNode);
			}
		}
		
		//create and add child
		N ch = createNode.getBy(tar);
		root.addChild(ch);
		
		return getOrCreatePath(ch, elems, lvl+1, getNodeId, createNode);
	}

	public static <N extends TreeNode<N>, E> void getPathToRoot(Collection<E> path, N node, GetBy1<E, N> getNodeId)
	{
		if(null != node)
		{
			do
			{
				path.add(getNodeId.getBy(node));
			}
			while(null != (node = node.getParent()));
		}
	}
}
