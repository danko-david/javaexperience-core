package eu.javaexperience.algorithm.search.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.javaexperience.collection.map.KeyVal;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.interfaces.ExternalDataAttached;
import eu.javaexperience.reflect.Mirror;

public class GraphSearchTraveledPath<V, E> implements ExternalDataAttached
{
	protected final GraphSearcher<V, E> graphSearch;
	protected GraphSearchDirection direction;
	
	protected GraphPath<V, E> path;
	protected int forkCount = 0;
	
	protected ArrayList<Entry<E, V>> neighborNodes = null;
	//protected ArrayList<GraphSearchTraveledPath<V, E>> forkedBranches = new ArrayList<>();
	protected Set<Entry<E, V>> processedNeighborVertexes = new HashSet<>();
	
	public GraphSearchTraveledPath
	(
		GraphSearcher<V, E> graphSearch,
		GraphSearchDirection direction
	)
	{
		this.graphSearch = graphSearch;
		this.direction = direction;
		path = new GraphPath<>();
	}
	
	public V getFirstVertex()
	{
		return path.first.getFrom();
	}

	public V getLastVertex()
	{
		return path.last.getTo();
	}
	
	public GraphSearchVertexTransition<V, E> getFirstNode()
	{
		return path.first;
	}
	
	public GraphSearchVertexTransition<V, E> getLastNode()
	{
		return path.last;
	}
	
	public GraphSearchDirection getSearchDirection()
	{
		return direction;
	}
	
	public GraphSearchTraveledPath<V, E> copy()
	{
		GraphSearchTraveledPath<V, E> ret = new GraphSearchTraveledPath<>(graphSearch, direction);
		try
		{
			ret.path = this.path.clone();
		}
		catch (CloneNotSupportedException e)
		{
			Mirror.propagateAnyway(e);
		}
		
		ret.neighborNodes = null;
		ret.processedNeighborVertexes = new HashSet<>();
		return ret;
	}

	public V getDirectionVertex()
	{
		switch(direction)
		{
			case BACKWARD:	return getFirstVertex();
			case BIDIRECTIONAL: return null;
			case FORWARD: return getLastVertex();
			default: throw new UnimplementedCaseException(direction);
		}
	}
	
	public V getDirectionPrevVertex()
	{
		switch(direction)
		{
			case BACKWARD:	return getFirstNode().getTo();
			case BIDIRECTIONAL: return null;
			case FORWARD: return getLastNode().getFrom();
			default: throw new UnimplementedCaseException(direction);
		}
	}
	
	public Entry<E, V> getDirectionPrevVertexEntry()
	{
		switch(direction)
		{
			case BACKWARD:	return new KeyVal(getFirstNode().getEdge(), getFirstNode().getTo());
			case BIDIRECTIONAL: return null;
			case FORWARD: return new KeyVal(getFirstNode().getEdge(), getLastNode().getFrom());
			default: throw new UnimplementedCaseException(direction);
		}
	}
	
	public GraphPath<V, E> getPath()
	{
		return path;
	}
	
	public List<Entry<E, V>> getOrCacheNeighborNodes(GraphSearchSubject<V, E> subject)
	{
		if(null == neighborNodes)
		{
			neighborNodes = new ArrayList<>();
			subject.fillNeighborVertexes(getDirectionVertex(), neighborNodes);
		}
		
		return neighborNodes;
	}
	
	public GraphSearchTraveledPath<V, E> fork(GraphSearchSubject<V, E> subject, Entry<E, V> node)
	{
		GraphSearchTraveledPath<V, E> ob = this;
		final GraphSearchDirection dir = ob.getSearchDirection();
		
		GraphSearchTraveledPath<V, E> ret = ob.copy();
		
		if
		(
			dir == GraphSearchDirection.BACKWARD
		||
			(
				dir == GraphSearchDirection.BIDIRECTIONAL
			&&
				ob.getFirstVertex() == node
			)
		)
		{
			ret.getFirstNode().addVertexBackward
			(
				node.getValue(),
				node.getKey()
			);
		}
		else if
		(
			dir == GraphSearchDirection.FORWARD
		||
			(
				dir == GraphSearchDirection.BIDIRECTIONAL
			&&
				ob.getLastVertex() == node
			)
		)
		{
			ret.getLastNode().addVertexForward
			(
				node.getKey(),
				node.getValue()
			);
		}
		else
		{
			throw new UnimplementedCaseException(ob.getSearchDirection());
		}
		
		this.processedNeighborVertexes.add(node);
		
		++this.forkCount;
		return ret;
	}
	
	public boolean hasUndiscoveredBranch(GraphSearchSubject<V, E> subj)
	{
		return getOrCacheNeighborNodes(subj).size() > processedNeighborVertexes.size();
	}
	
	public void fillOpenVertexes(GraphSearcher<V, E> search, Collection<Entry<E, V>> dst)
	{
		for(Entry<E, V> v:getOrCacheNeighborNodes(search.subject))
		{
			if(!processedNeighborVertexes.contains(v))
			{
				dst.add(v);
			}
		}
	}
	
	public GraphSearchTraveledPath<V, E> merge(GraphSearchTraveledPath<V, E> with)
	{
		/*
		 * TODO this affects `hasUndiscoveredBranch` because we may megre a branch
		 * that this already forked:
		 * 	
		 *  this = just A
		 *  
		 *   A's forked path: A -> B
		 *   
		 *   if only B available from A, and
		 *   
		 *   other branch to merge: B -> C
		 *   
		 *   A's forks list: A -> B and A -> B -> C
		 *   (so it' duplicated)
		 */
		
		//"this" is the backward path and the "with" is the forward 
		if(getFirstVertex().equals(with.getLastVertex()))
		{
			GraphSearchTraveledPath<V, E> ret = new GraphSearchTraveledPath<V, E>(graphSearch, direction);
			ret.neighborNodes = neighborNodes;
			ret.processedNeighborVertexes = processedNeighborVertexes;
			ret.path = with.path.merge(path);
			
			this.processedNeighborVertexes.add(with.getDirectionPrevVertexEntry());
			with.processedNeighborVertexes.add(this.getDirectionPrevVertexEntry());
			
			return ret;
		}
		else if(getLastVertex().equals(with.getFirstVertex()))
		{
			GraphSearchTraveledPath<V, E> ret = new GraphSearchTraveledPath<V, E>(graphSearch, direction);
			ret.neighborNodes = with.neighborNodes;
			ret.processedNeighborVertexes = with.processedNeighborVertexes;
			ret.path = path.merge(with.path);
			
			this.processedNeighborVertexes.add(with.getDirectionPrevVertexEntry());
			with.processedNeighborVertexes.add(this.getDirectionPrevVertexEntry());
			
			return ret;
		}
		
		throw new RuntimeException("Can't merge `"+this+"` with `"+with+"`");
	}
	
	@Override
	public String toString()
	{
		return "GraphSearchTraveledPath: "+path;
	}

	public boolean hasFork()
	{
		return forkCount > 0;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(!(obj instanceof GraphSearchTraveledPath))
		{
			return false; 
		}
		return path.equals(((GraphSearchTraveledPath)obj).path);
	}
	
	@Override
	public int hashCode()
	{
		return path.hashCode();
	}

	
/**************************** extra data attachment ***************************/
	protected Map<String, Object> extraData;
	
	@Override
	public Map<String, Object> getExtraDataMap()
	{
		if(null == extraData)
		{
			extraData = new SmallMap<>();
		}
		return extraData;
	}
}
