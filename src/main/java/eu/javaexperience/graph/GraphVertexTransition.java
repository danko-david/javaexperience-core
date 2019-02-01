package eu.javaexperience.graph;

public class GraphVertexTransition<O, V, E> implements Cloneable
{
	protected O owner;
	
	protected V from;
	protected E edge;
	protected V to;
	
	public GraphVertexTransition(O owner, V from, E edge, V to)
	{
		this.owner = owner;
		this.from = from;
		this.edge = edge;
		this.to = to;
	}
	
	public V getFrom()
	{
		return from;
	}
	
	public E getEdge()
	{
		return edge;
	}
	
	public V getTo()
	{
		return to;
	}
	
	@Override
    public GraphVertexTransition<O, V, E> clone() throws CloneNotSupportedException
	{
		return (GraphVertexTransition<O, V, E>) super.clone();
	}
	
	@Override
	public String toString()
	{
		return "GraphVertexTransition: from: "+from+", edge: "+edge+", to: "+to;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(!(obj instanceof GraphVertexTransition))
		{
			return false;
		}
		
		GraphVertexTransition o = (GraphVertexTransition) obj;
		
		return from.equals(o.from) && edge.equals(o.edge) && to.equals(o.to);
	}
	
	@Override
	public int hashCode()
	{
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
		return ret;
	}
	
	public O getOwner()
	{
		return owner;
	}
}
