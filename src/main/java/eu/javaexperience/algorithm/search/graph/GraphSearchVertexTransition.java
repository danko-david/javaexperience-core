package eu.javaexperience.algorithm.search.graph;

import eu.javaexperience.graph.GraphVertexTransition;

public class GraphSearchVertexTransition<V, E> extends GraphVertexTransition<GraphPath<V, E>, V, E>
{
	protected GraphSearchVertexTransition<V, E> prev;
	protected GraphSearchVertexTransition<V, E> next;
	
	protected GraphSearchVertexTransition(GraphPath<V, E> path, V from, E edge, V to)
	{
		super(path, from, edge, to);
	}

	public GraphSearchVertexTransition<V, E> addVertexForward(E e, V next)
	{
		if(isInitial())
		{
			edge = e;
			to = next;
			return this;
		}
		else
		{
			GraphSearchVertexTransition<V, E> ret = new GraphSearchVertexTransition<>(owner, to, e, next);
			ret.prev = this;
			this.next = ret;
			owner.registerLast(ret);
			return ret;
		}
	}
	
	public GraphSearchVertexTransition<V, E> addVertexBackward(V prev, E e)
	{
		if(isInitial())
		{
			edge = e;
			from = prev;
			return this;
		}
		else
		{
			GraphSearchVertexTransition<V, E> ret = new GraphSearchVertexTransition<>(owner, prev, e, from);
			
			ret.next = this;
			this.prev = ret;
			owner.registerFirst(ret);
			return ret;
		}
	}
	
	public GraphSearchVertexTransition<V, E> prev()
	{
		return prev;
	}
	
	public GraphSearchVertexTransition<V, E> next()
	{
		return next;
	}
	
	public boolean isInitial()
	{
		return null == this.edge;
	}
	
	void setOwner(GraphPath<V, E> owner)
	{
		this.owner = owner;
	}
	
	protected transient int hashCode = 0;
	
	@Override
	public int hashCode()
	{
		if(0 != hashCode)
		{
			return hashCode;
		}
		
		int ret = 27;
		
		if(null != from)
		{
			ret = 31* ret + from.hashCode();
		}
		
		if(null != edge)
		{
			ret = 31* ret + edge.hashCode();
		}
		
		if(null != to)
		{
			ret = 31* ret + to.hashCode();
		}
		
		return hashCode = ret;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(!(obj instanceof GraphSearchVertexTransition))
		{
			return false;
		}
		
		GraphSearchVertexTransition other = (GraphSearchVertexTransition) obj;
		
		return from.equals(other.from) && (edge == other.edge || edge.equals(other.edge)) && to.equals(other.to);
	}
}
